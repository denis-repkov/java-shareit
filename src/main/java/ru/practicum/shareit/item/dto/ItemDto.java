package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemDto {
    private Long id;
    private Long owner;
    private String name;
    private String description;
    private Boolean available;
    private Long request;
    private LocalDateTime lastBooking;
    private LocalDateTime nextBooking;
    private List<String> comments;
}
