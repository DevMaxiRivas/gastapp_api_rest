package com.app.util;

public class MaskingUtils {

    public static String maskEmail(String email) {
        // Regex: Match 1st char, then middle chars, then last char before '@', then domain
        return email.replaceAll("(?<=.{1}).(?=.*@)", "*");
    }
}