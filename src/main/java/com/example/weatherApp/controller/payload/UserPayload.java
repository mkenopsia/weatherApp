package com.example.weatherApp.controller.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserPayload(
        @NotBlank(message = "{user.errors.name_is_blank}")
        @Size(min = 2, max = 20, message = "{user.errors.errors.name_size_is_invalid}")
        String name,
        @NotNull(message = "{user.errors.password_is_blank}")
        @Size(min = 3, max = 20, message = "{user.errors.errors.password_size_is_invalid}")
        String password,
        String role) {
}
