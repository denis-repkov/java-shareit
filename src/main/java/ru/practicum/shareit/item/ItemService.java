package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Set;

public interface ItemService {
    Set<ItemDto> getByUser(Integer userId);

    Set<Item> findAll();

    ItemDto get(Integer itemId);

    ItemDto add(CreateItemDto itemDto, Integer userId);

    ItemDto update(Integer requestor, Integer itemId, UpdateItemDto newItem);

    void remove(Integer itemId);

    Set<ItemDto> search(String text);
}
