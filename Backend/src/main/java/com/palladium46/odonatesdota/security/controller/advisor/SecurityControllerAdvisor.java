package com.palladium46.odonatesdota.security.controller.advisor;

import com.palladium46.odonatesdota.exceptions.RefreshTokenException;
import com.palladium46.odonatesdota.exceptions.UnAuthorizeException;
import com.palladium46.odonatesdota.user.model.ErrorUserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice(basePackages = {"com.palladium46.odonatesdota.security.controller", "com.palladium46.odonatesdota.security.config"})

public class SecurityControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UnAuthorizeException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorUserDto> handleUnauthorizedException(UnAuthorizeException ex) {
        System.out.println("Handling Unauthorized Exceptions");

        List<String> messages = new ArrayList<>();
        messages.add(ex.getMessage());
        return new ResponseEntity<>(
                new ErrorUserDto(ex.getClass().getName(),
                        LocalDateTime.now().toString(),
                        messages
                ), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(RefreshTokenException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorUserDto> handleRefreshTokenException(RefreshTokenException ex) {
        System.out.println("TEST : IS HANDLE REFRESH TOKEN EXCEPTION BEING CALLED ??");
        List<String> messages = new ArrayList<>();
        messages.add(ex.getMessage());
        return new ResponseEntity<>(
                new ErrorUserDto(ex.getClass().getName(),
                        LocalDateTime.now().toString(),
                        messages
                ), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorUserDto> handleGenericException(Exception ex) {
        System.out.println("Handling Generic Exception");

        List<String> messages = new ArrayList<>();
        messages.add(ex.getMessage());
        return new ResponseEntity<>(
                new ErrorUserDto(ex.getClass().getName(),
                        LocalDateTime.now().toString(),
                        messages
                ), HttpStatus.UNAUTHORIZED);
    }

}
