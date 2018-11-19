package com.rspace.rspaceimgmetadata.microservice.controller;

import com.rspace.rspaceimgmetadata.microservice.model.ImageMetadataEmbeddedKey;
import com.rspace.rspaceimgmetadata.microservice.util.InputValidator;
import com.rspace.rspaceimgmetadata.microservice.util.excpetions.DuplicateEntryException;
import com.rspace.rspaceimgmetadata.microservice.util.excpetions.WrongOrCorruptFileException;
import com.rspace.rspaceimgmetadata.microservice.util.excpetions.NoDatabaseEntryFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.rspace.rspaceimgmetadata.microservice.model.ImageMetadataEntity;
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
        ImageMetadataEntity inputData = new ImageMetadataEntity(customerId, rspaceImageId, version);
        inputData.setUserId(userId);

        InputValidator validator = new InputValidator(inputData);
        validator.addFile(imgFile);
        if(!validator.isValid()){
            return validator.getLastErrorResponse().get();
        }
        try {
            imageMetadataService.insertNewImageMetadata(inputData, imgFile);
        } catch (WrongOrCorruptFileException e){
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
    @PostMapping("/img_metadata/update/{customerId}/{userId}/{rspaceImageId}/{version}")
    public ResponseEntity<String> update(@PathVariable String customerId, @PathVariable String userId, @PathVariable Long rspaceImageId, @PathVariable int version, @RequestParam("file") MultipartFile imgFile){

        ImageMetadataEntity inputData = new ImageMetadataEntity(customerId, rspaceImageId, version);
        inputData.setUserId(userId);

        InputValidator validator = new InputValidator(inputData);
        validator.addFile(imgFile);
        if(!validator.isValid()){
            return validator.getLastErrorResponse().get();
        }

        try{
        imageMetadataService.updateImageMetadata(inputData, imgFile);
        } catch (WrongOrCorruptFileException e){
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
    @GetMapping("/img_metadata/get/{customerId}/{rspaceImageId}/{version}")
    public ResponseEntity<String> get(@PathVariable String customerId, @PathVariable Long rspaceImageId, @PathVariable int version){
        ImageMetadataEmbeddedKey imageKey = new ImageMetadataEmbeddedKey(customerId, rspaceImageId, version);

        InputValidator validator = new InputValidator(imageKey);
        if(!validator.isValid()){
            return validator.getLastErrorResponse().get();
        }

        Optional<String> jsonResponseBody = imageMetadataService.getImageData(customerId, rspaceImageId, version);

        return createJsonHttpGetResponse(jsonResponseBody);
    }

    /**
     * Deletes the associated image (key) in the database.
     * If the image (key) is not found in the database a NotDatabaseEntryFoundException will be catched.
     * In this case responses a Http NOT_FOUND
     * @param customerId
     * @param rspaceImageId
     * @param version
     * @return Http OK will image can be deleted. Http NOT_FOUND if not.
     */
    @DeleteMapping("/img_metadata/delete/{customerId}/{rspaceImageId}/{version}")
    private ResponseEntity<String> delete(@PathVariable String customerId, @PathVariable Long rspaceImageId, @PathVariable int version){
        ImageMetadataEmbeddedKey imageKey = new ImageMetadataEmbeddedKey(customerId, rspaceImageId, version);

        InputValidator validator = new InputValidator(imageKey);
        if(!validator.isValid()){
            return validator.getLastErrorResponse().get();
        }

        try {
            imageMetadataService.deleteImageMetadata(imageKey);
            return new ResponseEntity<String>("Deleted", HttpStatus.OK);
        } catch (NoDatabaseEntryFoundException e) {
            return new ResponseEntity<String>("Could not delete!", HttpStatus.NOT_FOUND);
        }
    }

    
    /**
     * A quick method to assert service is up and running
     * @return
     */
    @GetMapping
    private ResponseEntity<String> status() {
    	return new ResponseEntity<String>("Service is running", HttpStatus.OK);
    }

    private ResponseEntity<String> createJsonHttpGetResponse(Optional<String> jsonResonseBody){

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
