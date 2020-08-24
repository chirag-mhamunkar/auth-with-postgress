package com.example.authservice.exception;

import java.util.List;

public class RoleKeysFoundException extends KeysFoundException{

    public RoleKeysFoundException(List<String> keys) {
        super(keys);
    }
}
