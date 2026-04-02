package com.goormthon5backend.web.reservation;

import com.goormthon5backend.dto.reservation.ReservationDto;
import com.goormthon5backend.service.reservation.ReservationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public List<ReservationDto.ListItemDto> list(@RequestParam(required = false) Long userId) {
        return reservationService.getReservationList(userId);
    }

    @PostMapping
    public ReservationDto.CreateResponse create(@RequestBody ReservationDto.CreateRequest request) {
        return reservationService.createReservation(request);
    }
}
