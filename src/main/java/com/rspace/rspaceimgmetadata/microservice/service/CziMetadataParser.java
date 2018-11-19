package com.rspace.rspaceimgmetadata.microservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import static com.rspace.rspaceimgmetadata.microservice.service.CziFile.SEGMENTID_METADATA;

/**
 * Provides functionalities to (extract and) parse the metadata of CziFiles to json or xml
 */
public class CziMetadataParser {
    CziFile cziFile;

    private String xmlMetadata;
    private String jsonMetadata;


    public CziMetadataParser(CziFile cziFile) {
        this.cziFile = cziFile;
    }

    /**
     * Returns the metadata of the czi file as json String
     * @param cziFile
     * @return
     * @throws CouldNotParseCZIMetadataException
     */
    public static String toJson(CziFile cziFile) throws CouldNotParseCZIMetadataException {
        CziMetadataParser parser = new CziMetadataParser(cziFile);
        parser.extractXmlMetadata();
        parser.createJsonMetadata();

        return parser.getJsonMetadataAsLowerCase();
    }

    /**
     * Returns the metadata of the czi file as xml String
     * @param cziFile
     * @return
     * @throws CouldNotParseCZIMetadataException
     */
    public static String toXml(CziFile cziFile) throws CouldNotParseCZIMetadataException {
        CziMetadataParser parser = new CziMetadataParser(cziFile);
        parser.extractXmlMetadata();
        parser.createJsonMetadata();

        return parser.getXmlMetadata();
    }


    /**
     * Parses the xml Metadata to json metadata (as String)
     * @throws CouldNotParseCZIMetadataException
     */
    private void createJsonMetadata() throws CouldNotParseCZIMetadataException {
        XmlMapper xmlMapper = new XmlMapper();
        JsonNode json = null;
        try {
            json = xmlMapper.readTree(xmlMetadata.getBytes());
            ObjectMapper jsonMapper = new ObjectMapper();
            jsonMetadata = jsonMapper.writeValueAsString(json);

        } catch (IOException e) {
            e.printStackTrace();
            throw new CouldNotParseCZIMetadataException("An error ocurred by parsing the extracted xml to json. Perhaps the extracted xml is not valid");
        }
    }

    /**
     * Extracts the metadata from the bytes of the czi files. See the czi documentation for positions in the byte array.
     * (Consider that numbers are stored as little endian)
     * @throws MetadataSegmentNotFoundException
     */
    private void extractXmlMetadata() throws MetadataSegmentNotFoundException {

        byte[] fileBytes = cziFile.getFileBytes();

        // Regarding the filetype documentation, the start position of the metadata segment is the byte 92
        // (32 bytes for the fileheader segment header and the 60th byte of the fileheader segment)
        long metadataPosition = ByteBuffer.wrap(fileBytes, 92, 8).order(ByteOrder.LITTLE_ENDIAN).getLong();

        //check if this is really the start of the metadata section:
        if(!checkMetadataPosition(metadataPosition)){
            throw new MetadataSegmentNotFoundException("No metadata segment was found. Searched on byte 92. Perhaps the czi file is corrupted");
        }

        // Regarding the filetype doc, the xmlSize is a int, starting at the 16th byte of the metadata segment header
        int xmlSize = ByteBuffer.wrap(fileBytes, (int) metadataPosition + 32, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();

        // Regarding the filetype doc, the xmlStarts in the XML segment after the header part, which contains 256 bytes (and the segment Header which contains 32 bytes)
        int xmlStart = (int) metadataPosition + 32 + 256;

        try {
            xmlMetadata = new String(Arrays.copyOfRange(fileBytes, xmlStart, xmlStart+xmlSize), "utf-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    /**
     * Check if on the given position the metadata section started
     * @param metadataPosition
     * @return
     */
    private boolean checkMetadataPosition(long metadataPosition){
        String segmentID;
        try {
            segmentID = new String(Arrays.copyOfRange(cziFile.getFileBytes(), (int) metadataPosition, (int) metadataPosition + 15), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
        return segmentID.contains(SEGMENTID_METADATA);
    }

    /**
     * Returns the extracted xml metadata as (xml) String
     * @return
     */
    private String getXmlMetadata() {
        return xmlMetadata;
    }

    /**
     * Returns the extracted metedata as json String
     * @return
     */
    private String getJsonMetadata() {
        return jsonMetadata;
    }

    private String getJsonMetadataAsLowerCase(){
        return jsonMetadata.toLowerCase();
    }

    //todo higher lvl parsing exception
    public class MetadataSegmentNotFoundException extends CouldNotParseCZIMetadataException{
        public MetadataSegmentNotFoundException(String message) {
            super(message);
        }
    }

    public class CouldNotParseCZIMetadataException extends Exception{
        public CouldNotParseCZIMetadataException(String message) {
            super(message);
        }
    }
}
