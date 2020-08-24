package com.example.authservice.exception;

import java.util.List;

public class PermissionKeysFoundException  extends  KeysFoundException{

    public PermissionKeysFoundException(List<String> keys) {
        super(keys);
    }
}
