package com.app.util;

public class StorageUtils {
    private static final String STORAGE_URL_PUBLIC = "/storage/";
    public static String generateURLPublic(String pathFile) {
        return STORAGE_URL_PUBLIC + pathFile;
    }
}
