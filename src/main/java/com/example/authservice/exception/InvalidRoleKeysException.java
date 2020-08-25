package com.example.authservice.exception;

import java.util.List;

public class InvalidRoleKeysException extends Exception{

    private List<String> keys;

    public InvalidRoleKeysException(List<String> keys){
        super("Invalid role keys: " + keys);
    }
}
