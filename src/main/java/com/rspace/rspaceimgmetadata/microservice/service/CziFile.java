package com.rspace.rspaceimgmetadata.microservice.service;

import java.io.*;
import java.util.Arrays;

public class CziFile {
    final static String SEGMENTID_FILE = "ZISRAWFILE";
    final static String SEGMENTID_METADATA = "ZISRAWMETADATA";

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
     * Creates a new CzuFile from a file on the disk (in the classpath)
     * @param filename
     * @throws NoValidCziFileException
     */
    public CziFile(String filename) throws NoValidCziFileException {
        File file = new File(filename);
        readFile(file);
    }

    /**
     * Creates new CziFile from a File object
     * @param file
     * @throws NoValidCziFileException
     */
    public CziFile(File file) throws NoValidCziFileException {
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
                        "or corrupted file used");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * reads file and returns the inputStream
     * @param file
     */
    private void readFile(File file) throws NoValidCziFileException {
        InputStream inStream = null;

        try{
            inStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        readInputStream(inStream);
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

        return segmentID.contains(SEGMENTID_FILE);
    }

    public class NoValidCziFileException extends Exception{
        public NoValidCziFileException(String message) {
            super(message);
        }
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }
}
