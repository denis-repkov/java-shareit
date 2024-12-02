package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody CreateUserDto user) {
        User toCreate = userMapper.map(user);
        User createdFilm = userService.create(toCreate);
        return userMapper.map(createdFilm);
    }

    @GetMapping("/{userId}")
    public UserDto get(@PathVariable int userId) {
        User user = userService.get(userId);
        return userMapper.map(user);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable Integer userId, @Valid @RequestBody UpdateUserDto userDto) {
        User user = userMapper.map(userDto);
        User updatedUser = userService.update(userId, user);
        return userMapper.map(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public void remove(@PathVariable Integer userId) {
        userService.remove(userId);
    }
}
