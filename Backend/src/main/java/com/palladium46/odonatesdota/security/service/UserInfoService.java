package com.palladium46.odonatesdota.security.service;

import com.palladium46.odonatesdota.user.model.User;
import com.palladium46.odonatesdota.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {

    private final UserService userService;

    public UserInfoService(UserService userService) {
        this.userService = userService;
    }

    public UserInfoDetails loadUserBySteamId(String steamId64bits) throws UsernameNotFoundException {
        User user = userService.findBySteamId(steamId64bits);

        return Optional.ofNullable(user)
                .map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with steamId :" + steamId64bits));
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getFullUserByName(username);

        return Optional.ofNullable(user)
                .map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userName :" + username));
    }

}
