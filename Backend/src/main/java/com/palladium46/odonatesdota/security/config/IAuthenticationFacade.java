package com.palladium46.odonatesdota.security.config;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
    Authentication getAuthentication() throws Exception;
}
