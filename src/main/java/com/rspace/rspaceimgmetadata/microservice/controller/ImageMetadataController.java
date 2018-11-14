package com.rspace.rspaceimgmetadata.microservice.controller;

import com.rspace.rspaceimgmetadata.microservice.service.ImageMetadataSearchService;
import com.rspace.rspaceimgmetadata.microservice.service.ImageMetadataService.DuplicateEntryException;
import com.rspace.rspaceimgmetadata.microservice.service.ImageMetadataService.WrongFileFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.rspace.rspaceimgmetadata.microservice.Model.ImageMetadataEntity;
import com.rspace.rspaceimgmetadata.microservice.service.ImageMetadataService;

import java.util.Optional;

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
    @PutMapping("/img_metadata/insert/{customerId}/{userId}/{rspaceImageId}/{version}")
    public ResponseEntity<String> insert(@PathVariable String customerId, @PathVariable String userId, @PathVariable Long rspaceImageId, @PathVariable int version, @RequestParam("file") MultipartFile imgFile){
        ImageMetadataEntity orgData = new ImageMetadataEntity(customerId, rspaceImageId, version);
        orgData.setUserId(userId);

        try {
            imageMetadataService.insertNewImageMetadata(orgData, imgFile);
        } catch (WrongFileFormatException e){
             return new ResponseEntity<String>("No file or wrong file format detected", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        } catch (DuplicateEntryException e){
            return  new ResponseEntity<String>("Image metadata with the same ID are already in the database", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<String>("Created", HttpStatus.NO_CONTENT);
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
    public ResponseEntity<String> update(@PathVariable String customerId, @PathVariable String userId, @PathVariable Long rspaceImageId, @PathVariable int version, @RequestParam("file") MultipartFile imgFile){
        ImageMetadataEntity orgData = new ImageMetadataEntity(customerId, rspaceImageId, version);
        orgData.setUserId(userId);
        try{
        imageMetadataService.updateImageMetadata(orgData, imgFile);
        } catch (WrongFileFormatException e){
            return new ResponseEntity<String>("No file or wrong file format detected", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }

        return new ResponseEntity<String>("Created", HttpStatus.NO_CONTENT);
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
    public ResponseEntity<String> get(@PathVariable String customerId, @PathVariable Long rspaceImageId, @PathVariable int version){

        Optional<String> jsonResponseBody = imageMetadataService.getImageMetadata(customerId, rspaceImageId, version);

        return createJsonHTTPResponse(jsonResponseBody);
    }

    
    /**
     * A quick method to assert service is up and running
     * @return
     */
    @GetMapping
    public ResponseEntity<String> status() {
    	return new ResponseEntity<String>("OK", HttpStatus.OK);
    }

    private ResponseEntity<String> createJsonHTTPResponse(Optional<String> jsonResonseBody){

        if(jsonResonseBody.isPresent()){
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<String>(jsonResonseBody.get(), responseHeaders, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<String>("No metadata for the requested images were found.", HttpStatus.NOT_FOUND);
        }

    }
}
