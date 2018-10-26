package com.rspace.rspaceimgmetadata.microservice.service;

import com.rspace.rspaceimgmetadata.microservice.repository.ImageMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageMetadataService {

    @Autowired
    ImageMetadataRepository metadataRepository;

    public void insertNewImageMetadata(ImageMetadataEntity imageMetadata){
        metadataRepository.save(imageMetadata);
    }

    private void extractMetadataAsJson(){

    }
}
