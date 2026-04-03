package com.goormthon5backend.service.guest_book;

import com.goormthon5backend.domain.enums.GuestBookType;
import com.goormthon5backend.dto.guest_book.GuestBookDto;
import com.goormthon5backend.repository.user.UserRepository;
import com.goormthon5backend.repository.accommodation.AccommodationRepository;
import com.goormthon5backend.repository.guest_book.GuestBookRepository;
import com.goormthon5backend.repository.guest_book.GuestbookImageRepository;
import com.goormthon5backend.service.accommodation.AccommodationAiAsyncService;
import com.goormthon5backend.service.file.S3FileService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class GuestBookService {

    private final AccommodationRepository accommodationRepository;
    private final UserRepository userRepository;
    private final GuestBookRepository guestBookRepository;
    private final GuestbookImageRepository guestbookImageRepository;
    private final S3FileService s3FileService;
    private final AccommodationAiAsyncService accommodationAiAsyncService;

    public List<GuestBookDto.ListItemDto> getAccommodationGuestBooks(Long accommodationId) {
        if (!accommodationRepository.existsById(accommodationId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "숙소를 찾을 수 없습니다.");
        }

        return guestBookRepository.findListByAccommodationId(accommodationId);
    }

    @Transactional
    public void createGuestBook(Long accommodationId, GuestBookDto.CreateRequest request, MultipartFile imageFile)
        throws IOException {

        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        GuestBookType type = request.validateAndGetType(imageFile);

        try {
            Long guestBookId = guestBookRepository.createGuestBook(
                    accommodationId,
                    request.userId(),
                    request.content().trim(),
                    type,
                    request.rating()
            );

            if (type == GuestBookType.IMAGE) {
                String imageUrl = resolveImageUrl(imageFile);
                Long imageId = guestbookImageRepository.createImage(imageUrl);
                guestbookImageRepository.createGuestbookImage(guestBookId, imageId);
            }

            maybeRunAiRewriteInBackgroundAfterCommit(accommodationId);

        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "유효하지 않은 사용자 또는 숙소");
        }
    }

    private String resolveImageUrl(MultipartFile imageFile) throws IOException {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "IMAGE 타입은 image 파일이 필수입니다.");
        }
        return s3FileService.upload(imageFile).fileUrl();
    }

    private void maybeRunAiRewriteInBackgroundAfterCommit(Long accommodationId) {
        long guestBookCount = guestBookRepository.countByAccommodationId(accommodationId);
        if (guestBookCount != 5L) {
            return;
        }

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    accommodationAiAsyncService.rewriteAccommodationAsync(accommodationId);
                }
            });
            return;
        }

        accommodationAiAsyncService.rewriteAccommodationAsync(accommodationId);
    }
}
