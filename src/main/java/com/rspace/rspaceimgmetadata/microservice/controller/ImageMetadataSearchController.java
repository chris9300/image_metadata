package com.rspace.rspaceimgmetadata.microservice.controller;

import com.rspace.rspaceimgmetadata.microservice.service.ImageMetadataSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ImageMetadataSearchController {

    @Autowired
    private ImageMetadataSearchService searchService;


    @GetMapping("/img_metadata/search/{searchTerm}")
    public ResponseEntity<String> searchInAll(@PathVariable String searchTerm){
        String jsonStrResult = searchService.searchTermInAll(searchTerm);
        return createJsonResponseEntity(jsonStrResult);
    }


    /**
     * Searches for Images that contains the searchTerm as prefix in metadata with keys which are given as a json array.
     * @param searchTerm URL Path Arg: String prefix search Term
     * @param jsonKeys Json (string) array in the post-body
     * @return
     */
    @PostMapping("/img_metadata/search/inKeys/{searchTerm}")
    public ResponseEntity<String> searchInKeysOfAllUsers(@PathVariable String searchTerm, @RequestBody String jsonKeys){

        // Validate json Input
        InputValidator validator = new InputValidator();
        validator.addJsonKeyArr(jsonKeys);
        if(!validator.isValid()){
            return validator.getLastErrorResponse().get();
        }

        String jsonStrResult = searchService.searchTermInKeysOfAllUsers(searchTerm, jsonKeys);
        return createJsonResponseEntity(jsonStrResult);
    }

    /**
     * Searches for Images that contains the searchTerm as prefix in metadata of images that are from a user of the json users arr.
     * The resulted images are only from users of the json user array
     *
     * @param searchTerm
     * @param jsonUsers json array with user ids
     * @return
     */
    @PostMapping("/img_metadata/search/ofUsers/{searchTerm}")
    public ResponseEntity<String> searchInAllKeysOfUsers(@PathVariable String searchTerm, @RequestBody String jsonUsers){
        // Validate json Input
        InputValidator validator = new InputValidator();
        validator.addJsonUserIdArr(jsonUsers);
        if(!validator.isValid()){
            return validator.getLastErrorResponse().get();
        }

        String jsonStrResult = searchService.searchTermInAllKeysOfUsers(searchTerm, jsonUsers);
        return createJsonResponseEntity(jsonStrResult);
    }

    /**
     * Searches for Images that contains the searchTerm as prefix in metadata with keys which are given as a json array.
     * The resulted images are only from users of the json user array
     *
     * Json Body Structure:
     * {"keys": [], "users": []}
     *
     * @param searchTerm String prefix search Term
     * @param jsonParameter Json (string) array in the post-body which contains the keys and users
     * @return
     */
    @PostMapping("/img_metadata/search/inKeys/ofUsers/{searchTerm}")
    public ResponseEntity<String> searchInKeysOfUsers(@PathVariable String searchTerm, @RequestBody String jsonParameter){
        // Validate json Input
        InputValidator validator = new InputValidator();
        validator.addJsonParameterObject(jsonParameter);
        if(!validator.isValid()){
            return validator.getLastErrorResponse().get();
        }

        String jsonStrResult =searchService.searchTermInKeysOfUsers(searchTerm, jsonParameter);
        return createJsonResponseEntity(jsonStrResult);
    }

    /// ------------------ Prefix Searches ------------------

    /**
     * Searches in all images for any key which contains values with the searchPrefix
     * @param searchPrefix
     * @return
     */
    @GetMapping("/img_metadata/search/prefix/{searchPrefix}")
    public ResponseEntity<String> searchPrefixInAll(@PathVariable String searchPrefix){
        String jsonStrResult = searchService.searchPrefixInAll(searchPrefix);
        return createJsonResponseEntity(jsonStrResult);
    }


    /**
     * Searches for Images that contains the searchTerm as prefix in metadata with keys which are given as a json array.
     * @param searchTerm URL Path Arg: String prefix search Term
     * @param jsonKeys Json (string) array in the post-body
     * @return
     */
    @PostMapping("/img_metadata/search/prefix/inKeys/{searchTerm}")
    public ResponseEntity<String> searchPrefixInKeysOfAllUsers(@PathVariable String searchTerm, @RequestBody String jsonKeys){
        // Validate json Input
        InputValidator validator = new InputValidator();
        validator.addJsonKeyArr(jsonKeys);
        if(!validator.isValid()){
            return validator.getLastErrorResponse().get();
        }

        String jsonStrResult = searchService.searchPrefixInKeysOfAllUsers(searchTerm, jsonKeys);
        return createJsonResponseEntity(jsonStrResult);
    }

    /**
     * Searches for Images that contains the searchTerm as prefix in metadata of images that are from a user of the json users arr.
     * The resulted images are only from users of the json user array
     *
     * @param searchTerm
     * @param jsonUsers json array with user ids
     * @return
     */
    @PostMapping("/img_metadata/search/prefix/ofUsers/{searchTerm}")
    public ResponseEntity<String> searchPrefixInAllKeysOfUsers(@PathVariable String searchTerm, @RequestBody String jsonUsers){
        // Validate json Input
        InputValidator validator = new InputValidator();
        validator.addJsonUserIdArr(jsonUsers);
        if(!validator.isValid()){
            return validator.getLastErrorResponse().get();
        }


        String jsonStrResult = searchService.searchPrefixInAllKeysOfUsers(searchTerm, jsonUsers);
        return createJsonResponseEntity(jsonStrResult);
    }

    /**
     * Searches for Images that contains the searchTerm as prefix in metadata with keys which are given as a json array.
     * The resulted images are only from users of the json user array
     *
     * Json Body Structure:
     * {"keys": [], "users": []}
     *
     * @param searchTerm String prefix search Term
     * @param jsonParameter Json (string) array in the post-body which contains the keys and users
     * @return
     */
    @PostMapping("/img_metadata/search/prefix/inKeys/ofUsers/{searchTerm}")
    public ResponseEntity<String> searchPrefixInKeysOfUsers(@PathVariable String searchTerm, @RequestBody String jsonParameter){
        // Validate json Input
        InputValidator validator = new InputValidator();
        validator.addJsonParameterObject(jsonParameter);
        if(!validator.isValid()){
            return validator.getLastErrorResponse().get();
        }

        String jsonStrResult = searchService.searchPrefixInKeysOfUsers(searchTerm, jsonParameter);
        return createJsonResponseEntity(jsonStrResult);
    }

    private ResponseEntity<String> createJsonResponseEntity(String jsonStringBody){
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>(jsonStringBody, responseHeaders, HttpStatus.OK);
    }
}
