package com.umut.project.database.mywebservice.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public final class AuthDto {

   public record AuthRequest(
           @Email(message = "{validation.email.invalid}")
           @NotBlank(message ="{validation.email.notblank}")
           String email,
           @NotBlank(message = "{validation.password.notblank}")
           String password
   ){}

    public record Token(
            String prefix,
            String token,
            String refreshToken
    ){}

    public record AuthResponse(
            Long id,
            String username,
            String email,
            String image,
            Token token,
            String message
    ){}
}
