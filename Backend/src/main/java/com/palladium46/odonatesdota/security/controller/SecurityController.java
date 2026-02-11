package com.palladium46.odonatesdota.security.controller;

import com.palladium46.odonatesdota.security.service.SecurityService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
public class SecurityController {

    private final SecurityService securityService;

    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @GetMapping("/refreshToken")
    public ResponseEntity<Void> refreshToken(@CookieValue(name = "refreshToken", required = false) String token, HttpServletResponse response){
        return securityService.refreshToken(token, response);
    }

    @GetMapping("/logout")
    public  ResponseEntity<Void> logout(HttpServletResponse response) {
        return securityService.logout(response);
    }

    @GetMapping("/isValidRefreshToken")
    public boolean isValidRefreshToken(@CookieValue(name = "refreshToken") String refreshToken){
        return securityService.isValidRefreshToken(refreshToken);
    }

    // ~~*~~*~~*~~* TESTING ENDPOINTS ~~*~~*~~*~~*
    @GetMapping("/only_user_access")
    public String onlyUserAccess(){
        return "Only users has access to this endpoint";
    }

    @GetMapping("/only_admin_access")
    public String onlyAdminAccess(){
        return "Only admins has access to this endpoint";
    }

    @GetMapping("/public")
    public String publicEndpoint(){
        return "Public endpoint";
    }

    @GetMapping("/protected")
    public String protectedEndpoint(){
        return "Protected endpoint";
    }
}
