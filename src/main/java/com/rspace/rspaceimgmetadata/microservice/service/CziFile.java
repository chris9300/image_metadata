package com.rspace.rspaceimgmetadata.microservice.service;

import com.rspace.rspaceimgmetadata.microservice.util.excpetions.NoValidCziFileException;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Arrays;

public class CziFile {
    final static String SEGMENT_ID_FILE = "ZISRAWFILE";
    final static String SEGMENT_ID_METADATA = "ZISRAWMETADATA";

    byte[] fileBytes;

    /**
     * Creates a new CziFile from an inputStream
     * @param inputStream
     * @throws NoValidCziFileException
     */
    public CziFile(InputStream inputStream) throws NoValidCziFileException {
        readInputStream(inputStream);
    }

    /**
     * Creates a new CziFile from an inputStream
     * @param file Multipart czi file
     * @throws NoValidCziFileException
     */
    public CziFile(MultipartFile file) throws NoValidCziFileException {
        readMultipartFile(file);
    }

    /**
     * Creates a new CzuFile from a file on the disk (in the classpath)
     * @param filename
     * @throws NoValidCziFileException
     */
    public CziFile(String filename) throws NoValidCziFileException, FileNotFoundException {
        File file = new File(filename);
        readFile(file);
    }

    /**
     * Creates new CziFile from a File object
     * @param file
     * @throws NoValidCziFileException
     */
    public CziFile(File file) throws NoValidCziFileException, FileNotFoundException {
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
