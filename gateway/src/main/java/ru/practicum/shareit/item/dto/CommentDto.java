package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private Long item;
    private String authorName;

    @NotBlank(message = "Текст комментария не должен быть пустым")
    @Size(max = 500, message = "Текст комментария не должен превышать 500 символов")
    private String text;

    private LocalDateTime created;
}
