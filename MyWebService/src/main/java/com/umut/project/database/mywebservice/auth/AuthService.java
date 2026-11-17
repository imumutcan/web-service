package com.umut.project.database.mywebservice.auth;

import com.umut.project.database.mywebservice.auth.dto.AuthDto;
import com.umut.project.database.mywebservice.exception.AuthenticationException;
import com.umut.project.database.mywebservice.security.CurrentUser;
import com.umut.project.database.mywebservice.token.JwtService;
import com.umut.project.database.mywebservice.token.RefreshToken;
import com.umut.project.database.mywebservice.token.RefreshTokenService;
import com.umut.project.database.mywebservice.user.User;
import com.umut.project.database.mywebservice.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthDto.AuthResponse authenticate(AuthDto.AuthRequest request)
    {
        User user = userRepository.findByEmail(request.email()).orElse(null);
        if(user == null)
        {
            throw new AuthenticationException();
        }
        if(!user.isActivate())
        {
            throw new com.umut.project.database.mywebservice.exception.UserNotActivatedException();
        }
        if(user == null)
        {
            throw new AuthenticationException();
        }
        if(!passwordEncoder.matches(request.password(), user.getPassword()))
        {
            throw new AuthenticationException();
        }
        
        String jwtToken = jwtService.generateToken(new CurrentUser(user));
        RefreshToken refreshTokenObj = refreshTokenService.generateRefreshToken(user);
        
        AuthDto.Token tokenDto = new AuthDto.Token("Bearer", jwtToken, refreshTokenObj.getToken());
        
        return toAuthResponse(user, tokenDto);
    }


    public AuthDto.AuthResponse toAuthResponse(User user, AuthDto.Token token)
    {
        return new AuthDto.AuthResponse(user.getId(),user.getUsername(), user.getEmail(), user.getImage(), token, "Login is success!");
    }
}
