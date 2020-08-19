package com.example.authservice.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
@Table(value = "role_users__user_roles")
public class UserRoleMapping {

    @Id
    private Long id;

    @Column("user_roles")
    private Long userId;

    @Column("role_users")
    private Long roleId;

    public UserRoleMapping(Long userId, Long roleId){
        this(null, userId, roleId);
    }

}
