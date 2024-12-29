package ru.practicum.shareit.request;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.request.dal.ItemRequestRepository;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceTest {

    @Autowired
    private final EntityManager em;
    UserDto user1;
    UserDto user2;
    UserDto user3;
    TypedQuery<ItemRequest> queryFindById;
    @Autowired
    private ItemRequestRepository itemRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemRequestService itemRequestService;

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

        queryFindById = em.createQuery("Select r from ItemRequest r where r.id = :id", ItemRequest.class);
    }

    @Test
    void create() {
        CreateItemRequestDto newRequest = CreateItemRequestDto.builder()
                .description("Description1")
                .build();

        ItemRequestDto created = itemRequestService.save(user1.getId(), newRequest);
        ItemRequest request = queryFindById.setParameter("id", created.getId()).getSingleResult();

        assertNotNull(request);
        assertNotNull(request.getId());
        assertEquals(created.getDescription(), request.getDescription());
        assertEquals(created.getRequester(), user1.getId());
        assertEquals(created.getCreated(), request.getCreated());
    }

    @Test
    void findById() {
        CreateItemRequestDto newRequest = CreateItemRequestDto.builder()
                .description("Description1")
                .build();

        ItemRequestDto created = itemRequestService.save(user1.getId(), newRequest);
        ItemRequestDto found = itemRequestService.findById(user1.getId(), created.getId());
        assertNotNull(found);
        assertEquals(created.getDescription(), found.getDescription());
        assertEquals(created.getRequester(), user1.getId());
        assertEquals(created.getCreated(), found.getCreated());
    }

    @Test
    void findByRequester() {
        CreateItemRequestDto newRequest1 = CreateItemRequestDto.builder()
                .description("Description1")
                .build();
        CreateItemRequestDto newRequest2 = CreateItemRequestDto.builder()
                .description("Description1")
                .build();

        ItemRequestDto created1 = itemRequestService.save(user1.getId(), newRequest1);
        ItemRequestDto created2 = itemRequestService.save(user2.getId(), newRequest2);
        List<ItemRequestDto> items = itemRequestService.findByRequester(user1.getId());
        assertNotNull(items);
        assertEquals(1, items.size());
        ItemRequestDto request = items.get(0);
        assertEquals(created1.getDescription(), request.getDescription());
        assertEquals(created1.getRequester(), user1.getId());
        assertEquals(created1.getCreated(), request.getCreated());
    }

    @Test
    void findAll() {
        CreateItemRequestDto newRequest1 = CreateItemRequestDto.builder()
                .description("Description1")
                .build();
        CreateItemRequestDto newRequest2 = CreateItemRequestDto.builder()
                .description("Description2")
                .build();

        ItemRequestDto created1 = itemRequestService.save(user1.getId(), newRequest1);
        ItemRequestDto created2 = itemRequestService.save(user2.getId(), newRequest2);

        List<ItemRequestDto> items = itemRequestService.findAll(user3.getId());

        assertNotNull(items);
        assertEquals(2, items.size());

        ItemRequestDto request1 = items.get(0);
        ItemRequestDto request2 = items.get(1);

        assertEquals(created1.getDescription(), request1.getDescription());
        assertEquals(created1.getRequester(), user1.getId());
        assertEquals(created1.getCreated(), request1.getCreated());
        assertEquals(created2.getDescription(), request2.getDescription());
        assertEquals(created2.getRequester(), user2.getId());
        assertEquals(created2.getCreated(), request2.getCreated());
    }

    @Test
    void deleteById() {
        CreateItemRequestDto newRequest1 = CreateItemRequestDto.builder()
                .description("Description1")
                .build();
        CreateItemRequestDto newRequest2 = CreateItemRequestDto.builder()
                .description("Description2")
                .build();

        ItemRequestDto created1 = itemRequestService.save(user1.getId(), newRequest1);
        ItemRequestDto created2 = itemRequestService.save(user2.getId(), newRequest2);

        assertEquals(2, itemRequestService.findAll(user3.getId()).size());
        itemRequestService.delete(created1.getId());
        assertEquals(1, itemRequestService.findAll(user3.getId()).size());

        NotFoundException n =
                assertThrows(NotFoundException.class, () -> itemRequestService.findById(user3.getId(), created1.getId()));
        assertEquals("Не найден запрос с ID: " + created1.getId(), n.getMessage());
    }
}