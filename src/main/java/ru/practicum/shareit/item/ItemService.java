package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {
    List<ItemDto> findByOwner(Long ownerId);

    List<ItemDto> findAll(Long userId);

    ItemDto findById(Long itemId);

    ItemDto save(Long ownerId, CreateItemDto itemDto);

    ItemDto save(Long ownerId, Long itemId, UpdateItemDto itemDto);

    void deleteById(Long itemId);

    List<ItemDto> search(String text);

    CommentDto commentItem(Long userId,
                           Long itemId,
                           CreateCommentDto commentDto);
}
