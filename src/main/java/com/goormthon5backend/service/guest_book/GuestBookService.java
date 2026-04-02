package com.goormthon5backend.service.guest_book;

import com.goormthon5backend.domain.enums.GuestBookType;
import com.goormthon5backend.dto.guest_book.GuestBookDto;
import com.goormthon5backend.repository.UserRepository;
import com.goormthon5backend.repository.accommodation.AccommodationRepository;
import com.goormthon5backend.repository.guest_book.GuestBookRepository;
import com.goormthon5backend.repository.guest_book.GuestbookImageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class GuestBookService {

    private final AccommodationRepository accommodationRepository;
    private final UserRepository userRepository;
    private final GuestBookRepository guestBookRepository;
    private final GuestbookImageRepository guestbookImageRepository;

    public List<GuestBookDto.ListItemDto> getAccommodationGuestBooks(Long accommodationId) {
        if (!accommodationRepository.existsById(accommodationId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "숙소를 찾을 수 없습니다.");
        }

        return guestBookRepository.findListByAccommodationId(accommodationId);
    }

    @Transactional
    public void createGuestBook(Long accommodationId, GuestBookDto.CreateRequest request) {

        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        GuestBookType type = request.validateAndGetType();

        try {
            Long guestBookId = guestBookRepository.createGuestBook(
                    accommodationId,
                    request.userId(),
                    request.content().trim(),
                    type,
                    request.rating()
            );

            if (type == GuestBookType.IMAGE) {
                Long imageId = guestbookImageRepository.createImage(request.imageUrl().trim());
                guestbookImageRepository.createGuestbookImage(guestBookId, imageId);
            }

        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "유효하지 않은 사용자 또는 숙소");
        }
    }
}
