package com.rspace.rspaceimgmetadata.microservice.util.excpetions;

public class NoValidCziFileException extends WrongOrCorruptFileException {
    public NoValidCziFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
