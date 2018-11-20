package com.rspace.rspaceimgmetadata.microservice.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.JsonParserDelegate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.rspace.rspaceimgmetadata.microservice.util.excpetions.CouldNotParseCZIMetadataException;
import com.rspace.rspaceimgmetadata.microservice.util.excpetions.CziMetadataSegmentNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Map;

import static com.rspace.rspaceimgmetadata.microservice.service.CziFile.SEGMENT_ID_METADATA;

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

        return parser.getJsonMetadata();
    }

    public static String toJsonWithLowerKeys(CziFile cziFile) throws CouldNotParseCZIMetadataException{
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
            throw new CouldNotParseCZIMetadataException("An error ocurred by parsing the extracted xml to json. Perhaps the extracted xml is not valid", e);
        }
    }

    /**
     * Extracts the metadata from the bytes of the czi files. See the czi documentation for positions in the byte array.
     * (Consider that numbers are stored as little endian)
     * @throws CziMetadataSegmentNotFoundException
     */
    private void extractXmlMetadata() throws CziMetadataSegmentNotFoundException {

        byte[] fileBytes = cziFile.getFileBytes();

        // Regarding the filetype documentation, the start position of the metadata segment is the byte 92
        // (32 bytes for the fileheader segment header and the 60th byte of the fileheader segment)
        long metadataPosition = ByteBuffer.wrap(fileBytes, 92, 8).order(ByteOrder.LITTLE_ENDIAN).getLong();

        //check if this is really the start of the metadata section:
        if(!checkMetadataPosition(metadataPosition)){
            throw new CziMetadataSegmentNotFoundException("No metadata segment was found. Searched on byte 92. Perhaps the czi file is corrupted", null);
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
        return segmentID.contains(SEGMENT_ID_METADATA);
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

    /**
     * Returns the extracted metadata as json String that only contains lower case letters
     * @return
     */
    private String getJsonMetadataAsLowerCase() throws CouldNotParseCZIMetadataException {
        try {
            return lowerParser();
        } catch (IOException e) {
            e.printStackTrace();
            throw new CouldNotParseCZIMetadataException("Could not parse the normal json String to json String with lower cases", e);
        }
    }


    //////////////////////////////// copied form:
    // https://stackoverflow.com/questions/18838095/how-do-i-parse-json-into-a-map-with-lowercase-keys-using-jackson
    ///////////////////////////////
    private String lowerParser() throws IOException {
        @SuppressWarnings("serial")
        ObjectMapper mapper = new ObjectMapper(new JsonFactory() {
            @Override
            protected JsonParser _createParser(byte[] data, int offset, int len, IOContext ctxt) throws IOException {
                return new DowncasingParser(super._createParser(data, offset, len, ctxt));
            }

            @Override
            protected JsonParser _createParser(InputStream in, IOContext ctxt) throws IOException {
                return new DowncasingParser(super._createParser(in, ctxt));
            }

            @Override
            protected JsonParser _createParser(Reader r, IOContext ctxt) throws IOException {
                return new DowncasingParser(super._createParser(r, ctxt));
            }

            @Override
            protected JsonParser _createParser(char[] data, int offset, int len, IOContext ctxt, boolean recyclable)
                    throws IOException {
                return new DowncasingParser(super._createParser(data, offset, len, ctxt, recyclable));
            }
        });

        Map<String, ?> val = mapper.reader(Map.class)
                .with(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
                .with(JsonParser.Feature.ALLOW_SINGLE_QUOTES)
                .readValue(jsonMetadata);//readValue("{CustName:'Jimmy Smith', CustNo:'1234', Details:{PhoneNumber:'555-5555',Result:'foo'} } }");


        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(val);

    }


    private static final class DowncasingParser extends JsonParserDelegate {
        private DowncasingParser(JsonParser d) {
            super(d);
        }

        @Override
        public String getCurrentName() throws IOException, JsonParseException {
            if (hasTokenId(JsonTokenId.ID_FIELD_NAME)) {
                return delegate.getCurrentName().toLowerCase();
            }
            return delegate.getCurrentName();
        }

        @Override
        public String getText() throws IOException, JsonParseException {
            if (hasTokenId(JsonTokenId.ID_FIELD_NAME)) {
                return delegate.getText().toLowerCase();
            }
            return delegate.getText();
        }
    }

}
