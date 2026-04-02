package com.goormthon5backend.web.guest_book;

import com.goormthon5backend.dto.guest_book.GuestBookDto;
import com.goormthon5backend.service.guest_book.GuestBookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accommodations/{accommodationId}/guest-books")
public class GuestBookController {

    private final GuestBookService guestBookService;

    @GetMapping
    public List<GuestBookDto.ListItemDto> list(@PathVariable Long accommodationId) {
        return guestBookService.getAccommodationGuestBooks(accommodationId);
    }
}
