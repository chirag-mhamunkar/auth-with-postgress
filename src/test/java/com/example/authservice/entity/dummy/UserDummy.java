package com.example.authservice.entity.dummy;

import com.example.authservice.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDummy {

    public static User create(){
        return new User("1234567", "client", "tenant");
    }
}
