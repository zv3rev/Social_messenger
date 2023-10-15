package com.relex.relex_social.exception;

public class PermanentlyDeletedAccountException extends RuntimeException {
    public PermanentlyDeletedAccountException(String string) {
        super(string);
    }
}
