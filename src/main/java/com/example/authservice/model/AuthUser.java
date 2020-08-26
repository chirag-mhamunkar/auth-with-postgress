package com.example.authservice.model;

import com.example.authservice.entity.User;
import lombok.AllArgsConstructor;
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

    private AuthUser(long id){
        this.setId(id);
    }

    public AuthUser(User user){
        super(user.getId(), user.getUserId(), user.getClient(), user.getTenant(), user.getCreatedAt(), user.getUpdatedAt());
    }

    public User toEntity(){
        return new User(getUserId(), getClient(), getTenant());
    }


    //below are some handy methods
    public static AuthUser from(String userId, String client, String tenant){
        return new AuthUser(userId, client, tenant);
    }

    public static AuthUser from(long id){
        return new AuthUser(id);
    }
}
