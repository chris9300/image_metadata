package com.rspace.rspaceimgmetadata.microservice.util.excpetions;

public class MetadataSegmentNotFoundException extends CouldNotParseCZIMetadataException{
    public MetadataSegmentNotFoundException(String message, Throwable course) {
        super(message, course);
    }
}
