package com.example.authservice.model;

import com.example.authservice.entity.Permission;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AuthPermission extends Permission {

    public AuthPermission(String key, String name, boolean active){
        super(key, name, active);
    }

    public Permission toEntity() {
        return new Permission(getKey(), getName(), isActive());
    }
}
