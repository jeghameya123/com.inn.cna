package com.inn.cna.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CnaUtils {

    private CnaUtils(){

    }
    public static ResponseEntity<String> getResponseEntity(String responseMessage , HttpStatus httpStatus) {
        return new ResponseEntity<String>("{\"message\":\""+responseMessage+"\"}",httpStatus);
    }

}
