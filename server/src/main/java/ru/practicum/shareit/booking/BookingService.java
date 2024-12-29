package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {
    BookingDto save(CreateBookingDto dto, Long bookerId);

    BookingDto processBooking(Long userId, Long bookingId, Boolean approved);

    BookingDto findById(Long id, Long userId);

    List<BookingDto> findByBookerAndState(Long bookerId, BookingState state);

    List<BookingDto> findByOwnerAndState(Long ownerId, BookingState state);
}
