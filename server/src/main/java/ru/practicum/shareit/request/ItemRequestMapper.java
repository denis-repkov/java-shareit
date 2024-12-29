package ru.practicum.shareit.request;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;

@Component
public class ItemRequestMapper {

    private final ItemMapper itemMapper;

    public ItemRequestMapper(ItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    public final ItemRequest map(Long userId, CreateItemRequestDto dto) {
        return ItemRequest.builder()
                .requester(userId)
                .created(LocalDateTime.now())
                .description(dto.getDescription())
                .build();
    }

    public final ItemRequest map(ItemRequestDto dto) {
        return ItemRequest.builder()
                .id(dto.getId())
                .requester(dto.getRequester())
                .created(dto.getCreated())
                .description(dto.getDescription())
                .build();
    }

    ItemRequestDto map(ItemRequest dto) {
        return ItemRequestDto.builder()
                .id(dto.getId())
                .requester(dto.getRequester())
                .created(dto.getCreated())
                .description(dto.getDescription())
                .items(dto.getItems() == null ? null : dto.getItems().stream()
                        .map(itemMapper::map)
                        .toList())
                .build();
    }
}
