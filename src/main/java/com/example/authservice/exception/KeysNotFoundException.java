package com.example.authservice.exception;

import java.util.List;

public class KeysNotFoundException extends Exception{
    private List<String> keys;

    public KeysNotFoundException(List<String> keys){
        super(prepareMessage(keys));
    }

    private static String prepareMessage(List<String> keys) {
        return "Keys not found in DB: " + keys;
    }
}
