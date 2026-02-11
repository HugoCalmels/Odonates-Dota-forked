package com.palladium46.odonatesdota.security.service;

import com.palladium46.odonatesdota.exceptions.RefreshTokenException;
import com.palladium46.odonatesdota.security._refreshToken.model.RefreshToken;
import com.palladium46.odonatesdota.security._refreshToken.service.RefreshTokenService;
import com.palladium46.odonatesdota.security.config.AuthenticationFacade;
import com.palladium46.odonatesdota.user.model.User;
import com.palladium46.odonatesdota.user.service.UserService;
import com.palladium46.odonatesdota.utils.CookiesUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityService {

    @Value("${jwt.expiration.ms}")
    private int jwtExpiration;

    private static JwtService jwtService;

    private static RefreshTokenService refreshTokenService;

    private final AuthenticationFacade authenticationFacade;

    private final UserService userService;

    public SecurityService(JwtService jwtService, RefreshTokenService refreshTokenService, AuthenticationFacade authenticationFacade, UserService userService) {
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.authenticationFacade = authenticationFacade;
        this.userService = userService;

    }

    public ResponseEntity<Void> refreshToken(String refreshToken, HttpServletResponse response) {
        try {
            refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        String token = jwtService.generateToken(user.getSteamId64bits());
                        CookiesUtils.removeCookie(response, "token");
                        CookiesUtils.addToCookie(response, "token", token, jwtExpiration);

                        return ResponseEntity.noContent().build();
                    })
                    .orElseThrow(() -> new RefreshTokenException("Refresh token is not in database!"));

            return null;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<Void> logout(HttpServletResponse response) {
        CookiesUtils.removeCookie(response, "token");
        CookiesUtils.removeCookie(response, "refreshToken");
        return ResponseEntity.noContent().build();
    }

    public boolean isValidRefreshToken(String token){
        Optional<RefreshToken> optionalRefreshToken = refreshTokenService.findByToken(token);
        return optionalRefreshToken.isPresent();
    }

    public User getCurrentUser() throws Exception {
        UserInfoDetails userDetails = (UserInfoDetails) authenticationFacade.getAuthentication().getPrincipal();
        return userDetails.getFullUser();
    }

    public User getFullCurrentUser() throws Exception {
        return userService.findBySteamId(this.getCurrentUser().getSteamId64bits());
    }

}
