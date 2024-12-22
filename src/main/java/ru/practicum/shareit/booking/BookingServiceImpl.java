package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dal.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.AuthentificationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.WrongArgumentsException;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareit.exception.ErrorMessages.BOOKING_NOT_FOUND;
import static ru.practicum.shareit.exception.ErrorMessages.ITEM_NOT_FOUND;
import static ru.practicum.shareit.exception.ErrorMessages.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    private final BookingMapper bookingMapper;

    @Override
    public BookingDto save(CreateBookingDto dto, Long bookerId) {
        if (!dto.getStart().isBefore(dto.getEnd())) {
            throw new WrongArgumentsException("Дата начала бронирования должна предшествовать дате окончания");
        }

        User booker = userRepository.findById(bookerId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + bookerId));

        Long itemId = dto.getItemId();
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND + itemId));

        if (item.getAvailable().equals(false)) {
            throw new WrongArgumentsException("Невозможно забронировать недоступный товар: " + itemId);
        }
        Booking toCreate = bookingMapper.map(dto, booker, item);
        Booking createdBooking = bookingRepository.save(toCreate);
        return bookingMapper.map(createdBooking);
    }

    @Override
    public BookingDto processBooking(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(BOOKING_NOT_FOUND + bookingId));
        Item item = booking.getItem();

        Long itemOwner = item.getOwner().getId();
        if (!itemOwner.equals(userId)) {
            throw new AuthentificationException("Только владелец товара может одобрить/отклонить бронирование");
        }

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        Booking updatedBooking = bookingRepository.save(booking);
        return bookingMapper.map(updatedBooking);
    }

    @Override
    public BookingDto findById(Long id, Long userId) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(BOOKING_NOT_FOUND + id));
        Item item = booking.getItem();
        Long ownerId = item.getOwner().getId();

        if (!userId.equals(booking.getBooker().getId()) && !userId.equals(ownerId)) {
            throw new AuthentificationException("Только заказчик и владелец товара могут просматривать бронирование");
        }
        return bookingMapper.map(booking);
    }

    @Override
    public List<BookingDto> findByBookerAndState(Long bookerId, BookingState state) {
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + bookerId));
        List<Booking> bookings = bookingRepository.findByBookerAndState(booker, state.toString());
        List<BookingDto> bookingDtoList = new ArrayList<>();

        for (Booking booking : bookings) {
            bookingDtoList.add(bookingMapper.map(booking));
        }
        return bookingDtoList;
    }

    @Override
    public List<BookingDto> findByOwnerAndState(Long ownerId, BookingState state) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + ownerId));
        List<Booking> bookings = bookingRepository.findByOwnerAndState(owner, state.toString());
        List<BookingDto> bookingDtoList = new ArrayList<>();

        for (Booking booking : bookings) {
            bookingDtoList.add(bookingMapper.map(booking));
        }
        return bookingDtoList;
    }
}