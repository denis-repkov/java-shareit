package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    private static final String HEADER_USER_PARAMETER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> createRequest(
            @RequestHeader(HEADER_USER_PARAMETER) Long userId,
            @RequestBody ItemRequestDto dto
    ) {
        return itemRequestClient.createRequest(userId, dto);
    }

    @GetMapping
    public ResponseEntity<Object> findByUser(
            @RequestHeader(HEADER_USER_PARAMETER) Long userId) {
        return itemRequestClient.findByUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAll(
            @RequestHeader(HEADER_USER_PARAMETER) Long userId) {
        return itemRequestClient.findAll(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findByRequest(
            @RequestHeader(HEADER_USER_PARAMETER) Long userId,
            @PathVariable Long requestId
    ) {
        return itemRequestClient.findByRequest(userId, requestId);
    }
}
