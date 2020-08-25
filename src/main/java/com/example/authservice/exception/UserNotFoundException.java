package com.example.authservice.exception;

import com.example.authservice.model.AuthUser;
import lombok.Data;

@Data
public class UserNotFoundException extends Exception{

    private AuthUser authUser;

    public UserNotFoundException(AuthUser authUser){
        super("User not found: " + authUser);
        this.authUser = authUser;
    }
}
