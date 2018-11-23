package com.rspace.rspaceimgmetadata.microservice.service;

import com.rspace.rspaceimgmetadata.microservice.util.excpetions.NoValidCziFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Arrays;

public class CziFile {
    final static String SEGMENT_ID_FILE = "ZISRAWFILE";
    final static String SEGMENT_ID_METADATA = "ZISRAWMETADATA";

    byte[] fileBytes;
    Logger logger;

    public CziFile() {
        logger = LoggerFactory.getLogger(CziFile.class);
    }

    /**
     * Creates a new CziFile from an inputStream
     * @param inputStream
     * @throws NoValidCziFileException
     */
    public CziFile(InputStream inputStream) throws NoValidCziFileException {
        this();
        readInputStream(inputStream);
    }

    /**
     * Creates a new CziFile from an inputStream
     * @param file Multipart czi file
     * @throws NoValidCziFileException
     */
    public CziFile(MultipartFile file) throws NoValidCziFileException {
        this();
        readMultipartFile(file);
    }

    /**
     * Creates a new CzuFile from a file on the disk (in the classpath)
     * @param filename
     * @throws NoValidCziFileException
     */
    public CziFile(String filename) throws NoValidCziFileException, FileNotFoundException {
        this();
        File file = new File(filename);
        readFile(file);
    }

    /**
     * Creates new CziFile from a File object
     * @param file
     * @throws NoValidCziFileException
     */
    public CziFile(File file) throws NoValidCziFileException, FileNotFoundException {
        this();
        readFile(file);
    }

    /**
     * Read the input stream and convert it to a byte array if the file is a valid czi file
     * @param fileStram
     * @throws NoValidCziFileException
     */
    private void readInputStream(InputStream fileStram) throws NoValidCziFileException {

        try {
            int filesize = fileStram.available();
            // byte array to store the file
            fileBytes = new byte[filesize];

            fileStram.read(fileBytes);
            fileStram.close();

            if(!checkFileType()){
                // reset content, if file is not valid
                fileBytes = null;

                throw new NoValidCziFileException("Could not found the header of the szi file. Perhaps wrong file format " +
                        "or corrupted file used", null);
            }

        } catch (IOException e) {
            logger.debug("Could not read the czi input file. " + e.getMessage()+ "\n" + e.getStackTrace());
            throw new NoValidCziFileException("Could not read the input File. Perhaps the file is corrupted.", e);
        }
    }

    /**
     * Read a czi data from a file object. Check if it is valid czi and store the bytes in the CziFile
     * @param file
     */
    private void readFile(File file) throws NoValidCziFileException, FileNotFoundException {
        InputStream inStream = new FileInputStream(file);
        readInputStream(inStream);
    }

    /**
     * Read a czi data from a MultipartFile object. Check if it is valid czi and store the bytes in the CziFile
     * @param file
     */
    private void readMultipartFile(MultipartFile file) throws NoValidCziFileException {
        try {
            InputStream inputStream = file.getInputStream();
            readInputStream(inputStream);
        } catch (IOException e) {
            logger.debug("Could not read the czi input file. " + e.getMessage() + "\n" + e.getStackTrace());
            throw new NoValidCziFileException("Could not read the input File. Perhaps the file is corrupted.", e);
        }
    }

    /**
     * Checks if the byte array is a czi File. For this, look for the Fileheader which should contain "ZISRAWFILE"
     * @return
     */
    private boolean checkFileType(){
        String segmentID;
        try {
            segmentID = new String(Arrays.copyOfRange(fileBytes, 0, 15), "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.debug("Could not encode the czi input file. " + e.getMessage() + "\n" + e.getStackTrace());
            return false;
        }

        return segmentID.contains(SEGMENT_ID_FILE);
    }

    /**
     * Returns the byte array of the czi file
     * @return
     */
    public byte[] getFileBytes() {
        return fileBytes;
    }
}
