package com.rspace.rspaceimgmetadata.microservice.controller;

import com.rspace.rspaceimgmetadata.microservice.repository.ImageMetadataRepository;
import com.rspace.rspaceimgmetadata.microservice.service.ImageMetadataEntity;
import com.rspace.rspaceimgmetadata.microservice.service.ImageMetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageMetadataController {

    @Autowired
    private ImageMetadataService imageMetadataService;

    public ImageMetadataController() {
    }

    @PostMapping("/img_metadata/{customerId}/{rspaceImageId}/{version}/insert")
    public void insert(@PathVariable String customerId, @PathVariable Long rspaceImageId, @PathVariable int version){
        ImageMetadataEntity orgData = new ImageMetadataEntity(customerId, rspaceImageId, version);

        imageMetadataService.insertNewImageMetadata(orgData);
        // todo process image
    }
}
