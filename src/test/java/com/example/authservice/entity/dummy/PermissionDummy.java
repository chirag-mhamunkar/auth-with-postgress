package com.example.authservice.entity.dummy;

import com.example.authservice.entity.Permission;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PermissionDummy {

    public static Permission create(){
        return new Permission("PERMISSION_KEY_1", "PERMISSION_NAME_1", true);
    }
}
