package com.palladium46.odonatesdota.security.config;

import com.palladium46.odonatesdota.exceptions.RefreshTokenException;
import com.palladium46.odonatesdota.security.service.UserInfoDetails;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade implements IAuthenticationFacade {

    @Override
    public Authentication getAuthentication() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof UserInfoDetails) {
                    return authentication;
                }
            } else {
                throw new RefreshTokenException("Refresh token expired");
            }
        return null;
    }
}
