package ru.practicum.shareit.user.dal;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private int idCounter = 0;

    private final Map<Integer, User> userStorage = new HashMap<>();

    @Override
    public Boolean containsById(Integer id) {
        return userStorage.containsKey(id);
    }

    @Override
    public Boolean containsByEmail(String email) {
        return userStorage.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public User find(Integer id) {
        return userStorage.get(id);
    }

    @Override
    public User add(User newUser) {
        newUser.setId(getNextId());
        userStorage.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User update(User newUser) {
        userStorage.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User remove(Integer id) {
        return userStorage.remove(id);
    }

    private int getNextId() {
        return ++idCounter;
    }
}
