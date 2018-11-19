package com.rspace.rspaceimgmetadata.microservice.util.excpetions;

public class NoDatabaseEntryFoundException extends  Exception {
    public NoDatabaseEntryFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
