package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {

    @NotBlank(message = "Имя пользователя не должно быть пустым")
    @Size(max = 100, message = "Имя пользователя не должно превышать 100 символов")
    private String name;

    @NotBlank(message = "Эл.почта пользователя не должна быть пустой")
    @Email(message = "Эл.почта некорректна")
    private String email;

}
