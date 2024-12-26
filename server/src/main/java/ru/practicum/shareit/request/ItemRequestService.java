package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto findById(Long userId, Long requestId);

    List<ItemRequestDto> findByRequester(Long userId);

    List<ItemRequestDto> findAll(Long userId);

    ItemRequestDto save(Long userId, CreateItemRequestDto request);

    void delete(Long requestId);
}
