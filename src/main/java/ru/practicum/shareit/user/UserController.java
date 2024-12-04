package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody CreateUserDto user) {
        return userService.create(user);
    }

    @GetMapping("/{userId}")
    public UserDto get(@PathVariable int userId) {
        return userService.get(userId);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable Integer userId, @Valid @RequestBody UpdateUserDto userDto) {
        return userService.update(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void remove(@PathVariable Integer userId) {
        userService.remove(userId);
    }
}
