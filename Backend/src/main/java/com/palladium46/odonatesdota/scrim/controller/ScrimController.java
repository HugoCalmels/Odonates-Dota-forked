package com.palladium46.odonatesdota.scrim.controller;

import com.palladium46.odonatesdota.scrim.model.*;
import com.palladium46.odonatesdota.scrim.model.responses.ScrimDetailsResponse;
import com.palladium46.odonatesdota.scrim.service.ScrimService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scrim")
public class ScrimController {

    private final ScrimService scrimService;

    public ScrimController(ScrimService scrimService) {
        this.scrimService = scrimService;
    }
    @PostMapping()
    public ResponseEntity<ScrimDto> saveScrim(@RequestBody Scrim newScrim, @RequestParam("timezone") String timezone, @RequestParam String[] playerIds) throws Exception {
        ScrimDto createdScrim =  scrimService.saveScrim(newScrim, timezone, playerIds);
        return ResponseEntity.ok(createdScrim);
    }

    @GetMapping("/getAllScrims")
    public ResponseEntity<List<ScrimDto>> getAllScrims(@RequestParam("timezone") String timezone) throws Exception {
       return ResponseEntity.ok(scrimService.getAllActiveScrims(timezone));
    }

    @GetMapping("/scrim-details/{scrimId}")
    public ResponseEntity<ScrimDto> getFullScrim(@PathVariable Long scrimId, @RequestParam("timezone") String timezone) throws Exception {
        return scrimService.getFullScrim(scrimId, timezone);
    }

    @PostMapping("/join-scrim/{scrimId}")
    public ResponseEntity<ScrimDto> joinScrim(@PathVariable Long scrimId) throws Exception {
        return scrimService.joinScrim(scrimId);
    }

    @DeleteMapping("{scrimId}")
    public ResponseEntity<Void> deleteScrim(@PathVariable Long scrimId) throws Exception {
        return scrimService.deleteScrim(scrimId);
    }

    @PostMapping("/cancel/{scrimId}")
    public ResponseEntity<ScrimDto> cancelScrim(@PathVariable Long scrimId) throws Exception {
        return scrimService.cancelScrim(scrimId);
    }

    @GetMapping("/getScrimUserStatus/{scrimId}")
    public ResponseEntity<ScrimDetailsResponse> getScrimUserStatus(@PathVariable Long scrimId) throws Exception {
        return scrimService.getScrimUserStatus(scrimId);
    }

    @GetMapping("/getScrimHistory")
    public ResponseEntity<List<ScrimDto>> getScrimHistory(@RequestParam("timezone") String timezone) throws Exception {
        return ResponseEntity.ok(scrimService.getScrimHistory(timezone));
    }

    @PostMapping("/propose/{scrimId}")
    public ResponseEntity<ScrimDto> proposeScrim(@PathVariable Long scrimId, @RequestParam String[] playerIds, String timezone) throws Exception {
        return scrimService.proposeScrim(scrimId, playerIds, timezone);
    }

    @DeleteMapping("/propose/{scrimId}")
    public ResponseEntity<ScrimDto> cancelProposedScrim(@PathVariable Long scrimId, @RequestParam String timezone) throws Exception {
        return scrimService.cancelProposedScrim(scrimId, timezone);
    }

    @PostMapping("/accept/{proposalId}")
    public ResponseEntity<ScrimDto> acceptProposal(@PathVariable Long proposalId, @RequestParam String timezone) throws Exception {
        return scrimService.acceptScrimProposal(proposalId, timezone);
    }

    @PostMapping("/reject/{proposalId}")
    public ResponseEntity<ScrimDto> rejectProposal(@PathVariable Long proposalId, @RequestParam String timezone) throws Exception {
        return scrimService.rejectScrimProposal(proposalId, timezone);
    }

    @GetMapping("/getNumberScrimsPlayed/{teamId}")
    public ResponseEntity<Long> getNumberScrimsPlayedFromOneTeam(@PathVariable Long teamId) throws Exception {
        return scrimService.getNumberScrimsPlayedFromOneTeam(teamId);
    }

}
