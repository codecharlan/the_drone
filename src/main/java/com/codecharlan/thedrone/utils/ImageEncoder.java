package com.codecharlan.thedrone.utils;

import java.util.Base64;
public class ImageEncoder {
    public static String encodeImageToBase64(byte[] imageData) {
        return Base64.getEncoder().encodeToString(imageData);
    }
}
