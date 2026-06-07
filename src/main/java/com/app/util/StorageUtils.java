package com.app.util;

public class StorageUtils {
    private static final String STORAGE_URL_PUBLIC = "/storage/public/";
    public static String generateURLPublic(String pathFile) {
        if(pathFile != null) {
            return STORAGE_URL_PUBLIC + pathFile;
        }
        return null;
    }
}
