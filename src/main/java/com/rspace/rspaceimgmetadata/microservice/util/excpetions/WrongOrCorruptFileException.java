package com.rspace.rspaceimgmetadata.microservice.util.excpetions;

public class WrongOrCorruptFileException extends Exception {
    public WrongOrCorruptFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
