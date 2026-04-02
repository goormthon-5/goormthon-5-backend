package com.goormthon5backend.service.guest_book;

import com.goormthon5backend.dto.guest_book.GuestBookDto;
import com.goormthon5backend.repository.accommodation.AccommodationRepository;
import com.goormthon5backend.repository.guest_book.GuestBookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class GuestBookService {

    private final AccommodationRepository accommodationRepository;
    private final GuestBookRepository guestBookRepository;

    public List<GuestBookDto.ListItemDto> getAccommodationGuestBooks(Long accommodationId) {
        if (!accommodationRepository.existsById(accommodationId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "숙소를 찾을 수 없습니다.");
        }

        return guestBookRepository.findListByAccommodationId(accommodationId);
    }
}
