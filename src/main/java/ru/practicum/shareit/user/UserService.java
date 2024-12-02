package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.model.User;

import static ru.practicum.shareit.exception.ErrorMessages.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService {

    private static int idCounter = 0;
    private final UserRepository userRepository;

    public User create(User user) {
        if (userRepository.containsByEmail(user.getEmail())) {
            throw new ValidationException("Указанный адрес эл.почты уже занят другим пользователем");
        }
        user.setId(getCounter());
        return userRepository.add(user);
    }

    public User get(int id) {
        if (!userRepository.containsById(id)) {
            throw new NotFoundException(USER_NOT_FOUND + id);
        }
        return userRepository.find(id);
    }

    public User update(Integer userId, User newUser) {
        if (!userRepository.containsById(userId)) {
            throw new NotFoundException(USER_NOT_FOUND + userId);
        }
        if (userRepository.containsByEmail(newUser.getEmail())) {
            throw new ValidationException("Указанный адрес эл.почты уже занят другим пользователем");
        }
        User updatedUser = userRepository.find(userId);
        if (newUser.getName() != null && !newUser.getName().isBlank()) {
            updatedUser.setName(newUser.getName());
        }

        if (newUser.getEmail() != null && !newUser.getEmail().isBlank()) {
            updatedUser.setEmail(newUser.getEmail());
        }
        return updatedUser;
    }

    public void remove(Integer id) {
        if (!userRepository.containsById(id)) {
            throw new NotFoundException(USER_NOT_FOUND + id);
        }
        userRepository.remove(id);
    }

    private synchronized int getCounter() {
        return ++idCounter;
    }
}
