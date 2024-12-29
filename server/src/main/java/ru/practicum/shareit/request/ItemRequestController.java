package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private static final String HEADER_USER_PARAMETER = "X-Sharer-User-Id";

    private final ItemRequestMapper requestMapper;

    private final ItemRequestService requestService;

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto create(@RequestHeader(HEADER_USER_PARAMETER) Long userId,
                                 @RequestBody CreateItemRequestDto dto) {
        return requestService.save(userId, dto);
    }

    @GetMapping
    public List<ItemRequestDto> findByUser(@RequestHeader(HEADER_USER_PARAMETER) Long userId) {
        return requestService.findByRequester(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAll(@RequestHeader(HEADER_USER_PARAMETER) Long userId) {
        return requestService.findAll(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findByRequest(
            @RequestHeader(HEADER_USER_PARAMETER) Long userId,
            @PathVariable Long requestId) {
        return requestService.findById(userId, requestId);
    }
}
