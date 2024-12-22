package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dal.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import static ru.practicum.shareit.exception.ErrorMessages.REQUEST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    ItemRequestRepository requestRepository;

    @Override
    public ItemRequest findById(Long requestId) {
        return requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException(REQUEST_NOT_FOUND + requestId));
    }

    @Override
    public ItemRequest save(ItemRequest request) {
        return requestRepository.save(request);
    }

    @Override
    public void delete(Long requestId) {
        requestRepository.deleteById(requestId);
    }
}
