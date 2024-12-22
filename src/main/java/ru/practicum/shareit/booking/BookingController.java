package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    public static final String HEADER_USER_PARAMETER = "X-Sharer-User-Id";

    @PostMapping
    BookingDto create(@RequestHeader(HEADER_USER_PARAMETER) Long bookerId,
                      @Valid @RequestBody CreateBookingDto dto) {
        return bookingService.save(dto, bookerId);
    }

    @PatchMapping("/{bookingId}")
    BookingDto processBooking(@RequestHeader(HEADER_USER_PARAMETER) Long userId,
                              @PathVariable Long bookingId,
                              @RequestParam Boolean approved) {
        return bookingService.processBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    BookingDto findById(@RequestHeader(HEADER_USER_PARAMETER) Long userId,
                        @PathVariable Long bookingId) {
        return bookingService.findById(bookingId, userId);
    }

    @GetMapping
    List<BookingDto> findByBookerAndState(@RequestHeader(HEADER_USER_PARAMETER) Long bookerId,
                                          @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingService.findByBookerAndState(bookerId, state);
    }

    @GetMapping("/owner")
    List<BookingDto> findByOwnerAndState(@RequestHeader(HEADER_USER_PARAMETER) Long ownerId,
                                         @RequestParam(defaultValue = "ALL") BookingState state) {

        return bookingService.findByOwnerAndState(ownerId, state);
    }
}
