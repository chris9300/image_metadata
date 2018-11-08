package com.rspace.rspaceimgmetadata.microservice.service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rspace.rspaceimgmetadata.microservice.repository.ImageMetadataJsonSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;

@Service
public class ImageMetadataSearchService {

    @Autowired
    ImageMetadataJsonSearchRepository searchRepository;


    /**
     * Searches for images that have metadata which matches with the searchTerm.
     * Searches in all keys and images of all users.
     * @param searchTerm
     * @return
     */
    public String searchTermInAll(String searchTerm){
        String[] searchResults = searchRepository.searchInAll(searchTerm);

        return resultListToJsonArr(searchResults);
    }

    /**
     * Searches for images that have metadata which contains with the searchPrefix.
     * Searches in all keys and images of all users.
     * @param searchPrefix
     * @return
     */
    public String searchPrefixInAll(String searchPrefix){
        return searchTermInAll(searchPrefix + "%");
    }

    /**
     * Searches over all Images for a given term in keys that are given in the json (target) Key array
     * Searches only in keys which are in the key array
     * Searches in images of all  users
     * @param searchTerm
     * @param jsonTargtKeys Target Keys as json array
     * @return
     */
    public String searchTermInKeysOfAllUsers(String searchTerm, String jsonTargtKeys){
        String[] searchResults = searchRepository.searchInKeysForAllUsers(searchTerm, createJsonKeyPathsArr(jsonTargtKeys));

        return resultListToJsonArr(searchResults);
    }

    /**
     * Searches over all Images for a given prefix in keys that are given in the json (target) Key array
     * Searches only in keys which are in the key array
     * Searches in images of all  users
     * @param searchPrefix
     * @param jsonTargtKeys Target Keys as array with a prefix
     * @return
     */
    public String searchPrefixInKeysOfAllUsers(String searchPrefix, String jsonTargtKeys){
        return searchTermInKeysOfAllUsers(searchPrefix + "%", jsonTargtKeys);
    }

    /**
     * Searches over Images of User of the users json array for terms as values of keys that are given in the keys json array
     * Searches only in keys which are in the key array
     * Searches only in images of users which are in the user array
     *
     * jsonParameter  Structure:
     * {"keys": [], "users": []}
     *
     * @param searchTerm
     * @param jsonParameter json Object with two json arrays
     * @return
     */
    public String searchTermInKeysOfUsers(String searchTerm, String jsonParameter){
        String keyArr = createJsonKeyPathsArr(extractJsonArrayWithKey("keys", jsonParameter));
        String userArr = extractJsonArrayWithKey("users", jsonParameter);

        String[] searchResults = searchRepository.searchInKeysOfUsers(searchTerm, keyArr, userArr);

        return resultListToJsonArr(searchResults);
    }

    /**
     * Searches over Images of User of the users json array for PREFIXES as values of keys that are given in the keys json array
     * Searches only in keys which are in the key array
     * Searches only in images of users which are in the user array
     *
     * jsonParameter  Structure:
     * {"keys": [], "users": []}
     *
     * @param searchPrefix
     * @param jsonParameter json Object with two json arrays
     * @return
     */
    public String searchPrefixInKeysOfUsers(String searchPrefix, String jsonParameter){
        return searchTermInKeysOfUsers(searchPrefix + "%", jsonParameter);
    }

    /**
     * Searches over Images of User of the users json array for terms as values of any keys
     * Searches in all keys of the images
     * Searches only in images of users which are in the user array
     *
     * @param searchTerm
     * @param jsonUsers json array with user IDs
     * @return
     */
    public String searchTermInAllKeysOfUsers(String searchTerm, String jsonUsers){
        String[] searchResults = searchRepository.searchInAllKeysOfUsers(searchTerm, jsonUsers);

        return resultListToJsonArr(searchResults);
    }

    /**
     * Searches over Images of User of the users json array for PREFIXES as values of any keys
     * Searches in all keys of the images
     * Searches only in images of users which are in the user array
     *
     *
     * @param searchPrefix
     * @param jsonUsers json array with user IDs
     * @return
     */
    public String searchPrefixInAllKeysOfUsers(String searchPrefix, String jsonUsers){
        return searchTermInAllKeysOfUsers(searchPrefix + "%", jsonUsers);
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
            return stringWriter.toString();

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // todo exception handling
        return "";
    }


    /**
     * Converts the elements of a json array to json_paths so a $. will added before each element
     * @param jsonKeyString
     * @return
     */
    private String createJsonKeyPathsArr(String jsonKeyString){
        // add path prefix to first element
        jsonKeyString = jsonKeyString.replace("[\"", "[\"$.");

        // add path prefix to all later elements
        //todo is this really save? Look for better way with jackson or in the sql query
        jsonKeyString = jsonKeyString.replace(",\"", ", \"$.");
        jsonKeyString = jsonKeyString.replace(", \"", ", \"$.");

        return jsonKeyString;
    }


    //todo exception handling  --> create wrongJsonParameterException?
    private String extractJsonArrayWithKey(String key, String jsonParameter){
        ObjectMapper mapper = new ObjectMapper();

        JsonNode json = null;
        try {
            json = mapper.readTree(jsonParameter);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonNode selectedElement = json.findPath(key);

        return selectedElement.toString();
    }

    //todo: Function that creates the search tags (prefix / suffix)
}


