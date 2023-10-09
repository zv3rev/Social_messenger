package com.relex.relex_social.handler;

import com.relex.relex_social.exception.EmailAlreadyExistsException;
import com.relex.relex_social.exception.NicknameAlreadyExistsException;
import com.relex.relex_social.exception.ResourceNotFoundException;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;

@ControllerAdvice
@Log
public class GlobalHandler {

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<Object> handleBindException(BindException e){
        List<String> errors = e.getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        log.log(Level.INFO,
                String.format("Bind exception in: %s, error count: %d.",
                        Objects.requireNonNull(e.getBindingResult().getTarget()),
                        e.getAllErrors().size()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(NicknameAlreadyExistsException.class)
    protected ResponseEntity<String> handleNicknameAlreadyExistsException(NicknameAlreadyExistsException e){
        log.log(Level.INFO, "Attempt to register a profile with an existing nickname", e);
        return ResponseEntity.badRequest().body("This nickname is already occupied by another user");
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    protected ResponseEntity<String> handleEmailAlreadyExistsException(EmailAlreadyExistsException e){
        log.log(Level.INFO, "Attempt to register a profile with an existing E-mail", e);
        return ResponseEntity.badRequest().body("This E-mail is already occupied by another user");
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<String> handleBadCredentialsException(BadCredentialsException e){
        log.log(Level.INFO, "Unsuccessful authentication due to invalid login or password", e);
        return new ResponseEntity("Username or password is not correct", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected  ResponseEntity<String> handleResourceNotFoundExcception(ResourceNotFoundException e){
        log.log(Level.INFO, "Getting access to a non-existent resource", e);
        return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected  ResponseEntity<String> handleAccessDeniedException(AccessDeniedException e){
        log.log(Level.INFO, "Attempt to access someone else's resource", e);
        return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
    }
}
