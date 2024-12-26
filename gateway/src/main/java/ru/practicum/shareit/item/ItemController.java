package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private static final String HEADER_USER_PARAMETER = "X-Sharer-User-Id";

    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> findAll(
            @RequestHeader(value = HEADER_USER_PARAMETER) Long userId) {
        return itemClient.findAll(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findById(
            @RequestHeader(HEADER_USER_PARAMETER) long userId,
            @PathVariable Long itemId) {
        return itemClient.findById(userId, itemId);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(
            @RequestHeader(HEADER_USER_PARAMETER) Long ownerId,
            @Valid @RequestBody CreateItemDto itemDto) {
        return itemClient.createItem(ownerId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(
            @RequestHeader(HEADER_USER_PARAMETER) Long userId,
            @PathVariable long itemId,
            @Valid @RequestBody UpdateItemDto dto) {
        return itemClient.updateItem(userId, itemId, dto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(
            @RequestHeader(HEADER_USER_PARAMETER) Long userId,
            @RequestParam(name = "text") String text) {
        return itemClient.search(userId, text);
    }

    @DeleteMapping("/{itemId}")
    public void deleteById(@RequestHeader(HEADER_USER_PARAMETER) Long userId,
                           @PathVariable(name = "itemId") Long itemId) {
        itemClient.deleteById(userId, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> commentItem(
            @RequestHeader(HEADER_USER_PARAMETER) Long userId,
            @PathVariable(name = "itemId") Long itemId,
            @RequestBody CreateCommentDto commentDto) {
        return itemClient.commentItem(userId, itemId, commentDto);
    }
}
