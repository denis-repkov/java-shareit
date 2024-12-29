package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    UserDto save(CreateUserDto userDto);

    UserDto findById(Long id);

    UserDto update(Long userId, UpdateUserDto userDto);

    void deleteById(Long id);
}
