package com.rspace.rspaceimgmetadata.microservice.util.excpetions;

public class CziMetadataSegmentNotFoundException extends CouldNotParseCZIMetadataException{
    public CziMetadataSegmentNotFoundException(String message, Throwable course) {
        super(message, course);
    }
}
