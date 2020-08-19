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
@Table(value = "user_role_mapping")
public class UserRoleMapping {

    @Id
    private Long id;

    @Column("user_id")
    private Long userId;

    @Column("role_id")
    private Long roleId;

    public UserRoleMapping(Long userId, Long roleId){
        this(null, userId, roleId);
    }

}
