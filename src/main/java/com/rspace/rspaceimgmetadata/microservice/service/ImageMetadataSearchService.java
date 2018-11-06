package com.rspace.rspaceimgmetadata.microservice.service;

import com.rspace.rspaceimgmetadata.microservice.repository.ImageMetadataJsonSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageMetadataSearchService {

    @Autowired
    ImageMetadataJsonSearchRepository searchRepository;


    /**
     * Searches for images with the searches for images with
     * @return
     */
    public String searchPrefixOverAllKeys(String searchTerm){
        String[] searchResults = searchRepository.searchOverTargetKeys("1%", "[\"$.YCbCrPositioning\", \"$.ColorSpace\"]");

        System.out.println(searchResults[0]);
        return searchResults[0];
    }
}
