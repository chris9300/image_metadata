package com.rspace.rspaceimgmetadata.microservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rspace.rspaceimgmetadata.microservice.repository.ImageMetadataRepository;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.GenericImageMetadata;
import org.apache.commons.imaging.common.ImageMetadata.ImageMetadataItem;
import org.apache.commons.imaging.common.ImageMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import javax.imageio.stream.ImageInputStream;
import java.io.IOException;

@Service
public class ImageMetadataService {

    @Autowired
    ImageMetadataRepository metadataRepository;

    public void insertNewImageMetadata(ImageMetadataEntity imageMetadataDO, MultipartFile imgFile) {
        try {
            ImageMetadata metadata = Imaging.getMetadata(imgFile.getInputStream(), imgFile.getName());

            imageMetadataDO.setJsonMetadata(extractMetadataAsJson(metadata));

        } catch (IOException ioex) {
            // todo Exception handling
            ioex.printStackTrace();
        } catch (ImageReadException e) {
            // todo EceptionHandling
            e.printStackTrace();
        }

        metadataRepository.save(imageMetadataDO);
    }

    private String extractMetadataAsJson(ImageMetadata imageMetadata) {
        List<? extends ImageMetadataItem> metadataList = imageMetadata.getItems();


        // The metadataList cannot directly be parsed into json.
        // That's why the tags are stored as key/value pairs in a hashmap. This hashmap can be parsed by jackson
        HashMap<String, String> metadataMap = new HashMap<>();
        for (int i = 0; i < metadataList.size(); i++) {
            ImageMetadataItem item = metadataList.get(i);

            // todo: handle what happens if 2 vals has the same key (e.g. Keywords)
            String[] pair = item.toString().split(":", 2);
            metadataMap.put(pair[0], pair[1]);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter stringWriter = new StringWriter();

        try {
            objectMapper.writeValue(stringWriter, metadataMap);

        } catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("" + stringWriter);

        return stringWriter.toString();
    }
}
