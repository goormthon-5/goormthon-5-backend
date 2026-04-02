package com.goormthon5backend.web.guest_book;

import com.goormthon5backend.dto.guest_book.GuestBookDto;
import com.goormthon5backend.service.guest_book.GuestBookService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accommodations/{accommodationId}/guest-books")
public class GuestBookController {

    private final GuestBookService guestBookService;

    @GetMapping
    public List<GuestBookDto.ListItemDto> list(@PathVariable Long accommodationId) {
        return guestBookService.getAccommodationGuestBooks(accommodationId);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void create(
        @PathVariable Long accommodationId,
        @RequestPart("request") GuestBookDto.CreateRequest request,
        @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        guestBookService.createGuestBook(accommodationId, request, image);
    }
}
