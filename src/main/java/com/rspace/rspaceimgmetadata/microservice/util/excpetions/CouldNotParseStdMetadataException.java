package com.rspace.rspaceimgmetadata.microservice.util.excpetions;

public class CouldNotParseStdMetadataException extends WrongOrCorruptFileException {
    public CouldNotParseStdMetadataException(String message, Throwable cause) {
        super(message, cause);
    }
}
