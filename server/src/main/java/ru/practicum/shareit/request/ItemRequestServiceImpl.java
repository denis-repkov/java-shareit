package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.AuthentificationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dal.ItemRequestRepository;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dal.UserRepository;

import java.util.List;

import static ru.practicum.shareit.exception.ErrorMessages.NOT_AUTHENTICATED_USER;
import static ru.practicum.shareit.exception.ErrorMessages.REQUEST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository requestRepository;

    private final ItemRequestMapper itemRequestMapper;

    private final UserRepository userRepository;

    private final ItemRequestRepository itemRequestRepository;

    @Override
    @Transactional(readOnly = true)
    public ItemRequestDto findById(Long userId, Long requestId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new AuthentificationException(NOT_AUTHENTICATED_USER);
        }
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(() -> new NotFoundException(REQUEST_NOT_FOUND + requestId));
        return itemRequestMapper.map(itemRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDto> findByRequester(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new AuthentificationException(NOT_AUTHENTICATED_USER);
        }
        List<ItemRequest> requests = requestRepository.findByRequester(userId);
        return requests.stream()
                .map(itemRequestMapper::map)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDto> findAll(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new AuthentificationException(NOT_AUTHENTICATED_USER);
        }
        List<ItemRequest> requests = requestRepository.findAll();
        return requests.stream()
                .map(itemRequestMapper::map)
                .toList();
    }

    @Override
    @Transactional
    public ItemRequestDto save(Long userId, CreateItemRequestDto request) {
        return userRepository.findById(userId)
                .map(user -> itemRequestMapper.map(userId, request))
                .map(itemRequestRepository::save)
                .map(itemRequestMapper::map)
                .orElseThrow(() -> new AuthentificationException(NOT_AUTHENTICATED_USER));
    }

    @Override
    @Transactional
    public void delete(Long requestId) {
        requestRepository.deleteById(requestId);
    }
}
