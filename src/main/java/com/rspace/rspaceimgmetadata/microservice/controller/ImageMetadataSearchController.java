package com.rspace.rspaceimgmetadata.microservice.controller;

import com.rspace.rspaceimgmetadata.microservice.service.ImageMetadataSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ImageMetadataSearchController {

    @Autowired
    private ImageMetadataSearchService searchService;


    @GetMapping("/img_metadata/search/{searchTerm}")
    public String searchInAll(@PathVariable String searchTerm){
        return searchService.searchTermInAll(searchTerm);
    }


    /**
     * Searches for Images that contains the searchTerm as prefix in metadata with keys which are given as a json array.
     * @param searchTerm URL Path Arg: String prefix search Term
     * @param jsonKeys Json (string) array in the post-body
     * @return
     */
    @PostMapping("/img_metadata/search/inKeys/{searchTerm}")
    public String searchInKeysOfAllUsers(@PathVariable String searchTerm, @RequestBody String jsonKeys){
        return searchService.searchTermInKeysOfAllUsers(searchTerm, jsonKeys);
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
    public String searchInAllKeysOfUsers(@PathVariable String searchTerm, @RequestBody String jsonUsers){
        return searchService.searchTermInAllKeysOfUsers(searchTerm, jsonUsers);
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
    public String searchInKeysOfUsers(@PathVariable String searchTerm, @RequestBody String jsonParameter){
        return searchService.searchPrefixInKeysOfUsers(searchTerm, jsonParameter);
    }

    /// ------------------ Prefix Searches ------------------

    /**
     * Searches in all images for any key which contains values with the searchPrefix
     * @param searchPrefix
     * @return
     */
    @GetMapping("/img_metadata/search/prefix/{searchPrefix}")
    public String searchPrefixInAll(@PathVariable String searchPrefix){
        return searchService.searchPrefixInAll(searchPrefix);
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
