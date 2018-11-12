package com.rspace.rspaceimgmetadata.microservice.service;

import com.rspace.rspaceimgmetadata.microservice.Model.ImageMetadataEmbeddedKey;
import com.rspace.rspaceimgmetadata.microservice.Model.ImageMetadataEntity;
import com.rspace.rspaceimgmetadata.microservice.repository.ImageMetadataRepository;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImageMetadataService {

    @Autowired
    ImageMetadataRepository metadataRepository;


    public Optional<String> getImageMetadata(String custId, long rspaceImageId, int version){
        ImageMetadataEmbeddedKey key = new ImageMetadataEmbeddedKey(custId, rspaceImageId, version);

        Optional<ImageMetadataEntity> dbResult = metadataRepository.findById(key);
        Optional<String> jsonResult = Optional.empty();
        if(dbResult.isPresent()){
            jsonResult = Optional.of(dbResult.get().getJsonMetadata());
        }
        return jsonResult;
    }

    /**
     * Insert the metadata of the imgFile to the database
     * @param imageMetadataDO
     * @param imgFile
     */
    public void insertNewImageMetadata(ImageMetadataEntity imageMetadataDO, MultipartFile imgFile)throws WrongFileFormatException {
        updateImageMetadata(imageMetadataDO,imgFile);
    }


    /**
     * Updates the metadata of the imgFile in the database.
     * The images is identified by the customerID, rspaceImageID and the image Version.
     * If the database does not contain metadata with that key, a new row will be generated.
     * @param imageMetadataDO
     * @param imgFile
     */
    public void updateImageMetadata(ImageMetadataEntity imageMetadataDO, MultipartFile imgFile) throws WrongFileFormatException{
        try {
            ImageMetadata metadata = Imaging.getMetadata(imgFile.getInputStream(), imgFile.getName());
            String jsonMetadata = ImageMetadataParser.parseToJson(metadata);

            imageMetadataDO.setJsonMetadata(jsonMetadata);

        } catch (IOException ioex) {
            ioex.printStackTrace();
        } catch (ImageReadException e) {
            e.printStackTrace();
            throw new WrongFileFormatException("");
        }

        metadataRepository.save(imageMetadataDO);
    }

    public class WrongFileFormatException extends Exception {
        public WrongFileFormatException(String message) {
            super(message);
        }
    }


}
