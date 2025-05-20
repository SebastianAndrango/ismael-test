package com.ismael.exception;

public class GenericException extends RuntimeException {
    public GenericException (String errorDetail){
        super(errorDetail);
    }
}
