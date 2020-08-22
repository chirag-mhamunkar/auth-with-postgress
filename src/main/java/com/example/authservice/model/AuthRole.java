package com.example.authservice.model;

import com.example.authservice.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthRole extends Role {
    private List<AuthPermission> permissions;

    public void addPermission(AuthPermission pm){
        if(Objects.isNull(permissions)) permissions = new ArrayList<>();
        permissions.add(pm);
    }
}
