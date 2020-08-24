package com.example.authservice.exception;

import java.util.List;

public class KeysFoundException extends Exception {

    private List<String> keys;

    public KeysFoundException(List<String> keys){
        super(prepareMessage(keys));
        this.keys = keys;
    }

    private static String prepareMessage(List<String> keys){
        return "Keys found in DB: " + keys;
    }
}
