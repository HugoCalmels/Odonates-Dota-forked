package com.palladium46.odonatesdota.team.controller;

import com.palladium46.odonatesdota.scrim.model.ScrimDto;
import com.palladium46.odonatesdota.team.model.*;
import com.palladium46.odonatesdota.team.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/team")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping()
    public ResponseEntity<TeamDto> saveTeam(
                         @RequestParam("name") String name,
                         @RequestParam("password") String password,
                         @RequestParam(value= "logo", required = false) MultipartFile logoFile
                         ) throws Exception {
        Team newTeam = teamService.saveTeam(name, password, logoFile);
        return ResponseEntity.ok(TeamMapper.toDto(newTeam));
    }

    @PutMapping()
    public ResponseEntity<TeamDto> editTeam(@RequestParam("name") String name,
                                            @RequestParam("password") String password,
                                            @RequestParam(value = "logo", required = false) MultipartFile logoFile
    ) throws Exception {
        Team editedTeam = teamService.editTeam(name, password, logoFile);
        return ResponseEntity.ok(TeamMapper.toDto(editedTeam));
    }

    @DeleteMapping()
    public void deleteTeam() throws Exception {
        teamService.deleteTeam();
    }

    @GetMapping("/getAllTeams")
    public ResponseEntity<List<TeamDto>> getAllTeams(){
        return ResponseEntity.ok(teamService.getAllTeams());
    }


    @GetMapping("/team-details/{teamName}")
    public ResponseEntity<TeamDto> getFullTeam(@PathVariable String teamName) throws Exception {
        return ResponseEntity.ok(teamService.getFullTeam(teamName));
    }

    @GetMapping("/generateRandomTeams")
    public void generateRandomTeams() throws Exception {
        teamService.generateRandomTeams();
    }

    @PostMapping("/joinTeam")
    public ResponseEntity<TeamDto> joinTeam(@RequestBody TeamRequest request) throws Exception {
        return teamService.joinTeam(request);
    }

    @GetMapping("/getTeamPassword")
    public ResponseEntity<String> getTeamPassword() throws Exception {
        return teamService.getTeamPassword();
    }

    @DeleteMapping("/leaveTeam")
    public ResponseEntity<TeamDto> leaveTeam() throws Exception {
        return teamService.leaveTeam();
    }

    @PostMapping("/kickPlayer")
    public ResponseEntity<TeamDto> kickPlayer(@RequestBody TeamPlayerRequest request) throws Exception {
        return teamService.kickPlayer(request.getSteamId());
    }

    @PostMapping("/search")
    public ResponseEntity<List<TeamDto>> searchTeams(@RequestBody TeamSearchRequest request) {
        return teamService.searchTeams(request.getSearchQuery());
    }

    @GetMapping("/existsByName/{teamName}")
    public boolean existsByName(@PathVariable String teamName) {
        return teamService.existsByName(teamName);
    }

    @GetMapping("/isUserTeamAdmin/{teamId}")
    public boolean isUserTeamAdmin(@PathVariable String teamId) throws Exception {
        return teamService.isUserTeamAdmin(teamId);
    }

    @GetMapping("/isUserTeamPlayer/{teamId}")
    public boolean isUserTeamPlayer(@PathVariable String teamId) throws Exception {
        return teamService.isUserTeamPlayer(teamId);
    }


    @GetMapping("/addUserToMyTeam")
    public void addUserToMyTeam() throws Exception {
        teamService.addUserToMyTeam();
    }


}
