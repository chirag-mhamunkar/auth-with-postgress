package com.example.authservice.model;

import com.example.authservice.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthUser extends User {

    private List<AuthRole> roles;

    public AuthUser(String userId, String client, String tenant){
        super(userId, client, tenant);
    }

    public AuthUser(User user){
        super(user.getId(), user.getUserId(), user.getClient(), user.getTenant(), user.getCreatedAt(), user.getUpdatedAt());
    }

    public User toEntity(){
        return new User(getUserId(), getClient(), getTenant());
    }
}
