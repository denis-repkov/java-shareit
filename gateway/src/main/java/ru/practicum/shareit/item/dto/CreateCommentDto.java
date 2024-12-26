package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentDto {

    @NotBlank(message = "Текст комментария не должен быть пустым")
    @Size(max = 500, message = "Текст комментария не должен превышать 500 символов")
    private String text;
}
