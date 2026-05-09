package ru.optiroute.demo.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

record RegisterRequest(
        @NotBlank(message = "Введите имя.")
        @Size(max = 50, message = "Имя должно быть короче 50 символов.")
        String name,
        @NotBlank(message = "Введите почту.")
        @Email(message = "Почта должна быть корректной.")
        String email,
        @NotBlank(message = "Введите пароль.")
        @Size(min = 8, max = 24, message = "Пароль должен содержать от 8 до 24 символов.")
        String password
) {
}

record LoginRequest(
        @NotBlank(message = "Введите почту.")
        @Email(message = "Почта должна быть корректной.")
        String email,
        @NotBlank(message = "Введите пароль.")
        String password
) {
}

