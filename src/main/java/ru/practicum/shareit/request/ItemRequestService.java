package ru.practicum.shareit.request;

import ru.practicum.shareit.request.model.ItemRequest;

public interface ItemRequestService {
    ItemRequest findById(Long requestId);

    ItemRequest save(ItemRequest request);

    void delete(Long requestId);
}
