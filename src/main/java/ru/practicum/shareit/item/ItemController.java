package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.Set;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private static final String HEADER_USER_PARAMETER = "X-Sharer-User-Id";

    private final ItemService itemService;

    @GetMapping
    public Set<ItemDto> getAll(@RequestHeader(value = HEADER_USER_PARAMETER) Integer userId) {
        return itemService.getByUser(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto get(@PathVariable Integer itemId) {
        return itemService.get(itemId);
    }

    @PostMapping
    public ItemDto add(@RequestHeader(HEADER_USER_PARAMETER) Integer userId, @Valid @RequestBody CreateItemDto itemDto) {
        return itemService.add(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(HEADER_USER_PARAMETER) Integer userId,
                          @PathVariable Integer itemId,
                          @Valid @RequestBody UpdateItemDto newItemDto) {
        return itemService.update(userId, itemId, newItemDto);
    }

    @GetMapping("/search")
    public Set<ItemDto> search(@RequestHeader(HEADER_USER_PARAMETER) Integer userId,
                               @RequestParam() String text) {
        return itemService.search(text);
    }

    @DeleteMapping("/{itemId}")
    public void remove(@PathVariable(name = "itemId") Integer itemId) {
        itemService.remove(itemId);
    }

}
