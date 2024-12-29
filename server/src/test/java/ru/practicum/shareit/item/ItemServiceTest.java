package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.exception.AuthentificationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.WrongArgumentsException;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceTest {

    @Autowired
    private final EntityManager em;
    UserDto user1;
    UserDto user2;
    UserDto user3;
    ItemDto item1;
    ItemDto item2;
    ItemDto item3;
    TypedQuery<Item> queryFindById;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;
    @Autowired
    private ItemService itemService;

    @BeforeEach
    void setUp() {
        CreateUserDto userDto1 = CreateUserDto.builder()
                .name("Name1")
                .email("aaa1@b.ru")
                .build();

        CreateUserDto userDto2 = CreateUserDto.builder()
                .name("Name2")
                .email("aaa2@b.ru")
                .build();

        CreateUserDto userDto3 = CreateUserDto.builder()
                .name("Name3")
                .email("aaa3@b.ru")
                .build();

        user1 = userService.save(userDto1);
        user2 = userService.save(userDto2);
        user3 = userService.save(userDto3);

        CreateItemDto itemDto1 = CreateItemDto.builder()
                .name("Item1")
                .description("Description1")
                .available(true)
                .build();

        CreateItemDto itemDto2 = CreateItemDto.builder()
                .name("Item2")
                .description("Description2")
                .available(true)
                .build();

        CreateItemDto itemDto3 = CreateItemDto.builder()
                .name("Item3")
                .description("Description3")
                .available(true)
                .build();

        item1 = itemService.save(user1.getId(), itemDto1);
        item2 = itemService.save(user1.getId(), itemDto2);
        item3 = itemService.save(user2.getId(), itemDto3);

        queryFindById = em.createQuery("Select i from Item i where i.id = :id", Item.class);
    }

    @Test
    void create() {
        CreateItemDto toCreate = CreateItemDto.builder()
                .name("Item1")
                .description("Description1")
                .available(true)
                .build();

        ItemDto itemDto = itemService.save(user1.getId(), toCreate);
        Item item = queryFindById.setParameter("id", itemDto.getId()).getSingleResult();
        assertNotNull(item);
        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getAvailable(), item.getAvailable());
        assertEquals(itemDto.getDescription(), item.getDescription());
    }

    @Test
    void createByNonExistingUser() {
        CreateItemDto toCreate = CreateItemDto.builder()
                .name("Item1")
                .description("Some description")
                .available(true)
                .build();

        NotFoundException n =
                assertThrows(NotFoundException.class, () -> itemService.save(999L, toCreate));
        assertEquals("Не найден пользователь с ID: " + 999L, n.getMessage());
    }

    @Test
    void update() {
        CreateItemDto itemDto = CreateItemDto.builder()
                .name("Item1")
                .description("Description1")
                .available(true)
                .build();

        UpdateItemDto toUpdate = UpdateItemDto.builder()
                .name("new Name")
                .description("new Description")
                .available(true)
                .build();

        ItemDto createdItemDto = itemService.save(user1.getId(), itemDto);
        ItemDto updated = itemService.save(user1.getId(), createdItemDto.getId(), toUpdate);
        Item item = queryFindById.setParameter("id", updated.getId()).getSingleResult();
        assertNotNull(createdItemDto);
        assertEquals(createdItemDto.getId(), item.getId());
        assertEquals(updated.getName(), item.getName());
        assertEquals(updated.getAvailable(), item.getAvailable());
        assertEquals(updated.getDescription(), item.getDescription());
    }

    @Test
    void updateNotByOwner() {
        CreateItemDto itemDto = CreateItemDto.builder()
                .name("Item1")
                .description("Description1")
                .available(true)
                .build();

        UpdateItemDto toUpdate = UpdateItemDto.builder()
                .name("new Name")
                .description("new Description")
                .available(true)
                .build();

        ItemDto createdItemDto = itemService.save(user1.getId(), itemDto);
        AuthentificationException a =
                assertThrows(AuthentificationException.class, () -> itemService.save(user2.getId(), createdItemDto.getId(), toUpdate));
        assertEquals("Данные по товару может обновлять только его владелец", a.getMessage());
    }

    @Test
    void updateByNotExistingUser() {
        CreateItemDto itemDto = CreateItemDto.builder()
                .name("Item1")
                .description("Description1")
                .available(true)
                .build();

        UpdateItemDto toUpdate = UpdateItemDto.builder()
                .name("new Name")
                .description("new Description")
                .available(true)
                .build();

        ItemDto createdItemDto = itemService.save(user1.getId(), itemDto);
        AuthentificationException a =
                assertThrows(AuthentificationException.class, () -> itemService.save(999L, createdItemDto.getId(), toUpdate));
        assertEquals("Данные по товару может обновлять только его владелец", a.getMessage());
    }

    @Test
    void findByOwner() {
        List<ItemDto> foundItems = itemService.findByOwner(user1.getId());
        List<ItemDto> foundItemsEmpty = itemService.findByOwner(user3.getId());

        assertNotNull(foundItems);
        assertEquals(0, foundItemsEmpty.size());
        assertEquals(2, foundItems.size());
        ItemDto foundItem1 = foundItems.get(0);
        ItemDto foundItem2 = foundItems.get(1);
        assertEquals(item1.getName(), foundItem1.getName());
        assertEquals(item1.getAvailable(), foundItem1.getAvailable());
        assertEquals(item1.getDescription(), foundItem1.getDescription());
        assertEquals(item2.getName(), foundItem2.getName());
        assertEquals(item2.getAvailable(), foundItem2.getAvailable());
        assertEquals(item2.getDescription(), foundItem2.getDescription());
    }

    @Test
    void findAll() {
        List<ItemDto> foundItems = itemService.findAll(user1.getId());
        List<ItemDto> foundItemsAll = itemService.findAll(null);

        assertNotNull(foundItems);
        assertEquals(2, foundItems.size());
        assertEquals(3, foundItemsAll.size());
        ItemDto foundItem1 = foundItemsAll.get(0);
        ItemDto foundItem2 = foundItemsAll.get(1);
        ItemDto foundItem3 = foundItemsAll.get(2);
        assertEquals(item1.getName(), foundItem1.getName());
        assertEquals(item1.getAvailable(), foundItem1.getAvailable());
        assertEquals(item1.getDescription(), foundItem1.getDescription());
        assertEquals(item2.getName(), foundItem2.getName());
        assertEquals(item2.getAvailable(), foundItem2.getAvailable());
        assertEquals(item2.getDescription(), foundItem2.getDescription());
        assertEquals(item3.getName(), foundItem3.getName());
        assertEquals(item3.getAvailable(), foundItem3.getAvailable());
        assertEquals(item3.getDescription(), foundItem3.getDescription());
    }

    @Test
    void findById() {
        ItemDto foundItem = itemService.findById(item1.getId());

        assertNotNull(foundItem);
        assertEquals(item1.getName(), foundItem.getName());
        assertEquals(item1.getAvailable(), foundItem.getAvailable());
        assertEquals(item1.getDescription(), foundItem.getDescription());
        NotFoundException n =
                assertThrows(NotFoundException.class, () -> itemService.findById(999L));
        assertEquals("Не найден товар с ID: " + 999L, n.getMessage());
    }

    @Test
    void deleteById() {
        CreateItemDto itemDto = CreateItemDto.builder()
                .name("Item1")
                .description("Description1")
                .available(true)
                .build();

        ItemDto createdItemDto = itemService.save(user1.getId(), itemDto);
        assertNotNull(itemService.findById(createdItemDto.getId()));

        NotFoundException f =
                assertThrows(NotFoundException.class, () -> itemService.deleteById(999L, createdItemDto.getId()));
        assertEquals("Не найден пользователь с ID: " + 999L, f.getMessage());

        itemService.deleteById(user1.getId(), createdItemDto.getId());
        NotFoundException n =
                assertThrows(NotFoundException.class, () -> itemService.findById(createdItemDto.getId()));
        assertEquals("Не найден товар с ID: " + createdItemDto.getId(), n.getMessage());
    }

    @Test
    void deleteNotExistingItem() {
        NotFoundException n =
                assertThrows(NotFoundException.class, () -> itemService.deleteById(user1.getId(), 999L));
        assertEquals("Не найден товар с ID: " + 999L, n.getMessage());
    }

    @Test
    void searchBlank() {
        assertEquals(Collections.emptyList(), itemService.search(user1.getId(), " "));
    }

    @Test
    void searchNotUser() {
        NotFoundException n =
                assertThrows(NotFoundException.class, () -> itemService.search(999L, "Test"));
        assertEquals("Не найден пользователь с ID: " + 999L, n.getMessage());
    }

    @Test
    void searchSuccess() {
        CreateItemDto itemMissing = CreateItemDto.builder()
                .name("Item3")
                .description("Another")
                .available(true)
                .build();
        ItemDto itemDto = itemService.save(user3.getId(), itemMissing);

        List<ItemDto> searchResult = itemService.search(user3.getId(), "Another");

        assertEquals(1, searchResult.size());
    }

    @Test
    void commentItem() {
        CreateBookingDto toCreate = CreateBookingDto.builder()
                .itemId(item1.getId())
                .start(LocalDateTime.now().minusDays(15))
                .end(LocalDateTime.now().minusDays(7))
                .build();

        BookingDto bookingDto = bookingService.save(toCreate, user1.getId());
        bookingService.processBooking(item1.getOwner(), bookingDto.getId(), true);

        CreateCommentDto commentDto = CreateCommentDto.builder()
                .text("Good comment")
                .build();

        CommentDto createdComment = itemService.commentItem(user1.getId(), item1.getId(), commentDto);

        assertEquals(item1.getId(), createdComment.getItem());
        assertEquals(user1.getName(), createdComment.getAuthorName());
        assertEquals("Good comment", createdComment.getText());
    }

    @Test
    void commentNotExistingItem() {
        CreateBookingDto toCreate = CreateBookingDto.builder()
                .itemId(item1.getId())
                .start(LocalDateTime.now().minusDays(15))
                .end(LocalDateTime.now().minusDays(7))
                .build();

        BookingDto bookingDto = bookingService.save(toCreate, user1.getId());
        bookingService.processBooking(item1.getOwner(), bookingDto.getId(), true);

        CreateCommentDto commentDto = CreateCommentDto.builder()
                .text("Good comment")
                .build();
        NotFoundException n =
                assertThrows(NotFoundException.class, () -> itemService.commentItem(user1.getId(), 999L, commentDto));
        assertEquals("Не найден товар с ID: " + 999L, n.getMessage());
    }

    @Test
    void commentRejectedBooking() {
        CreateBookingDto toCreate = CreateBookingDto.builder()
                .itemId(item1.getId())
                .start(LocalDateTime.now().minusDays(15))
                .end(LocalDateTime.now().minusDays(7))
                .build();

        BookingDto bookingDto = bookingService.save(toCreate, user1.getId());
        bookingService.processBooking(item1.getOwner(), bookingDto.getId(), false);

        CreateCommentDto commentDto = CreateCommentDto.builder()
                .text("Good comment")
                .build();
        WrongArgumentsException w =
                assertThrows(WrongArgumentsException.class, () -> itemService.commentItem(user1.getId(), item1.getId(), commentDto));
        assertEquals("Оставить комментарий может только тот, кто забронировал этот товар", w.getMessage());
    }
}