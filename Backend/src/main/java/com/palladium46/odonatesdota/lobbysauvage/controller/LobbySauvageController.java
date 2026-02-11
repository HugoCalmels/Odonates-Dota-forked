package com.palladium46.odonatesdota.lobbysauvage.controller;

import com.palladium46.odonatesdota.lobbysauvage.model.LobbySauvage;
import com.palladium46.odonatesdota.lobbysauvage.model.LobbySauvageDto;
import com.palladium46.odonatesdota.lobbysauvage.service.LobbySauvageService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lobby-sauvage")
public class LobbySauvageController {

    private final LobbySauvageService lobbySauvageService;

    public LobbySauvageController(LobbySauvageService lobbySauvageService) {
        this.lobbySauvageService = lobbySauvageService;
    }

    @PostMapping()
    public ResponseEntity<LobbySauvageDto> createLobby() throws Exception {
        return ResponseEntity.ok(this.lobbySauvageService.createLobby().toDto());
    }

    @PostMapping(value = "/leave")
    public ResponseEntity<Boolean> leaveLobby(HttpServletRequest request) throws Exception {
        boolean result = this.lobbySauvageService.leave();
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/join/{id}")
    public ResponseEntity<LobbySauvageDto> joinLobby(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(this.lobbySauvageService.joinLobby(id).toDto());
    }

    @GetMapping()
    public ResponseEntity<List<LobbySauvageDto>> getLobbys() throws Exception {
        return ResponseEntity.ok(this.lobbySauvageService.getAllLobby().stream().map(LobbySauvage::toDto).toList());
    }

    @GetMapping(value = "/current")
    public ResponseEntity<LobbySauvageDto> getUserCurrentLobby() throws Exception {
        LobbySauvage lobbySauvage = this.lobbySauvageService.getUserCurrentLobby();
        if (lobbySauvage != null)
            return ResponseEntity.ok(this.lobbySauvageService.getUserCurrentLobby().toDto());
        else
            return ResponseEntity.ok(null);
    }
}
