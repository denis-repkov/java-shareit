package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {

    @NotBlank(message = "Описание запроса не может быть пустым")
    @Size(max = 500, message = "Описание запроса не должно превышать 500 символов")
    private String description;

}
