package com.rspace.rspaceimgmetadata.microservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rspace.rspaceimgmetadata.microservice.Model.ImageMetadataEmbeddedKey;
import com.rspace.rspaceimgmetadata.microservice.Model.ImageMetadataEntity;
import com.rspace.rspaceimgmetadata.microservice.repository.ImageMetadataRepository;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
            jsonResult = imageMetadataEntityToJson(dbResult.get());
        }
        return jsonResult;
    }


    /**
     * Insert the metadata of the imgFile to the database
     * @param imageMetadataDO
     * @param imgFile
     * @exception WrongFileFormatException Thrown if the Metadata can not be extracted from the image file (Assumes, that the format is wrong)
     * @exception DuplicateEntryException Thrown if the key if the new ImageMetadataEntity already exists
     */
    public void insertNewImageMetadata(ImageMetadataEntity imageMetadataDO, MultipartFile imgFile)
            throws WrongFileFormatException, DuplicateEntryException {
        addMetadataToEntityObject(imageMetadataDO, imgFile);

        // Throws Exception if an object with the key already exists
        if(metadataRepository.existsById(imageMetadataDO.getCustRspaceImageVersion())){
            throw new DuplicateEntryException("");
        }

        metadataRepository.save(imageMetadataDO);
    }


    /**
     * Updates the metadata of the imgFile in the database.
     * The images is identified by the customerID, rspaceImageID and the image Version.
     * If the database does not contain metadata with that key, a new row will be generated.
     * @param imageMetadataDO
     * @param imgFile
     */
    public void updateImageMetadata(ImageMetadataEntity imageMetadataDO, MultipartFile imgFile)
            throws WrongFileFormatException{
        addMetadataToEntityObject(imageMetadataDO, imgFile);

        metadataRepository.save(imageMetadataDO);
    }

    /**
     * Creates a json String from one ImageMetadataEntity Object.
     * If it is not possible to create the json String, the return value will be Optional.empty
     * @param imageMetadataEntity
     * @return jsonString of the ImageMetadataEntity Object or Optional.empty
     */
    private Optional<String> imageMetadataEntityToJson(ImageMetadataEntity imageMetadataEntity){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return Optional.of(mapper.writeValueAsString(imageMetadataEntity));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void deleteImageMetadata(ImageMetadataEmbeddedKey imageKey) throws NoDatabaseEntryFoundException {
        try {
            metadataRepository.deleteById(imageKey);
        } catch (EmptyResultDataAccessException e){
            throw new NoDatabaseEntryFoundException("");
        }
    }

    /**
     * Extractes the metadata from the imgFile and added the extracted metadata as json to the imageMetadata Data Object
     * @param imageMetadataDO ImageMetadata Data Object
     * @param imgFile Image File
     * @return
     * @throws WrongFileFormatException Thrown if the Metadata can not be extracted from the image file (Assumes, that the format is wrong)
     */
    private ImageMetadataEntity addMetadataToEntityObject(ImageMetadataEntity imageMetadataDO, MultipartFile imgFile)
            throws WrongFileFormatException{
        try {
            ImageMetadata metadata = Imaging.getMetadata(imgFile.getInputStream(), imgFile.getName());
            String jsonMetadata = ImageMetadataParser.parseToJson(metadata);

            imageMetadataDO.setJsonMetadata(jsonMetadata);

        } catch (IOException ioex) {
            ioex.printStackTrace();
        } catch (ImageReadException e) {
            // This should only thrown if the data is from the wrong file format
            throw new WrongFileFormatException("");
        }

        return imageMetadataDO;
    }

    public class WrongFileFormatException extends Exception {
        public WrongFileFormatException(String message) {
            super(message);
        }
    }

    public class DuplicateEntryException extends Exception{
        public DuplicateEntryException(String message) {
            super(message);
        }
    }

    public class NoDatabaseEntryFoundException extends  Exception {
        public NoDatabaseEntryFoundException(String message) {
            super(message);
        }
    }


}
