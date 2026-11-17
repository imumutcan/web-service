package com.umut.project.database.mywebservice.user.dto;

import com.umut.project.database.mywebservice.user.validation.UniqueEmail;
import com.umut.project.database.mywebservice.user.validation.UniqueUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public final class UserDto {

    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$";

    private UserDto() {}

    public record UserRegisterRequest(

            @NotBlank(message = "{validation.username.notblank}")
            @Size(min = 3, max = 20, message = "{validation.username.size}")
            @UniqueUsername
            String username,
            @NotBlank(message = "{validation.password.notblank}")
            @Size(min = 8, max = 255, message = "{validation.password.size}")
            @Pattern(regexp = PASSWORD_REGEX, message = "{validation.password.pattern}")
            String password,
            @NotBlank(message ="{validation.email.notblank}")
            @Email(message = "{validation.email.invalid}")
            @UniqueEmail
            String email
    ){}

    public record UserUpdateRequest(
            @NotBlank(message = "{validation.username.notblank}")
            @Size(min = 3, max = 20, message = "{validation.username.size}")
            String username
    ){}

    public record PasswordUpdateRequest(
            @NotBlank(message = "{validation.password.notblank}")
            String currentPassword,
            @NotBlank(message = "{validation.password.notblank}")
            @Size(min = 8, max = 255, message = "{validation.password.size}")
            @Pattern(regexp = PASSWORD_REGEX, message = "{validation.password.pattern}")
            String newPassword
    ){}

    public record MessageResponse(
      String message
    ){}

    public record UserDetailsResponse(
            Long id,
            String username,
            String email,
            String image
    ){}
}
