package com.palladium46.odonatesdota.security._refreshToken.service;

import com.palladium46.odonatesdota.exceptions.RefreshTokenException;
import com.palladium46.odonatesdota.security._refreshToken.model.RefreshToken;
import com.palladium46.odonatesdota.security._refreshToken.repository.RefreshTokenRepository;
import com.palladium46.odonatesdota.user.model.User;
import com.palladium46.odonatesdota.user.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


@Service
public class RefreshTokenService {

    @Value("${jwt.refresh.expiration.ms}")
    private Long refreshTokenExpirationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserService userService;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserService userService){
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(String steamId64){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userService.findBySteamId(steamId64));
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpirationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if (token.getExpiryDate().compareTo(Instant.now())< 0){
            refreshTokenRepository.delete(token);
            throw new RefreshTokenException("Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Transactional
    public void deleteRefreshTokenFromUser(User user){
        refreshTokenRepository.deleteByUser(user);
    }

}
