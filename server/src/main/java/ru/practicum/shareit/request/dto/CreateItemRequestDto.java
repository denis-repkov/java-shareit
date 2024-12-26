package ru.practicum.shareit.request.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemRequestDto {
    private LocalDateTime created;
    private String description;
    private Long requester;
}
