package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
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
    @Transactional
    public UserDto save(CreateUserDto userDto) {
        User user = userMapper.map(userDto);
        User savedUser = userRepository.save(user);
        return userMapper.map(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + id));
        return userMapper.map(user);
    }

    @Override
    @Transactional
    public UserDto update(Long userId, UpdateUserDto userDto) {
        User currentUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + userId));
        User user = userMapper.map(userDto, userId, currentUser);
        User updatedUser = userRepository.save(user);
        return userMapper.map(updatedUser);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
