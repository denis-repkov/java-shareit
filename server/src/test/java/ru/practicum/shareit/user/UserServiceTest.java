package ru.practicum.shareit.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceTest {

    @Autowired
    private final EntityManager em;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Test
    void findByIdSuccess() {
        CreateUserDto userDto = CreateUserDto.builder()
                .name("Name")
                .email("aaa@b.ru")
                .build();

        UserDto createdUser = userService.save(userDto);
        UserDto foundUser = userService.findById(createdUser.getId());

        assertNotNull(foundUser);
        assertEquals(createdUser.getId(), foundUser.getId());
        assertEquals("Name", foundUser.getName());
        assertEquals("aaa@b.ru", foundUser.getEmail());
    }

    @Test
    void testSaveUser() {
        CreateUserDto userDto = CreateUserDto.builder()
                .name("Name")
                .email("aaa@b.ru")
                .build();

        UserDto createdUser = userService.save(userDto);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.id = :id", User.class);
        User user = query.setParameter("id", createdUser.getId())
                .getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void updateSuccess() {
        CreateUserDto userDto = CreateUserDto.builder()
                .name("Name")
                .email("aaa@b.ru")
                .build();

        UserDto createdUser = userService.save(userDto);

        UpdateUserDto updateUserDto = UpdateUserDto.builder()
                .name("Updated Name")
                .email("updated.email@example.com")
                .build();

        UserDto updatedUser = userService.update(createdUser.getId(), updateUserDto);

        assertEquals("Updated Name", updatedUser.getName());
        assertEquals("updated.email@example.com", updatedUser.getEmail());
    }

    @Test
    void deleteById_shouldRemoveUser_whenUserExists() {
        CreateUserDto userDto = CreateUserDto.builder()
                .name("Name")
                .email("aaa@b.ru")
                .build();

        UserDto userToDelete = userService.save(userDto);

        userService.deleteById(userToDelete.getId());

        Optional<User> deletedUser = userRepository.findById(userToDelete.getId());
        assertTrue(deletedUser.isEmpty());
    }

    @Test
    @DisplayName("Несуществующий пользователь не найден")
    void findByIdError() {
        assertThrows(NotFoundException.class, () -> userService.findById(999L));
    }
}