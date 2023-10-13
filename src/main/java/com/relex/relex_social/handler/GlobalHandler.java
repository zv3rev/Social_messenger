package com.relex.relex_social.handler;

import com.relex.relex_social.dto.response.AppErrorDto;
import com.relex.relex_social.exception.*;
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

@ControllerAdvice
@Log
public class GlobalHandler {

    private ResponseEntity getResponseEntity(Throwable e, HttpStatus httpStatus) {
        return new ResponseEntity(new AppErrorDto(httpStatus.value(), e.getMessage()), httpStatus);
    }

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<Object> handleBindException(BindException e) {
        List<String> errors = e.getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .toList();
        log.log(Level.INFO,
                String.format("Bind exception in: %s, error count: %d.",
                        Objects.requireNonNull(e.getBindingResult().getTarget()),
                        e.getAllErrors().size()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(NicknameAlreadyExistsException.class)
    protected ResponseEntity handleNicknameAlreadyExistsException(NicknameAlreadyExistsException e) {
        log.log(Level.INFO, "Attempt to register a profile with an existing nickname", e);
        return getResponseEntity(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    protected ResponseEntity handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {
        log.log(Level.INFO, "Attempt to register a profile with an existing E-mail", e);
        return getResponseEntity(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FriendshipRequestAlreadyExistException.class)
    protected ResponseEntity handelFriendshipRequestAlreadyExistException(FriendshipRequestAlreadyExistException e){
        log.log(Level.INFO, "Attempt to send a repeat friendship request", e);
        return getResponseEntity(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity handleBadCredentialsException(BadCredentialsException e) {
        log.log(Level.INFO, "Unsuccessful authentication due to invalid login or password", e);
        return getResponseEntity(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity handleResourceNotFoundExcception(ResourceNotFoundException e) {
        log.log(Level.INFO, "Getting access to a non-existent resource", e);
        return getResponseEntity(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity handleAccessDeniedException(AccessDeniedException e) {
        log.log(Level.INFO, "Attempt to access someone else's resource", e);
        return getResponseEntity(e, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(WrongMethodException.class)
    protected ResponseEntity handleWrongMethodException(WrongMethodException e){
        log.log(Level.INFO, "Attempt to call a method for an object that is not processed by it", e);
        return getResponseEntity(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity handleIllegalArgumentException(IllegalArgumentException e){
        log.log(Level.INFO, "A method with unsuitable parameters was called", e);
        return getResponseEntity(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SendingRestrictionException.class)
    protected ResponseEntity handleSendingRestrictionException(SendingRestrictionException e){
        log.log(Level.INFO, "Attempt to send a message to a user with a sending restriction", e);
        return getResponseEntity(e, HttpStatus.BAD_REQUEST);
    }
}
