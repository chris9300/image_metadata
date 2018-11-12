package com.rspace.rspaceimgmetadata.microservice.controller;

import com.rspace.rspaceimgmetadata.microservice.service.ImageMetadataSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.rspace.rspaceimgmetadata.microservice.Model.ImageMetadataEntity;
import com.rspace.rspaceimgmetadata.microservice.service.ImageMetadataService;

@RestController
public class ImageMetadataController {

    @Autowired
    private ImageMetadataService imageMetadataService;


    public ImageMetadataController() {
    }

    /**
     * Inserts the Metadata of a (new) image in the "form-data" body
     * The CustomerID, RspaceImageID and Version number are the used to identify the image
     * @param customerId
     * @param rspaceImageId
     * @param version
     * @param imgFile
     */
    @PutMapping("/img_metadata/{customerId}/{userId}/{rspaceImageId}/{version}/insert")
    public void insert(@PathVariable String customerId, @PathVariable String userId, @PathVariable Long rspaceImageId, @PathVariable int version, @RequestParam("file") MultipartFile imgFile){
        ImageMetadataEntity orgData = new ImageMetadataEntity(customerId, rspaceImageId, version);
        orgData.setUserId(userId);
        imageMetadataService.insertNewImageMetadata(orgData, imgFile);
    }

    /**
     * Updates the Metadata of an image. The image have to be in the  "form-data" body of the HTTP-POST Request
     * The CustomerID, RspaceImageID and Version number are the used to identify the image
     * @param customerId
     * @param rspaceImageId
     * @param version
     * @param imgFile
     */
    @PostMapping("/img_metadata/{customerId}/{userId}/{rspaceImageId}/{version}/update")
    public void update(@PathVariable String customerId, @PathVariable String userId, @PathVariable Long rspaceImageId, @PathVariable int version, @RequestParam("file") MultipartFile imgFile){
        ImageMetadataEntity orgData = new ImageMetadataEntity(customerId, rspaceImageId, version);
        orgData.setUserId(userId);
        imageMetadataService.updateImageMetadata(orgData, imgFile);
    }


    /**
     * Returns the json metadata that are associated to the image of the key parameters
     * The return value is a json string.
     * @param customerId
     * @param rspaceImageId
     * @param version
     * @return Json String
     */
    @GetMapping("/img_metadata/{customerId}/{rspaceImageId}/{version}/get")
    public String get(@PathVariable String customerId, @PathVariable Long rspaceImageId, @PathVariable int version){
        return imageMetadataService.getImageMetadata(customerId, rspaceImageId, version);
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
