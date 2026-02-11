package com.palladium46.odonatesdota.team.controller.advisor;

import com.palladium46.odonatesdota.exceptions.BadRequestException;
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

@ControllerAdvice(basePackages = "com.palladium46.odonatesdota.team.controller")
public class TeamControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UnAuthorizeException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorUserDto> handleUnauthorizedExeception(UnAuthorizeException ex) {
        List<String> messages = new ArrayList<>();
        messages.add(ex.getMessage());
        return new ResponseEntity<>(
                new ErrorUserDto(ex.getClass().getName(),
                        LocalDateTime.now().toString(),
                        messages
                ), HttpStatus.UNAUTHORIZED);
    }
}
