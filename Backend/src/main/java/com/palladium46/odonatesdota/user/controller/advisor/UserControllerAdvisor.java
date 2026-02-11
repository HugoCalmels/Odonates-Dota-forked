package com.palladium46.odonatesdota.user.controller.advisor;

import com.palladium46.odonatesdota.exceptions.BadRequestException;
import com.palladium46.odonatesdota.exceptions.EntityNotFoundException;
import com.palladium46.odonatesdota.exceptions.ValidationException;
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

@ControllerAdvice(basePackages = "com.palladium46.odonatesdota.user.controller")
public class UserControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorUserDto> handleBadRequestException(BadRequestException ex) {
        System.out.println("UserControllerAdvisor");
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
    public ResponseEntity<ErrorUserDto> handleEntityNotFoundException(EntityNotFoundException ex) {
        System.out.println("UserControllerAdvisor");
        List<String> messages = new ArrayList<>();
        messages.add(ex.getMessage());
        return new ResponseEntity<>(
                new ErrorUserDto(ex.getClass().getName(),
                        LocalDateTime.now().toString(),
                        messages
                ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorUserDto> handleValidationException(ValidationException ex){
        System.out.println("UserControllerAdvisor");
        ErrorUserDto response = new ErrorUserDto(ex.getClass().getName(), LocalDateTime.now().toString(),ex.getMessages());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorUserDto> handleNullPointerException(NullPointerException ex) {
        System.out.println("UserControllerAdvisor");
        List<String> messages = new ArrayList<>();
        messages.add(ex.getMessage());
        return new ResponseEntity<>(
                new ErrorUserDto(ex.getClass().getName(),
                        LocalDateTime.now().toString(),
                        messages
                ), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //Basique sur nimportel quelle exception pas gerée
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorUserDto> handleGenericException(Exception ex) {
        System.out.println("UserControllerAdvisor");
        List<String> messages = new ArrayList<>();
        messages.add("Exception not handled cause dev is noob : " + ex.getMessage());
        return new ResponseEntity<>(new ErrorUserDto(ex.getClass().getName(), LocalDateTime.now().toString(), messages), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
