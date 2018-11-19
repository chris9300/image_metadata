package com.rspace.rspaceimgmetadata.microservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rspace.rspaceimgmetadata.microservice.model.ImageMetadataEmbeddedKey;
import com.rspace.rspaceimgmetadata.microservice.model.ImageMetadataEntity;
import com.rspace.rspaceimgmetadata.microservice.util.FileTypeChecker;
import com.rspace.rspaceimgmetadata.microservice.repository.ImageMetadataRepository;
import com.rspace.rspaceimgmetadata.microservice.util.excpetions.*;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.IImageMetadata;
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


    /**
     * Returns an json object for one image containing the custId, rspaceImageId, version, userId and all metadata
     * @param custId
     * @param rspaceImageId
     * @param version
     * @return
     */
    public Optional<String> getImageData(String custId, long rspaceImageId, int version){
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
     * @exception WrongOrCorruptFileException Thrown if the Metadata can not be extracted from the image file (Assumes, that the format is wrong)
     * @exception DuplicateEntryException Thrown if the key if the new ImageMetadataEntity already exists
     */
    public void insertNewImageMetadata(ImageMetadataEntity imageMetadataDO, MultipartFile imgFile)
            throws WrongOrCorruptFileException, DuplicateEntryException {
        String jsonMetadata = extractJsonMetadataFromFile(imgFile);
        imageMetadataDO.setJsonMetadata(jsonMetadata);

        // Throws Exception if an object with the key already exists
        if(metadataRepository.existsById(imageMetadataDO.getEmbeddedKey())){
            throw new DuplicateEntryException("An object with the key is already in the database.", null);
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
            throws WrongOrCorruptFileException {
        String jsonMetadata = extractJsonMetadataFromFile(imgFile);
        imageMetadataDO.setJsonMetadata(jsonMetadata);

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
            throw new NoDatabaseEntryFoundException("No Object with that key was found in the database", null);
        }
    }


    /**
     * Extracts the metadata from a standard image file (see FileTypeChecker) and returns them as json String
     * @param stdImgFile
     * @return Json String of metadata
     * @throws WrongOrCorruptFileException Thrown if no supported file format was detected
     */
    private String jsonMetadataFromStandardFile(MultipartFile stdImgFile)
        throws WrongOrCorruptFileException {
            try {
                IImageMetadata metadata = Imaging.getMetadata(stdImgFile.getInputStream(), stdImgFile.getName());
                String jsonMetadata = ImageMetadataParser.parseToJson(metadata);

                return jsonMetadata;
            } catch (ImageReadException | IOException e) {
                e.printStackTrace();
                // This should only thrown if the data is from the wrong file format
                throw new WrongOrCorruptFileException("Could not extract the metadata. Probably the file is corrupted or perhaps the file has the wrong file format", e);
            }
    }

    private String jsonMetadataFromProprietaryFile(MultipartFile propImgFile) throws WrongOrCorruptFileException {
        if(FileTypeChecker.isCziFile(propImgFile)){
            try {
                CziFile cziFile = new CziFile(propImgFile);
                return CziMetadataParser.toJson(cziFile);
            } catch (NoValidCziFileException e) {
                e.printStackTrace();
                throw new WrongOrCorruptFileException("Could not extract the metadata. Probably the file is corrupted or perhaps the file has the wrong file format", e);
            }
        }
        throw new WrongOrCorruptFileException("Not valid file format detected. Perhaps the file format is not supported. Or the czi file is corrupted", null);
    }

    private String extractJsonMetadataFromFile(MultipartFile file) throws WrongOrCorruptFileException {
        if (FileTypeChecker.isSupportedStandardFile(file)){
            return jsonMetadataFromStandardFile(file);
        }

        if(FileTypeChecker.isSupportedProprietaryFile(file)){
            return jsonMetadataFromProprietaryFile(file);
        }

        throw new WrongOrCorruptFileException("Not valid file format detected. The file format is not supported.", null);
    }


}
