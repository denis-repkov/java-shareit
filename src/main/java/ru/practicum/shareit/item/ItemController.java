package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private static final String HEADER_USER_PARAMETER = "X-Sharer-User-Id";

    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader(value = HEADER_USER_PARAMETER) Long userId) {
        return itemService.findAll(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto get(@PathVariable Long itemId) {
        return itemService.findById(itemId);
    }

    @PostMapping
    public ItemDto save(@RequestHeader(HEADER_USER_PARAMETER) Long ownerId, @Valid @RequestBody CreateItemDto itemDto) {
        return itemService.save(ownerId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(HEADER_USER_PARAMETER) Long userId,
                          @PathVariable Long itemId,
                          @Valid @RequestBody UpdateItemDto itemDto) {
        return itemService.save(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader(HEADER_USER_PARAMETER) Long userId,
                               @RequestParam() String text) {
        return itemService.search(text);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable(name = "itemId") Long itemId) {
        itemService.deleteById(itemId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto commentItem(@RequestHeader(HEADER_USER_PARAMETER) Long userId,
                                  @PathVariable(name = "itemId") Long itemId,
                                  @RequestBody CreateCommentDto commentDto) {
        return itemService.commentItem(userId, itemId, commentDto);
    }
}
