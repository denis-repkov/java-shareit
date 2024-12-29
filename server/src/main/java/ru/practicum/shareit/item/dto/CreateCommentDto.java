package ru.practicum.shareit.item.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentDto {
    private String text;
}
