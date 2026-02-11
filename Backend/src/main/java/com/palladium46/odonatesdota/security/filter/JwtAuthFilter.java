package com.palladium46.odonatesdota.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palladium46.odonatesdota.exceptions.RefreshTokenException;
import com.palladium46.odonatesdota.exceptions.UnAuthorizeException;
import com.palladium46.odonatesdota.security.model.ErrorFilterResponse;
import com.palladium46.odonatesdota.security.service.JwtService;

import com.palladium46.odonatesdota.security.service.UserInfoDetails;
import com.palladium46.odonatesdota.security.service.UserInfoService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserInfoService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, UserInfoService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Cookie[] cookies = request.getCookies();
            String token = null;
            String steamId = null;

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("token".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }

            if (token != null) {
                steamId = jwtService.extractSteamId(token);
            }

            if (steamId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserInfoDetails userDetails = userDetailsService.loadUserBySteamId(steamId);

                if (jwtService.isValidToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    setErrorResponse(HttpStatus.UNAUTHORIZED, response, new UnAuthorizeException("Jwt not validated"));
                    return;
                }
            }

            filterChain.doFilter(request, response);
        } catch (RefreshTokenException e) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, e);
        } catch (UnAuthorizeException e) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, e);
        }  catch (Exception e) {
            setErrorResponse(HttpStatus.BAD_REQUEST, response, e);
        }
    }

    private void setErrorResponse(HttpStatus status, HttpServletResponse response, Throwable ex) {
        response.setStatus(status.value());
        response.setContentType("application/json");

        ErrorFilterResponse errorResponse = new ErrorFilterResponse();
        errorResponse.setStatus(status.value());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimestamp(System.currentTimeMillis());

        try {
            String json = new ObjectMapper().writeValueAsString(errorResponse);
            response.getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
