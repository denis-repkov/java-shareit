package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemRequestDto {

    @NotNull
    @Future
    private LocalDateTime created;

    @NotBlank(message = "Описание запроса не может быть пустым")
    @Size(max = 500, message = "Описание запроса не должно превышать 500 символов")
    private String description;

    @NotNull
    private Long requester;

}
