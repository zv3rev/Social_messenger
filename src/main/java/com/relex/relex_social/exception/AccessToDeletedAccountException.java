package com.relex.relex_social.exception;

import org.springframework.security.access.AccessDeniedException;

public class AccessToDeletedAccountException extends AccessDeniedException {
    public AccessToDeletedAccountException(String string) {
        super(string);
    }
}
