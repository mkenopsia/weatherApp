package com.example.weatherApp.controller.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserPayload(
        @NotBlank(message = "{weatherApp.users.create.errors.name_is_blank}")
        @Size(min = 2, max = 20, message = "{weatherApp.users.create.errors.name_size_is_invalid}")
        String name,
        @NotNull(message = "{weatherApp.users.create.errors.password_is_blank}")
        String password,
        String role) {
}
