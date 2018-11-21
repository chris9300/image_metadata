package com.rspace.rspaceimgmetadata.microservice.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rspace.rspaceimgmetadata.microservice.controller.ImageMetadataController;
import com.rspace.rspaceimgmetadata.microservice.model.ImageMetadataEmbeddedKey;
import com.rspace.rspaceimgmetadata.microservice.model.ImageMetadataEntity;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.util.*;

/**
 * Checks input values that the service gets in the controller. Values can be added with the add* functions. All values
 * will be checked immediately. If one or more errors are detected, the isValid function becomes false. The last error
 * is stored in the lastHtmlErrorResponse. Which contains a error message which describes the error and a html error status.
 * The errors are ResponseEntity Object because it is assumed that the input parameters are received via the http-interface.
 */
public class InputValidator {
    ValidatorFactory factory;
    Logger logger;

    boolean valid = true;
    ResponseEntity<String> lastHtmlErrorResponse;
    List<ResponseEntity<String>> htmlErrorResponseList;

    private void addNewError(ResponseEntity<String> newErrorResponse){
        lastHtmlErrorResponse = newErrorResponse;
        htmlErrorResponseList.add(newErrorResponse);
        valid = false;
    }

    public InputValidator() {
        logger = LoggerFactory.getLogger(InputValidator.class);
        factory = Validation.buildDefaultValidatorFactory();
        htmlErrorResponseList = new ArrayList<ResponseEntity<String>>();
    }

    public InputValidator(ImageMetadataEmbeddedKey imageMetadataKey){
        this();
        validateEmbeddedKey(imageMetadataKey);
    }

    public InputValidator(ImageMetadataEntity imageMetadataInput) {
        this();
        validateImageMetadataEntity(imageMetadataInput);
    }

    /**
     * Validates all (atomic) variables of the ImageMetadataEntity object (including the embeddedKey Object).
     * The validation follow the annotated rules in the ImageMetadataEmbeddedKey class.
     *
     * If the array is not valid, the isValid variable gets false and an error message will be stored.
     * @param inputEntity
     */
    private void validateImageMetadataEntity(ImageMetadataEntity inputEntity){
        validateEmbeddedKey(inputEntity.getEmbeddedKey());
        Validator validator = factory.getValidator();

        // Validate atomic vars of ImageMetadataEntity
        Set<ConstraintViolation<ImageMetadataEntity>> constraintViolations = validator.validate(inputEntity);

        // handle errors
        if (constraintViolations.size() > 0) {
            for (ConstraintViolation<ImageMetadataEntity> violation : constraintViolations) {
                ResponseEntity<String> error = new ResponseEntity<String>(
                        "The url path variable " + violation.getPropertyPath() + " is invalid: " + violation.getMessage(),
                        HttpStatus.BAD_REQUEST);
                // Adds errors to the htmlErrorResponseList
                addNewError(error);
            }
        }
    }

    /**
     * Validates all (atomic) variables of the embeddedKey object.
     * The validation follow the annotated rules in the ImageMetadataEmbeddedKey class.
     *
     * If the array is not valid, the isValid variable gets false and an error message will be stored.
     * @param inputKeyEntity
     */
    private void validateEmbeddedKey(ImageMetadataEmbeddedKey inputKeyEntity){
        Validator validator = factory.getValidator();

        // Validate atomic vars of ImageMetadataEmbeddedKeyEntity
        Set<ConstraintViolation<ImageMetadataEmbeddedKey>> constraintViolations = validator.validate(inputKeyEntity);

        // handle errors
        if (constraintViolations.size() > 0) {
            for (ConstraintViolation<ImageMetadataEmbeddedKey> violation : constraintViolations) {
                ResponseEntity<String> error = new ResponseEntity<String>(
                        "The url path variable " + violation.getPropertyPath() + " is invalid: " + violation.getMessage(),
                        HttpStatus.BAD_REQUEST);
                // Adds errors to the htmlErrorResponseList
                addNewError(error);
            }
        }
    }

    /**
     * Resets the Validator object.
     * The isValid function becomes true and the lastHtmlErrorReponse variable becomes empty
     */
    public void clear(){
        valid = true;
        htmlErrorResponseList.clear();
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
                logger.debug(jsonKeyArr + " could not interpreded as json Array for the key Array");
                addNewError(new ResponseEntity<String>("The json \"keys\" array is corrupted (no json array detected)", HttpStatus.UNPROCESSABLE_ENTITY));
            }
        } catch (IOException e) {
            logger.debug(jsonKeyArr + " could not interpreded as json Array for the key Array");
            addNewError(new ResponseEntity<String>("The json \"keys\" array is corrupted (no valid json detected)", HttpStatus.UNPROCESSABLE_ENTITY));
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
                logger.warn(jsonUserIdArr + " could not interpreded as json Array for the user Array");
                addNewError(new ResponseEntity<String>("The json \"users\" array is corrupted (no json array detected)", HttpStatus.UNPROCESSABLE_ENTITY));
            }
        } catch (IOException e) {
            logger.warn(jsonUserIdArr + " could not interpreded as json Array for the user Array");
            addNewError(new ResponseEntity<String>("The json \"users\" array is corrupted (no valid json detected)", HttpStatus.UNPROCESSABLE_ENTITY));
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
                this.addJsonKeyArr(json.findPath("keys").toString());
            } else {
                addNewError( new ResponseEntity<String>("No \"keys\" field was found in the json Parameter array", HttpStatus.UNPROCESSABLE_ENTITY));
            }


            if(json.has("users")) {
                this.addJsonKeyArr(json.findPath("users").toString());
            } else {
                addNewError(new ResponseEntity<String>("No \"users\" field was found in the json Parameter array", HttpStatus.UNPROCESSABLE_ENTITY));
            }

        } catch (IOException e) {
            addNewError(new ResponseEntity<String>("The json object is corrupted", HttpStatus.UNPROCESSABLE_ENTITY));
        }
    }

    /**
     * Adds a file and check if the file is valid. (Does not check the file content)
     * Valid standard files have to be one of these filetypes:
     * - image/jpg
     * - image/jpeg
     * - image/png
     * - image/tif
     * - image/tiff
     *
     * Valid proprietary files have to have one of these extension:
     * - czi
     *
     * If the file is not valid, the isValid variable gets false and an error message will be stored.
     * @param file
     */
    public void addFile(MultipartFile file){
        // Check if file is supported
        if(!checkFile(file)){
            addNewError(new ResponseEntity<String>("No file or wrong file format detected", HttpStatus.UNSUPPORTED_MEDIA_TYPE));
        }
    }

    /**
     * Checks if the file is a supported standard or proprietary file
     * @param file
     * @return true if the file is supported
     */
    private boolean checkFile(MultipartFile file){
        // Check if standardFile
        if(FileTypeChecker.isSupportedStandardFile(file)){
            return true;
        }
        // Check if allowed proprietary file
        if(FileTypeChecker.isSupportedProprietaryFile(file)){
            if(file.getSize() > Integer.MAX_VALUE){
                // Check that the file size is not bigger than about 250 bytes. This is because the max size of byte arrays
                addNewError(new ResponseEntity<String>(
                        "The file is to large for the service. CZI Files are supported to a max of " + Integer.MAX_VALUE + " bytes",
                        HttpStatus.UNSUPPORTED_MEDIA_TYPE));
            }
            return true;
        }
        return false;
    }



    public boolean isValid(){
        return valid;
    }

    /**
     * Returns an HTML Error ResponseEntity of the last not valid parameter, that was added.
     *
     * @return The ResponseEntity contains a error message and a html (error) status.
     */
    public Optional<ResponseEntity<String>> getLastErrorResponse(){
        return Optional.ofNullable(lastHtmlErrorResponse);
    }

    /**
     * Returns an ArrayList with all founded Errors as ResponseEntity
     * @return
     */
    public List<ResponseEntity<String>> getAllErrorResponse() {
        return htmlErrorResponseList;
    }
}
