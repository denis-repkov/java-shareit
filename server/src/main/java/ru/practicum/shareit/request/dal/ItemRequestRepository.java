package ru.practicum.shareit.request.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.Optional;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    Optional<ItemRequest> findById(Long id);

    List<ItemRequest> findByRequester(Long id);

    ItemRequest save(ItemRequest request);

    void deleteById(Long id);

}
