package ru.practicum.shareit.item.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemDto {
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}
