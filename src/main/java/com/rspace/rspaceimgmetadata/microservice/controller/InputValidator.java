package com.rspace.rspaceimgmetadata.microservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rspace.rspaceimgmetadata.microservice.Model.ImageMetadataEmbeddedKey;
import com.rspace.rspaceimgmetadata.microservice.Model.ImageMetadataEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * Checks input values that the service gets in the controller. Values can be added with the add* functions. All values
 * will be checked immediately. If one or more errors are detected, the isValid function becomes false. The last error
 * is stored in the lastHtmlErrorResponse. Which contains a error message which describes the error and a html error status.
 * The errors are ResponseEntity Object because it is assumed that the input parameters are received via the http-interface.
 */
public class InputValidator {
    final static String[] validFileTypes = {"image/jpg", "image/jpeg", "image/png", "image/tif", "image/tiff"};


    boolean valid = true;
    ResponseEntity<String> lastHtmlErrorResponse;

    public InputValidator() {
    }

    public InputValidator(ImageMetadataEntity imageMetadataInput) {
    }

    public InputValidator(ImageMetadataEmbeddedKey imageMetadataKey){

    }

    /**
     * Resets the Validator object.
     * The isValid function becomes true and the lastHtmlErrorReponse variable becomes empty
     */
    public void reset(){
        valid = true;
        lastHtmlErrorResponse = null;
    }

    /**
     * Add a json Kexs array and check if it is valid.
     * It has to be a valid json document and it has to be an json array.
     *
     * If the array is not valid, the isValid variable gets false and an error message will be stored.
     * @param jsonKeyArr
     */
    public void addJsonKeyArr(String jsonKeyArr){
        final ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode json = mapper.readTree(jsonKeyArr);
            if(!json.isArray()){
                valid = false;
                lastHtmlErrorResponse = new ResponseEntity<String>("The json \"keys\" array is corrupted (no json array detected)", HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } catch (IOException e) {
            valid = false;
            lastHtmlErrorResponse = new ResponseEntity<String>("The json \"keys\" array is corrupted (no valid json detected)", HttpStatus.UNPROCESSABLE_ENTITY);
        }


    }

    /**
     * Add a json UserId array and check if it is valid.
     * It has to be a valid json document and it has to be an json array.
     *
     * If the array is not valid, the isValid variable gets false and an error message will be stored.
     * @param jsonUserIdArr
     */
    public void addJsonUserIdArr(String jsonUserIdArr){
        final ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode json = mapper.readTree(jsonUserIdArr);
            if(!json.isArray()){
                valid = false;
                lastHtmlErrorResponse = new ResponseEntity<String>("The json \"keys\" array is corrupted (no json array detected)", HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } catch (IOException e) {
            valid = false;
            lastHtmlErrorResponse = new ResponseEntity<String>("The json \"users\" array is corrupted (no valid json detected)", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * Adds a json Parameter Object and check if it is valid.
     * A Valid json Parameter Object has to be a valid json document and contain these two arrays:
     * - keys
     * - user
     *
     * If the object is not valid, the isValid variable gets false and an error message will be stored.
     * @param jsonParameterObject
     */
    public void addJsonParameterObject(String jsonParameterObject){
        try {
            final ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(jsonParameterObject);

            if(json.has("keys")) {
                this.addJsonKeyArr(json.path("keys").asText());
            } else {
                valid = false;
                lastHtmlErrorResponse = new ResponseEntity<String>("No \"keys\" field was found in the json Parameter array", HttpStatus.UNPROCESSABLE_ENTITY);
            }


            if(json.has("users")) {
                this.addJsonKeyArr(json.path("users").asText());
            } else {
                valid = false;
                lastHtmlErrorResponse = new ResponseEntity<String>("No \"users\" field was found in the json Parameter array", HttpStatus.UNPROCESSABLE_ENTITY);
            }

        } catch (IOException e) {
            valid = false;
            lastHtmlErrorResponse = new ResponseEntity<String>("The json object is corrupted", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * Adds a file and check if the file is valid. Valid files have to be one of these filetypes:
     * - image/jpg
     * - image/jpeg
     * - image/png
     * - image/tif
     * - image/tiff
     *
     * If the file is not valid, the isValid variable gets false and an error message will be stored.
     * @param file
     */
    public void addFile(MultipartFile file){
        if(!Arrays.asList(validFileTypes).contains(file.getContentType())){
            valid = false;
            lastHtmlErrorResponse = new ResponseEntity<String>("No file or wrong file format detected", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
    }

    public boolean isValid(){
        return valid;
    }

    /**
     * Returns an HTML Error ResponseEntity of the last not valid parameter, that was added.
     *
     * @return The ResponseEntity contains a error message and a html (error) status.
     */
    public Optional<ResponseEntity<String>> getLastHtmlErrorResponse(){
        return Optional.ofNullable(lastHtmlErrorResponse);
    }
}
