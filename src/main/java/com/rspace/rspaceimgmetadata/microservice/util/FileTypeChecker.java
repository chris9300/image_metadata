package com.rspace.rspaceimgmetadata.microservice.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class FileTypeChecker {
    final static String CZI_EXTENSION = "czi";


    final static String[] VALID_STANDARD_FILE_TYPES = {"image/jpg", "image/jpeg", "image/png", "image/tif", "image/tiff"};
    final static String[] VALID_PROPRIETARY_FILE_TYPES = {"application/octet-stream"};
    final static String[] VALID_PROPRIETARY_FILE_EXTENSION = {CZI_EXTENSION};

    /**
     * Check if the file is a supported standard filetype:
     * - image/jpg
     * - image/jpeg
     * - image/png
     * - image/tif
     * - image/tiff
     *
     * @param file
     * @return true if supported standarf file type
     */
    public static boolean isSupportedStandardFile(MultipartFile file){
        return Arrays.asList(VALID_STANDARD_FILE_TYPES).contains(file.getContentType());
    }

    /**
     * Checks if the proprietary file has a valid (supported) extension
     * - czi
     * @param file
     * @return true if the file is a supported proprietary file
     */
    public static boolean isSupportedProprietaryFile(MultipartFile file){
        // First check the filetype
        if(Arrays.asList(VALID_PROPRIETARY_FILE_TYPES).contains(file.getContentType())) {

            // If valid filetype, check file extension
            String fileExtension = getFileExtension(file);
            return Arrays.asList(VALID_PROPRIETARY_FILE_EXTENSION).contains(fileExtension);
        } else {
            return false;
        }
    }

    /**
     * Checks if a file is a czi (carl zeiss image) file
     * @param file
     * @return
     */
    public static boolean isCziFile(MultipartFile file){
        // First check the filetype
        if(Arrays.asList(VALID_PROPRIETARY_FILE_TYPES).contains(file.getContentType())) {

            // If valid filetype, check that the extension is czi
            String fileExtension = getFileExtension(file);
            return fileExtension.equals(CZI_EXTENSION);
        } else {
            return false;
        }
    }

    /**
     * Extracts the file extensaion (the substring after the last dot)
     * @param file
     * @return
     */
    private static String getFileExtension(MultipartFile file){
        String fileName = file.getOriginalFilename();
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i+1);
        }

        return extension;
    }
}
