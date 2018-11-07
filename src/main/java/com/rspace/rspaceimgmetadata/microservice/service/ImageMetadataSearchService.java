package com.rspace.rspaceimgmetadata.microservice.service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rspace.rspaceimgmetadata.microservice.repository.ImageMetadataJsonSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

@Service
public class ImageMetadataSearchService {

    @Autowired
    ImageMetadataJsonSearchRepository searchRepository;


    /**
     * Searches for images with the searches for images with
     * @return
     */
    public String searchPrefixOverAllKeys(String searchPrefix){
        String searchTerm = searchPrefix + "%";
        String[] searchResults = searchRepository.searchOverTargetKeys(searchTerm, "[\"$.Model\",\"$.YCbCrPositioning\", \"$.ColorSpace\"]");

        System.out.println(searchResults[0]);
        return searchResults[0];
    }

    /**
     * Searches over all Images for a given prefix in keys that are given in the json (target) Key array
     * @param searchPrefix
     * @param jsonTargtKeys Target Keyss as array with a .$ prefix
     * @return
     */
    public String searchPrefixInKeys(String searchPrefix, String jsonTargtKeys){
        // todo: Function that adds the $. prefixes on the keys
        String searchTerm = searchPrefix + "%";
        String[] searchResults = searchRepository.searchOverTargetKeys(searchTerm, jsonTargtKeys);

        return resultListToJsonArr(searchResults);
    }


    /**
     * Converts a (String) Array to a json array
     * @param jsonResultList
     * @return
     */
    private String resultListToJsonArr(String [] jsonResultList){
        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter stringWriter = new StringWriter();

        try {
            objectMapper.writeValue(stringWriter, jsonResultList);

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // todo exception handling
        return stringWriter.toString();
    }
}
