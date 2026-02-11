package com.palladium46.odonatesdota.steamAuth.controller;

import com.palladium46.odonatesdota.steamAuth.service.SteamAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/steamAuth")
public class SteamAuthController {

    private final SteamAuthService steamAuthService;

    public SteamAuthController(SteamAuthService steamAuthService) {
        this.steamAuthService = steamAuthService;
    }

    @GetMapping("/process-steam-login")
    public ResponseEntity<String> processSteamLogin(@RequestParam Map<String, String> params, String currentRoute, HttpServletResponse httpServletResponse) throws Exception {
        return steamAuthService.processLogin(params,currentRoute, httpServletResponse);
    }

}
