package com.apt.attendancecall;

public class EncoderDecoder {
    String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    String decodeUserEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }

    String getFirstCharCapital(String str) {
        return str.substring(0, 1).toUpperCase() + (str.substring(1));
    }
}
