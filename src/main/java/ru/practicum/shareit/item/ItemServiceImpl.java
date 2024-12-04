package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AuthentificationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dal.UserRepository;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.shareit.exception.ErrorMessages.ITEM_NOT_FOUND;
import static ru.practicum.shareit.exception.ErrorMessages.USER_NOT_FOUND;


@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final ItemMapper itemMapper;

    @Override
    public Set<ItemDto> getByUser(Integer userId) {
        if (!userRepository.containsById(userId)) {
            throw new NotFoundException(USER_NOT_FOUND + userId);
        }
        return itemRepository.findByUserId(userId).stream()
                .map(itemMapper::map)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Item> findAll() {
        return itemRepository.findAll();
    }

    @Override
    public ItemDto get(Integer itemId) {
        if (!itemRepository.contains(itemId)) {
            throw new NotFoundException(ITEM_NOT_FOUND + itemId);
        }
        return itemMapper.map(itemRepository.find(itemId));
    }

    @Override
    public ItemDto add(CreateItemDto itemDto, Integer userId) {
        Item item = itemMapper.map(itemDto, userId);
        Integer ownerId = item.getOwner();
        if (!userRepository.containsById(ownerId)) {
            throw new NotFoundException(USER_NOT_FOUND + ownerId);
        }
        return itemMapper.map(itemRepository.add(item));
    }

    @Override
    public ItemDto update(Integer requestor, Integer itemId, UpdateItemDto newItem) {
        Item item = itemMapper.map(newItem, itemId);
        if (!itemRepository.contains(itemId)) {
            throw new NotFoundException(ITEM_NOT_FOUND + itemId);
        }
        Item updatedItem = itemRepository.find(itemId);
        if (!Objects.equals(updatedItem.getOwner(), requestor)) {
            throw new AuthentificationException("Данные по товару может обновлять только его владелец");
        }
        return itemMapper.map(itemRepository.update(item));
    }

    @Override
    public void remove(Integer itemId) {
        Item item = itemRepository.remove(itemId);
        if (item == null) {
            throw new NotFoundException(ITEM_NOT_FOUND + itemId);
        }
    }

    @Override
    public Set<ItemDto> search(String text) {
        if (text.isBlank()) {
            return Collections.emptySet();
        }
        return itemRepository.search(text).stream()
                .map(itemMapper::map)
                .collect(Collectors.toSet());
    }
}
