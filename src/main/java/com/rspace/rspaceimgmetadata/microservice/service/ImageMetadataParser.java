package com.rspace.rspaceimgmetadata.microservice.service;


import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rspace.rspaceimgmetadata.microservice.util.excpetions.CouldNotParseCZIMetadataException;
import com.rspace.rspaceimgmetadata.microservice.util.excpetions.CouldNotParseStdMetadataException;
import org.apache.commons.imaging.common.IImageMetadata;
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
    public static String parseToJson(IImageMetadata imgMetadata) throws CouldNotParseStdMetadataException {
        ImageMetadataParser parser = new ImageMetadataParser();
        final String json =  parser.toJson(imgMetadata);

        return json;
    }


    /**
     * Parses the extracted Metadata of the IImageMetadata object to a Hashmap.
     * The hashmap converts the keys to lower cases and adds values with the same key to one key-node
     * So the json parser can interpret them and does not overwrite values which have the same key
     *
     * @param imgMetadata
     * @return
     */
    public HashMap<String, Object> toHashmap(IImageMetadata imgMetadata){
        metadataMap = new HashMap<>();
        if(imgMetadata == null){
            return metadataMap;
        }

        List<? extends ImageMetadata.IImageMetadataItem> metadataList = imgMetadata.getItems();


        for (int i = 0; i < metadataList.size(); i++) {
            ImageMetadata.IImageMetadataItem item = metadataList.get(i);

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


    /**
     * Parses the metadata hash map to a json String
     * @param imgMetadata
     * @return
     */
    public String toJson(IImageMetadata imgMetadata) throws CouldNotParseStdMetadataException {

        // The metadataList cannot directly be parsed into json.
        // That's why the tags are stored as key/value pairs in a hash map.
        toHashmap(imgMetadata);

        // convert hash map to json
        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter stringWriter = new StringWriter();

        try {
            objectMapper.writeValue(stringWriter, metadataMap);

        } catch (IOException e) {
            e.printStackTrace();
            throw new CouldNotParseStdMetadataException("An error occured by parsing the metadata hashmap to the json string", e);
        }

        // todo exception handling
        return stringWriter.toString();
    }
}

