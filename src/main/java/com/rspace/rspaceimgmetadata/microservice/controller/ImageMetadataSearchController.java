package com.rspace.rspaceimgmetadata.microservice.controller;

import com.rspace.rspaceimgmetadata.microservice.service.ImageMetadataSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ImageMetadataSearchController {

    @Autowired
    private ImageMetadataSearchService searchService;

    @GetMapping("/img_metadata/search/{searchTerm}")
    public String searchPrefixInAll(@PathVariable String searchTerm){
        return searchService.searchPrefixInAll(searchTerm);
    }


    /**
     * Searches for Images that contains the searchTerm as prefix in metadata with keys which are given as a json array.
     * @param searchTerm URL Path Arg: String prefix search Term
     * @param jsonKeys Json (string) array in the post-body
     * @return
     */
    @PostMapping("/img_metadata/search/prefix/inKeys/{searchTerm}")
    public String searchPrefixInKeysOfAllUsers(@PathVariable String searchTerm, @RequestBody String jsonKeys){
        return searchService.searchPrefixInKeysOfAllUsers(searchTerm, jsonKeys);
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
    public String searchPrefixInAllKeysOfUsers(@PathVariable String searchTerm, @RequestBody String jsonUsers){
        return searchService.searchPrefixInAllKeysOfUsers(searchTerm, jsonUsers);
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
    public String searchPrefixInKeysOfUsers(@PathVariable String searchTerm, @RequestBody String jsonParameter){
        return searchService.searchPrefixInKeysOfUsers(searchTerm, jsonParameter);
    }
}
