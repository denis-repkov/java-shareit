package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static ru.practicum.shareit.exception.ErrorMessages.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public UserDto create(CreateUserDto userDto) {
        User user = userMapper.map(userDto);
        if (userRepository.containsByEmail(user.getEmail())) {
            throw new ValidationException("Указанный адрес эл.почты уже занят другим пользователем");
        }
        return userMapper.map(userRepository.add(user));
    }

    @Override
    public UserDto get(int id) {
        if (!userRepository.containsById(id)) {
            throw new NotFoundException(USER_NOT_FOUND + id);
        }
        return userMapper.map(userRepository.find(id));
    }

    @Override
    public UserDto update(Integer userId, UpdateUserDto userDto) {
        if (!userRepository.containsById(userId)) {
            throw new NotFoundException(USER_NOT_FOUND + userId);
        }
        if (userRepository.containsByEmail(userDto.getEmail())) {
            throw new ValidationException("Указанный адрес эл.почты уже занят другим пользователем");
        }
        User updatedUser = userRepository.find(userId);
        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            updatedUser.setName(userDto.getName());
        }

        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
            updatedUser.setEmail(userDto.getEmail());
        }
        return userMapper.map(updatedUser);
    }

    @Override
    public void remove(Integer id) {
        User user = userRepository.remove(id);
        if (user == null) {
            throw new NotFoundException(USER_NOT_FOUND + id);
        }
    }
}
