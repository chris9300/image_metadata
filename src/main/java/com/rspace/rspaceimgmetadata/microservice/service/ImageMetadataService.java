package com.rspace.rspaceimgmetadata.microservice.service;

import com.rspace.rspaceimgmetadata.microservice.repository.ImageMetadataRepository;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageMetadataService {

    @Autowired
    ImageMetadataRepository metadataRepository;

    public void insertNewImageMetadata(ImageMetadataEntity imageMetadataDO, MultipartFile imgFile) {
        try {
            ImageMetadata metadata = Imaging.getMetadata(imgFile.getInputStream(), imgFile.getName());
            String jsonMetadata = ImageMetadataParser.parseToJson(metadata);

            imageMetadataDO.setJsonMetadata(jsonMetadata);

            System.out.println(jsonMetadata);

        } catch (IOException ioex) {
            // todo Exception handling
            ioex.printStackTrace();
        } catch (ImageReadException e) {
            // todo EceptionHandling
            e.printStackTrace();
        }

        metadataRepository.save(imageMetadataDO);
    }

}
