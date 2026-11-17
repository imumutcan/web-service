package com.umut.project.database.mywebservice.auth;

import com.umut.project.database.mywebservice.auth.dto.AuthDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<AuthDto.AuthResponse> auth(@RequestBody @Valid AuthDto.AuthRequest request)
    {
        return ResponseEntity.status(HttpStatus.OK).body(authService.authenticate(request));
    }
}
