package com.rspace.rspaceimgmetadata.microservice.controller;

import com.rspace.rspaceimgmetadata.microservice.repository.ImageMetadataRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageMetadataController {

    private ImageMetadataRepository imageMetadatarepo;

    public ImageMetadataController(ImageMetadataRepository imageMetadatarepo) {
        this.imageMetadatarepo = imageMetadatarepo;
    }

    @PostMapping("/img_metadata/add")
    public void insert(){

    }
}
