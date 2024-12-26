package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Component
public class ItemMapper {

    public ItemDto map(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner().getId())
                .request(item.getRequest())
                .comments(item.getComments() == null ? null : item.getComments().stream()
                        .map(Comment::getText)
                        .toList())
                .build();
    }

    public Item map(CreateItemDto item, User owner) {
        return Item.builder()
                .owner(owner)
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(item.getRequestId())
                .build();
    }

    public Item map(UpdateItemDto item, Long itemId, Item currentItem) {
        return Item.builder()
                .id(itemId)
                .name(item.getName() != null ? item.getName() : currentItem.getName())
                .description(item.getDescription() != null ? item.getDescription() : currentItem.getDescription())
                .available(item.getAvailable() != null ? item.getAvailable() : currentItem.getAvailable())
                .owner(currentItem.getOwner())
                .comments(currentItem.getComments())
                .request(currentItem.getRequest())
                .build();
    }

    public Item map(ItemDto item, User owner) {
        return Item.builder()
                .id(item.getId())
                .owner(owner)
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(item.getRequest())
                .build();
    }
}