package com.palladium46.odonatesdota.user.service;

import com.palladium46.odonatesdota.exceptions.BannedUserException;
import com.palladium46.odonatesdota.security._refreshToken.service.RefreshTokenService;
import com.palladium46.odonatesdota.security.service.JwtService;
import com.palladium46.odonatesdota.steamAuth.model.PlayerInfo;
import com.palladium46.odonatesdota.steamAuth.service.SteamApiInteractionService;
import com.palladium46.odonatesdota.team.model.Team;
import com.palladium46.odonatesdota.user.model.Role;
import com.palladium46.odonatesdota.user.model.User;
import com.palladium46.odonatesdota.user.model.UserDto;
import com.palladium46.odonatesdota.user.model.UserMapper;
import com.palladium46.odonatesdota.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import static com.palladium46.odonatesdota.utils.VerificationMethods.verifyEntityNotNull;
import static com.palladium46.odonatesdota.steamAuth.utils.SteamAuthUtils.lastEditWithin30Minutes;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    private SteamApiInteractionService steamApiInteractionService;

    private final UserValidationService userValidationService;

    private final RefreshTokenService refreshTokenService;

    private final JwtService jwtService;

    public UserService(UserRepository userRepository, SteamApiInteractionService steamApiInteractionService, UserValidationService userValidationService, JwtService jwtService,@Lazy RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.steamApiInteractionService = steamApiInteractionService;
        this.userValidationService = userValidationService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    public User saveUser(PlayerInfo playerInfo, String steamId64bits) {
        userValidationService.checkUserRepositoryPresence();
        User newUser = UserMapper.initUser(
                steamId64bits,
                Role.ROLE_TEAMLESS,
                playerInfo
        );
        return userRepository.save(newUser);
    }


    public User getFullUserByName(String userName) {
        userValidationService.checkUserRepositoryPresence();
        User user = userRepository.findByUserName(userName);
        verifyEntityNotNull(user);
        return user;
    }


    public ResponseEntity<UserDto> getCurrentUser(String token){
        String username = jwtService.extractSteamId(token);
        User user = findBySteamId(username);
        if ( user.getTeam() == null){
            return ResponseEntity.ok(UserMapper.toDtoWithoutTeams(user));
        }
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    public Set<UserDto> getUsersSet(){
        userValidationService.checkUserRepositoryPresence();
        return userRepository.findAll().stream().map(UserMapper::toDtoWithoutTeams).collect(Collectors.toSet());
    }

    public ResponseEntity<String> deleteUser(String id){
        userValidationService.checkUserAndUserRepositoryPresence(id);
        userRepository.deleteById(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

    public User editUser(User existingUser, PlayerInfo playerInfo) {
        userValidationService.checkUserAndUserRepositoryPresence(existingUser.getSteamId64bits());
        User editedUser = UserMapper.updateUserWithPlayerInfo(existingUser, playerInfo);

        return userRepository.save(editedUser);
    }

    public void removeTeamFromUsers(Team team){
        for (User user : team.getUsers()) {
            user.setTeam(null);
            userRepository.save(user);
        }

    }

    public User addUserRole(User existingUser, Role role){
        existingUser.addRole(role);
        return userRepository.save(existingUser);
    }
    public User removeUserRole(User existingUser, Role role){
        existingUser.removeRole(role);
        return userRepository.save(existingUser);
    }

    public User findBySteamId(String steamId64bits){
        return userRepository.findBySteamId64bits(steamId64bits);
    }

    public boolean existsBySteamId(String steamId64bits){
        return userRepository.existsBySteamId64bits(steamId64bits);
    }

    public User saveOrUpdate(String steamId64bits) throws Exception {
        if (userRepository.existsBySteamId64bits(steamId64bits)){
            User foundUser = findBySteamId(steamId64bits);
            if (foundUser.isBanned()) {
                throw new BannedUserException("User is banned.");
            }
            refreshTokenService.deleteRefreshTokenFromUser(foundUser);
            // steamApiInteractionService costs resources, so it's 1 edit per 24h (for now).

            //if (!lastEditWithin30Minutes(foundUser.getLastEdit())){
                PlayerInfo playerInfo = steamApiInteractionService.getPlayerInfo(steamId64bits);

                return editUser(foundUser, playerInfo);
            //}
            //return foundUser;
        } else {
            PlayerInfo playerInfo = steamApiInteractionService.getPlayerInfo(steamId64bits);
            return saveUser(playerInfo, steamId64bits);
        }
    }

    public Set<User> findUsersByIds(List<UUID> userSteamIds) {
        return userRepository.findUsersByIds(userSteamIds);
    }

}
