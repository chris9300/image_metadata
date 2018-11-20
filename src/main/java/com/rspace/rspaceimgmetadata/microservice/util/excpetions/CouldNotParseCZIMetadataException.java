package com.rspace.rspaceimgmetadata.microservice.util.excpetions;

public class CouldNotParseCZIMetadataException extends WrongOrCorruptFileException{
    public CouldNotParseCZIMetadataException(String message, Throwable cause) {
        super(message, cause);
    }
}
