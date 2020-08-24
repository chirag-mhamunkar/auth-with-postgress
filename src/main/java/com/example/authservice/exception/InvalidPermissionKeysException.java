package com.example.authservice.exception;

import java.util.List;

public class InvalidPermissionKeysException extends Exception{
    private List<String> keys;
    public InvalidPermissionKeysException(List<String> keys){
        super("Invalid permission keys: " + keys);
    }
}
