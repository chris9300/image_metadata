package com.rspace.rspaceimgmetadata.microservice.util.excpetions;

public class DuplicateEntryException extends Exception{
    public DuplicateEntryException(String message, Throwable cause) {
        super(message, cause);
    }
}
