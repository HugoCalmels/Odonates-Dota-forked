package com.palladium46.odonatesdota.user.controller;

import com.palladium46.odonatesdota.steamAuth.model.PlayerInfo;
import com.palladium46.odonatesdota.user.model.SaveUserRequest;
import com.palladium46.odonatesdota.user.model.User;
import com.palladium46.odonatesdota.user.model.UserDto;
import com.palladium46.odonatesdota.user.model.UserMapper;
import com.palladium46.odonatesdota.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<UserDto> saveUser(@RequestBody SaveUserRequest request) {
        User savedUser = userService.saveUser(new PlayerInfo(request.getName(), request.getAvatar(), request.getSeasonRank(), request.getProfileUrl(), request.getLeaderboardRank(), request.getCountry()), request.getSteamId64bits());
        return ResponseEntity.ok(UserMapper.toDto(savedUser));
    }

    @GetMapping()
    public UserDto getFullUser(@RequestParam String userName) {
        User user = userService.getFullUserByName(userName);
        return UserMapper.toDto(user);
    }

    @GetMapping("/getCurrentUser")
    public ResponseEntity<UserDto> getCurrentUser(@CookieValue(name = "token", required = false) String token){
        return userService.getCurrentUser(token);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<Set<UserDto>> getAllUsers (){
        return ResponseEntity.ok(userService.getUsersSet());
    }

    @DeleteMapping("")
    public ResponseEntity deleteUser(@RequestParam String id) {
        return userService.deleteUser(id);
    }

    @PutMapping("")
    public ResponseEntity<UserDto> editUser(@RequestBody User existingUser, PlayerInfo playerInfo){
        return ResponseEntity.ok(UserMapper.toDto(userService.editUser(existingUser, playerInfo)));
    }

}
