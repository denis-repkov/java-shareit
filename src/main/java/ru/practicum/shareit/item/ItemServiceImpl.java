package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dal.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.AuthentificationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.WrongArgumentsException;
import ru.practicum.shareit.item.dal.CommentRepository;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.exception.ErrorMessages.ITEM_NOT_FOUND;
import static ru.practicum.shareit.exception.ErrorMessages.USER_NOT_FOUND;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    private final ItemMapper itemMapper;

    private final CommentMapper commentMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> findByOwner(Long ownerId) {
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        List<Item> items = itemRepository.findByOwner(owner);
        return items.stream()
                .map(itemMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> findAll(Long userId) {
        List<Item> items;
        if (userId != null) {
            User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
            items = itemRepository.findByOwner(owner);
        } else {
            items = itemRepository.findAll();
        }
        return items.stream()
                .map(itemMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto findById(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND + itemId));
        return itemMapper.map(item);
    }

    @Override
    @Transactional
    public ItemDto save(Long ownerId, CreateItemDto itemDto) {
        return userRepository.findById(ownerId)
                .map(owner -> itemMapper.map(itemDto, owner))
                .map(itemRepository::save)
                .map(itemMapper::map)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + ownerId));
    }

    @Override
    @Transactional
    public ItemDto save(Long ownerId, Long itemId, UpdateItemDto itemDto) {
        Item currentItem = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND + itemId));
        if (!currentItem.getOwner().getId().equals(ownerId)) {
            throw new AuthentificationException("Данные по товару может обновлять только его владелец");
        }
        Item item = itemMapper.map(itemDto, itemId, currentItem);
        item.setOwner(currentItem.getOwner());
        Item savedItem = itemRepository.save(item);
        if (userRepository.findById(ownerId).isEmpty()) {
            throw new NotFoundException(USER_NOT_FOUND + itemId);
        }
        return itemMapper.map(savedItem);
    }

    @Override
    @Transactional
    public void deleteById(Long itemId) {
        if (itemRepository.findById(itemId).isEmpty()) {
            throw new NotFoundException(ITEM_NOT_FOUND + itemId);
        }
        itemRepository.deleteById(itemId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.search(text).stream()
                .map(itemMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto commentItem(Long userId,
                                  Long itemId,
                                  CreateCommentDto commentDto) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND + itemId));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + userId));
        Comment comment = Comment.builder()
                .item(item)
                .user(user)
                .text(commentDto.getText())
                .created(LocalDateTime.now())
                .build();
        List<Booking> bookings = bookingRepository.findByBookerAndItemAndStatus(
                comment.getUser(),
                comment.getItem(),
                BookingStatus.APPROVED);

        if (bookings.isEmpty()) {
            throw new WrongArgumentsException("Оставить комментарий может только тот, кто забронировал этот товар");
        }
        Comment createdComment = commentRepository.save(comment);
        return commentMapper.map(createdComment);
    }
}
