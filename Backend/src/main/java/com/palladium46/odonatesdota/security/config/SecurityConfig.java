package com.palladium46.odonatesdota.security.config;

import com.palladium46.odonatesdota.security.filter.JwtAuthFilter;
import com.palladium46.odonatesdota.security.service.UserInfoService;
import com.palladium46.odonatesdota.user.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserService userService;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserService userService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userService = userService;
    }

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return new UserInfoService(userService);
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionAuthenticationStrategy ->
                        sessionAuthenticationStrategy.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/public").permitAll()
                        .requestMatchers("/user").permitAll()
                        .requestMatchers("/sse/lobby-sauvage-list-event").permitAll()
                        .requestMatchers("/sse/lobby-sauvage-event").permitAll()
                        .requestMatchers("/sse/scrim-list-event").permitAll()
                        .requestMatchers("/sse/team-list-event").permitAll()
                        .requestMatchers("/steamAuth/process-steam-login").permitAll()
                        .requestMatchers("/api/refreshToken").permitAll()
                        .requestMatchers("/api/isValidRefreshToken").permitAll()
                        .requestMatchers("/team/getAllTeams").permitAll()
                        .requestMatchers("/api/images/**").permitAll()
                        .requestMatchers("/api/images/default/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/team/generateRandomTeams").permitAll()
                        .requestMatchers("/team/search").permitAll()
                        .requestMatchers("/team/existsByName/**").permitAll()
                        .requestMatchers("/team/isUserTeamAdmin/**").permitAll()
                        .requestMatchers("/team/isUserTeamPlayer/**").permitAll()
                        .requestMatchers("/team/team-details/**").permitAll()
                        .requestMatchers("/scrim/getAllScrims").permitAll()
                        .requestMatchers("/scrim/scrim-details/**").permitAll()
                        .requestMatchers("/team/addUserToMyTeam").permitAll()
                        .requestMatchers("/scrim").permitAll()
                        .requestMatchers("/scrim/getNumberScrimsPlayed/**").permitAll()

                        .requestMatchers("/user/getAllUsers").authenticated()
                        .requestMatchers("/scrim/isTeamBiggerThanOne").permitAll()
                        .requestMatchers("/scrim/accept/**").authenticated()
                        .requestMatchers("/scrim/reject/**").authenticated()
                        .requestMatchers("/scrim/propose/**").authenticated()
                        .requestMatchers("/scrim/proposals/**").authenticated()
                        .requestMatchers("/scrim/getScrimHistory").authenticated()
                        .requestMatchers("/scrim/join-scrim/**").authenticated()
                        .requestMatchers("/scrim/cancel/**").authenticated()
                        .requestMatchers("/scrim/result/**").authenticated()
                        .requestMatchers("/scrim/getScrimUserStatus/**").authenticated()
                        .requestMatchers("/team/kickPlayer").authenticated()
                        .requestMatchers("/team/getTeamPassword").authenticated()
                        .requestMatchers("/team/joinTeam").authenticated()
                        .requestMatchers("/team/leaveTeam").authenticated()
                        .requestMatchers("/team").authenticated()
                        .requestMatchers("/api/logout").authenticated()
                        .requestMatchers("/api/protected").authenticated()
                        .requestMatchers("/user/getCurrentUser").authenticated()

                        .requestMatchers("/api/only_user_access").hasAnyRole("USER")
                        .requestMatchers("/api/only_admin_access").hasAnyRole("ADMIN")
                        .requestMatchers("/ws/**").authenticated()

                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
