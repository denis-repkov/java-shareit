package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingDto {
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}