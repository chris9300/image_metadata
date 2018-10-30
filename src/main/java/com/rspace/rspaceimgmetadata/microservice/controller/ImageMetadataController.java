package com.rspace.rspaceimgmetadata.microservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rspace.rspaceimgmetadata.microservice.service.ImageMetadataEntity;
import com.rspace.rspaceimgmetadata.microservice.service.ImageMetadataService;

@RestController
public class ImageMetadataController {

    @Autowired
    private ImageMetadataService imageMetadataService;

    public ImageMetadataController() {
    }

    @PostMapping("/img_metadata/{customerId}/{rspaceImageId}/{version}/insert")
    public void insert(@PathVariable String customerId, @PathVariable Long rspaceImageId, @PathVariable int version, @RequestParam("file") MultipartFile imgFile){
        ImageMetadataEntity orgData = new ImageMetadataEntity(customerId, rspaceImageId, version);

        imageMetadataService.insertNewImageMetadata(orgData, imgFile);
        // todo process image
    }
    
    /**
     * A quick method to assert service is up and running
     * @return
     */
    @GetMapping
    public ResponseEntity<String> status() {
    	return new ResponseEntity<String>("OK", HttpStatus.OK);
    }
}
