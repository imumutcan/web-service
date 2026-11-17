package com.umut.project.database.mywebservice.token;

import com.umut.project.database.mywebservice.exception.InvalidRefreshTokenException;
import com.umut.project.database.mywebservice.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-token-expiration-ms}")
    private Long refreshTokenDurationMs;

    @Transactional
    public RefreshToken generateRefreshToken(User user)
    {
        refreshTokenRepository.deleteExpiredByUserId(user.getId(), Instant.now());

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional(noRollbackFor = InvalidRefreshTokenException.class)
    public RefreshToken verify(String token)
    {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(InvalidRefreshTokenException::new);

        if(refreshToken.getExpiryDate().isBefore(Instant.now()))
        {
            refreshTokenRepository.delete(refreshToken);
            throw new InvalidRefreshTokenException();
        }
        return refreshToken;
    }

    @Transactional
    public int deleteByUserId(Long userId)
    {
        return refreshTokenRepository.deleteByUserId(userId);
    }

    @Transactional
    public int deleteByToken(String token)
    {
        return refreshTokenRepository.deleteByToken(token);
    }
}
