package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    UserDto create(CreateUserDto userDto);

    UserDto get(int id);

    UserDto update(Integer userId, UpdateUserDto userDto);

    void remove(Integer id);
}
