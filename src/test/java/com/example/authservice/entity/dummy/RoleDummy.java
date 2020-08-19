package com.example.authservice.entity.dummy;

import com.example.authservice.entity.Role;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleDummy {

    public static Role create(){
        return new Role("ROLE_KEY", "ROLE_NAME", "TENANT", true);
    }
}
