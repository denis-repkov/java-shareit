package ru.practicum.shareit.user.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {
    private String name;
    private String email;
}
