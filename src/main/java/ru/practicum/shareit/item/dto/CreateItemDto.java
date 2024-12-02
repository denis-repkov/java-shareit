package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateItemDto {

    @NotBlank(message = "Название товара не должно быть пустым")
    @Size(max = 100, message = "Название товара не должно превышать 100 символов")
    private String name;

    @NotBlank(message = "Описание товара не должно быть пустым")
    @Size(max = 500, message = "Описание товара не должно превышать 500 символов")
    private String description;

    @NotNull
    private Boolean available;

}
