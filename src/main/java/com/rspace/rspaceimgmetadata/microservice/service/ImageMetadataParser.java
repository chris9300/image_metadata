package com.rspace.rspaceimgmetadata.microservice.service;


import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.imaging.common.ImageMetadata;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Provides functions to parse the Metadata from a ImageMetadata object (org.apache.commons.imaging.common.ImageMetadata) to hash map or json.
 */

public class ImageMetadataParser {

    private HashMap<String, Object> metadataMap;

    /**
     * Parses a ImageMetadata object to a json String.
     * @param imgMetadata
     * @return
     */
    public static String parseToJson(ImageMetadata imgMetadata){
        ImageMetadataParser parser = new ImageMetadataParser();
        final String json =  parser.toJson(imgMetadata);

        return json;
    }


    public HashMap<String, Object> toHashmap(ImageMetadata imgMetadata){
        List<? extends ImageMetadata.ImageMetadataItem> metadataList = imgMetadata.getItems();

        metadataMap = new HashMap<>();

        for (int i = 0; i < metadataList.size(); i++) {
            ImageMetadata.ImageMetadataItem item = metadataList.get(i);

            // Remove ' because the should not be in the json values
            // Split the key/val pair at the first ": "
            String[] pair = item.toString().replace("'","").split(": ", 2);

            // The keys have to inserted in lower cases because of the case-insensitiv search.
            // Upper-Case keys would not working
            String key = pair[0].toLowerCase();
            String nextVal = pair[1];

            if (metadataMap.containsKey(key)){

                // If kay exists, the values of the key have to be stored as list or array.
                handleDoubleKeyValues(key, nextVal);
            } else {
                metadataMap.put(key, nextVal);
            }
        }
        return metadataMap;
    }

    /**
     * Adds the new value to a List of values for the key. If the value isn't a list currently, creates a list.
     * @param key
     * @param val
     */
    private void handleDoubleKeyValues(String key, String val){
        if(metadataMap.get(key) instanceof List){
            ((List) metadataMap.get(key)).add(val);
        } else{
            List<String> valueList = new ArrayList<String>();
            valueList.add( (String) metadataMap.get(key));
            valueList.add(val);
            metadataMap.put(key, valueList);
        }
    }


    public String toJson(ImageMetadata imgMetadata){

        // The metadataList cannot directly be parsed into json.
        // That's why the tags are stored as key/value pairs in a hash map.
        toHashmap(imgMetadata);

        // convert hash map to json
        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter stringWriter = new StringWriter();

        try {
            objectMapper.writeValue(stringWriter, metadataMap);

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

