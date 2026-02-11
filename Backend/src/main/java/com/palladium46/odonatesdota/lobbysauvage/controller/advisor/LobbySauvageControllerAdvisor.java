package com.palladium46.odonatesdota.lobbysauvage.controller.advisor;

import com.palladium46.odonatesdota.exceptions.EntityNotFoundException;
import com.palladium46.odonatesdota.exceptions.LobbyFullException;
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

@ControllerAdvice(basePackages = {"com.palladium46.odonatesdota.lobbysauvage.controller"})

public class LobbySauvageControllerAdvisor extends ResponseEntityExceptionHandler {
//TODO remove the ErrorUserDto for a generic name
    @ExceptionHandler(LobbyFullException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorUserDto> handleLobbyFullExceptions(UnAuthorizeException ex) {
        List<String> messages = new ArrayList<>();
        messages.add(ex.getMessage());
        return new ResponseEntity<>(
                new ErrorUserDto(ex.getClass().getName(),
                        LocalDateTime.now().toString(),
                        messages
                ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorUserDto> handleLobbyNotFound(UnAuthorizeException ex) {
        List<String> messages = new ArrayList<>();
        messages.add(ex.getMessage());
        return new ResponseEntity<>(
                new ErrorUserDto(ex.getClass().getName(),
                        LocalDateTime.now().toString(),
                        messages
                ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorUserDto> handleGenericException(Exception ex) {
        List<String> messages = new ArrayList<>();
        messages.add(ex.getMessage());
        return new ResponseEntity<>(
                new ErrorUserDto(ex.getClass().getName(),
                        LocalDateTime.now().toString(),
                        messages
                ), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
