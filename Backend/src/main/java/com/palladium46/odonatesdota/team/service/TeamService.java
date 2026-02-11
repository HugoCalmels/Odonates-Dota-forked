package com.palladium46.odonatesdota.team.service;

import com.palladium46.odonatesdota.images.ImageService;
import com.palladium46.odonatesdota.scrim.service.ScrimService;
import com.palladium46.odonatesdota.security.service.SecurityService;
import com.palladium46.odonatesdota.sse.SseController;
import com.palladium46.odonatesdota.steamAuth.model.PlayerInfo;
import com.palladium46.odonatesdota.team.model.*;
import com.palladium46.odonatesdota.team.repository.TeamRepository;
import com.palladium46.odonatesdota.user.model.Role;
import com.palladium46.odonatesdota.user.model.User;
import com.palladium46.odonatesdota.user.service.UserService;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class TeamService {
    @Value("${backend.server}")
    private String backendServerAdress;

    private final UserService userService;

    private final TeamRepository teamRepository;

    private final ImageService imageService;

    private final TeamPasswordService teamPasswordService;

    private final SecurityService securityService;

    private final ScrimService scrimService;

    private final SseController sseController;

    public  TeamService(UserService userService,
                        TeamRepository teamRepository,
                        ImageService imageService,
                        TeamPasswordService teamPasswordService,
                        SecurityService securityService,
                        ScrimService scrimService,
                        @Lazy SseController sseController) {
        this.userService = userService;
        this.teamRepository = teamRepository;
        this.imageService = imageService;
        this.teamPasswordService = teamPasswordService;
        this.securityService = securityService;
        this.scrimService = scrimService;
        this.sseController = sseController;
    }

    public Team saveTeam(String teamName, String password, MultipartFile logoFile) throws Exception {
        final String initialBackendServerAdress = backendServerAdress;
        User currentUser = securityService.getFullCurrentUser();

        if (!hasOneTeam(currentUser)) {
            userService.removeUserRole(currentUser, Role.ROLE_TEAMLESS);
            userService.addUserRole(currentUser, Role.ROLE_CAPTAIN);
            String logoName = (logoFile != null) ? logoFile.getOriginalFilename() : "default image";
            String logoUrl = (logoFile != null) ? imageService.saveImage(logoFile) : initialBackendServerAdress + "/api/images/default";

            Team newTeam = TeamMapper.initTeam(
                    teamName,
                    teamPasswordService.encrypt(password),
                    currentUser,
                    logoUrl,
                    logoName
            );

            teamRepository.save(newTeam);
            sseController.triggerTeamListUpdateEvent("client update");
            return newTeam;
        }

        throw new Exception("Already has a team");
    }

    public Team editTeam(String teamName, String password, MultipartFile logoFile) throws Exception {
        User currentUser = securityService.getCurrentUser();
        Team team = currentUser.getTeam();

        if (isCaptain(currentUser, team)){
            checkAndHandleTeamImage(logoFile, currentUser);
            currentUser.getTeam().setName(teamName);
            currentUser.getTeam().setPassword(teamPasswordService.encrypt(password));
            return teamRepository.save(currentUser.getTeam());
        }
        throw new Exception("Insufficient permissions");
    }

    public void checkAndHandleTeamImage(MultipartFile logoFile, User currentUser) throws IOException {
        if (!logoFile.getOriginalFilename().equals("default image")){
            checkAndDeleteTeamImage(currentUser.getTeam());
            currentUser.getTeam().setLogo(imageService.saveImage(logoFile));
            currentUser.getTeam().setLogoName(logoFile.getOriginalFilename());
        }
    }

    public List<TeamDto> getAllTeams() {
        List<Team> teams = teamRepository.findByStatusNot(TeamStatus.DELETED);

        List<TeamDto> teamsDto = teams.stream()
                .map(TeamMapper::toDtoWithoutUsers)
                .collect(Collectors.toList());

        return teamsDto;
    }

    public ResponseEntity<TeamDto> joinTeam(TeamRequest request) throws Exception {
        User currentUser = securityService.getCurrentUser();
        Team team = teamRepository.findTeamById(Long.valueOf(request.getTeamId()));

        if (team != null) {
            String teamPassword = teamPasswordService.decrypt(team.getPassword());

            if (request.getPassword().equals(teamPassword)) {
                team.addUser(currentUser);
                userService.addUserRole(currentUser, Role.ROLE_PLAYER);
                userService.removeUserRole(currentUser, Role.ROLE_TEAMLESS);

                teamRepository.save(team);
                return ResponseEntity.ok(TeamMapper.toDto(team));
            }

            throw new Exception("Incorrect Team password");
        }

        throw new Exception("Team not found");
    }

    public TeamDto getFullTeam(String teamName) throws Exception {
        Team team = teamRepository.findByNameAndStatusNot(teamName, TeamStatus.DELETED);
        if (team != null){
            Set<User> sortedUsers = team.getUsers().stream()
                    .sorted(Comparator.comparingInt(User::getUserOrder))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            team.setUsers(sortedUsers);
            return TeamMapper.toDto(team);
        }
        throw new Exception("Team not found");
    }

    public ResponseEntity<String> getTeamPassword() throws Exception {
        User currentUser = securityService.getFullCurrentUser();
        Team team = currentUser.getTeam();

        if (isTeamMember(currentUser, team)){
            String decryptedPassword = teamPasswordService.decrypt(currentUser.getTeam().getPassword());
            return ResponseEntity.ok(decryptedPassword);
        }
        throw new Exception("Insufficient permissions");
    }

    @Transactional
    public void deleteTeam() throws Exception {
        User currentUser = securityService.getFullCurrentUser();
        Team team = currentUser.getTeam();

        if (isCaptain(currentUser, team)) {

            try {
                checkAndDeleteTeamImage(team);
                String logoName = "default image";
                String logoUrl = backendServerAdress + "/api/images/default";
                team.setLogo(logoUrl);
                team.setLogoName(logoName);

                userService.removeUserRole(currentUser, Role.ROLE_CAPTAIN);
                userService.addUserRole(currentUser, Role.ROLE_TEAMLESS);
                removePlayersRoles(team, currentUser);
                scrimService.cleanupNonFinishedScrimsForTeam(team);
                userService.removeTeamFromUsers(team);
                currentUser.setTeam(null);

                team.setStatus(TeamStatus.DELETED);
                teamRepository.save(team);
                sseController.triggerTeamListUpdateEvent("client update");
            } catch (Exception e) {
                throw e;
            }
        } else {
            throw new Exception("You do not have permission to delete this team.");
        }

    }

    public ResponseEntity<TeamDto> leaveTeam() throws Exception {
        User currentUser = securityService.getFullCurrentUser();
        userService.removeUserRole(currentUser, Role.ROLE_PLAYER);
        userService.addUserRole(currentUser, Role.ROLE_TEAMLESS);
        Team team = currentUser.getTeam();
        team.removeUser(currentUser);
        teamRepository.save(team);
        return ResponseEntity.ok(TeamMapper.toDto(team));
    }

    public ResponseEntity<TeamDto> kickPlayer(String playerToKickSteamId) throws Exception {
        User currentUser = securityService.getFullCurrentUser();
        Team team = currentUser.getTeam();
        if(isCaptain(currentUser, team)){
            User userToKick = userService.findBySteamId(playerToKickSteamId);
            userService.removeUserRole(userToKick, Role.ROLE_PLAYER);
            userService.addUserRole(userToKick, Role.ROLE_TEAMLESS);
            team.removeUser(userToKick);
            teamRepository.save(team);
            return ResponseEntity.ok(TeamMapper.toDto(team));
        }
        throw new Exception("Insufficient permissions");
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //
    // ~~~~~~~~~~~~~~~~~~~~~~~ Méthodes auxiliaires  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //


    public boolean isCaptain(User currentUser, Team team) {
        User teamCaptain = team.getCaptain();
        if ( currentUser.equals(teamCaptain)){
            return true;
        }
        return false;
    }

    public boolean hasOneTeam(User user){
        if (user.getTeam() == null){
            return false;
        }
        return true;
    }

    public void checkAndDeleteTeamImage(Team team){
        if (!team.getLogoName().equals("default image")){
            imageService.deleteImage(extractImageOriginalName(team.getLogo()));
        }
    }

    public void removePlayersRoles(Team team, User currentUser){
        Set<User> users = team.getUsers();
        for (User teamUser : users) {
            if (teamUser != currentUser){
                userService.removeUserRole(teamUser, Role.ROLE_PLAYER);
                userService.addUserRole(teamUser, Role.ROLE_TEAMLESS);
            }
        }
    }

    public static String extractImageOriginalName(String url) {
        String regex = "/api/images/([a-zA-Z0-9-]+.*$)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    public ResponseEntity<List<TeamDto>> searchTeams(String searchQuery) {
        if (searchQuery.length() < 3) {
            return ResponseEntity.ok(getAllTeams());
        } else {
            List<Team> teams = teamRepository.findByNameContainingIgnoreCase(searchQuery);
            List<TeamDto> teamsDto = teams.stream()
                    .filter(team -> team.getStatus() != TeamStatus.DELETED)
                    .map(TeamMapper::toDtoWithoutUsers)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(teamsDto);
        }
    }

    public boolean existsByName(String teamName){
        return teamRepository.existsByNameIgnoreCaseAndStatusNot(teamName, TeamStatus.DELETED);
    }

    public boolean isUserTeamAdmin(String teamId) throws Exception {
        User currentUser = securityService.getCurrentUser();
        User teamCaptain = teamRepository.findTeamById(Long.valueOf(teamId)).getCaptain();

        if ( currentUser != null && teamCaptain != null && currentUser.equals(teamCaptain)){
            return true;
        }
        return false;
    }

    public boolean isUserTeamPlayer(String teamId) throws Exception {
        User currentUser = securityService.getCurrentUser();
        Team team = teamRepository.findTeamById(Long.valueOf(teamId));

        if (currentUser != null && team != null) {
            for (User user : team.getUsers()) {
                if (currentUser.equals(user)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isTeamMember(User currentUser, Team team){
        if (team != null) {
            Set<User> teamMembers = team.getUsers();
            return teamMembers.contains(currentUser);
        }
        return false;
    }

    private static final int[] dotaRanks = {10, 11, 12, 13, 14, 20, 21, 22, 23, 24,
            30, 31, 32, 33, 34, 40, 41, 42, 43, 44,
            50, 51, 52, 53, 54, 60, 61, 62, 63, 64,
            70, 71, 72, 73, 74, 80, 81, 82, 83, 84};


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //
    // ~~~~~~~~~~~~~~~~~~~~~~ Méthodes de dummy data  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

    public void addUserToMyTeam() {
        // ajoute 5x joueurs random sur les steamIds teams
        String[] steamIds = {"76561199606688134", "76561197968554190"};

        for (String steamId : steamIds) {
            User u = userService.findBySteamId(steamId);
            Team team = u.getTeam();

            for (int i = 0; i < 5; i++) {
                int randomIndex = new Random().nextInt(dotaRanks.length);
                int playerSeasonRank = dotaRanks[randomIndex];

                String playerName = "Player" + i;
                String playerAvatar = "https://picsum.photos/200/300";
                String playerProfileUrl = "http://www.test.com";
                PlayerInfo randomPlayerInfo = new PlayerInfo(playerName, playerAvatar, playerSeasonRank, playerProfileUrl, null, null);

                String randomSteamId = Integer.toString((int) (Math.random() * 1000));
                User user = userService.saveUser(randomPlayerInfo, randomSteamId);
                userService.addUserRole(user, Role.ROLE_CAPTAIN);
                userService.removeUserRole(user, Role.ROLE_TEAMLESS);

                String newRandomSteamId = Integer.toString((int) (Math.random() * 1000));
                User newUser = userService.saveUser(randomPlayerInfo, newRandomSteamId);
                userService.addUserRole(newUser, Role.ROLE_PLAYER);
                userService.removeUserRole(newUser, Role.ROLE_TEAMLESS);

                team.addUser(newUser);
            }

            teamRepository.save(team);
        }
    }

    public void generateRandomTeams() throws Exception {
        for (int i = 0; i < 50; i++) {
            // BUILD PLAYER INFO
            String playerName = "Player" + i;
            String playerAvatar = "https://picsum.photos/200/300";
            int playerSeasonRank = (int) (Math.random() * 100);
            String playerProfileUrl = "http//www.test.com";
            PlayerInfo randomPlayerInfo = new PlayerInfo(playerName, playerAvatar, playerSeasonRank, playerProfileUrl, null, null);

            // BUILD USER
            String randomSteamId = Integer.toString((int) (Math.random() * 1000));
            User user = userService.saveUser(randomPlayerInfo, randomSteamId);
            userService.addUserRole(user, Role.ROLE_CAPTAIN);
            userService.removeUserRole(user, Role.ROLE_TEAMLESS);

            // BUILD TEAM
            String teamName = "Team" + i;
            String teamPassword = "password" + i;
            String teamLogo = "https://picsum.photos/200/300";
            String logoName = "exemple.png";
            String teamAcronym = "OA";
            Team team = TeamMapper.initTeam(teamName, teamPasswordService.encrypt(teamPassword), user, teamLogo, logoName);
            teamRepository.save(team);

            // ADD USERS TO TEAM
            for ( int z = 0 ; z < 4 ; z ++){
                String newRandomSteamId = Integer.toString((int) (Math.random() * 1000));
                User newUser = userService.saveUser(randomPlayerInfo, newRandomSteamId);
                userService.addUserRole(newUser, Role.ROLE_PLAYER);
                userService.removeUserRole(newUser, Role.ROLE_TEAMLESS);
                team.addUser(newUser);
            }
        }
    }


}
