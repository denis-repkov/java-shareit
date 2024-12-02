package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateItemDto {

    @Size(max = 100, message = "Название товара не должно превышать 100 символов")
    private String name;

    @Size(max = 500, message = "Описание товара не должно превышать 500 символов")
    private String description;

    private Boolean available;

}