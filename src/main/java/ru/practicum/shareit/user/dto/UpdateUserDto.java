package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserDto {

    @Size(max = 100, message = "Имя пользователя не должно превышать 100 символов")
    private String name;

    @Email(message = "Эл.почта некорректна")
    private String email;

}
