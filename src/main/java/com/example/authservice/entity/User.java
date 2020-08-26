package com.example.authservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
@Table(value = "auth_user")
public class User {

    @Id
    private Long id;

    @Column("userId")
    private String userId;

    @Column("client")
    private String client;

    @Column("tenant")
    private String tenant;

    @Column("createdAt")
    private LocalDateTime createdAt;

    @Column("updatedAt")
    private LocalDateTime updatedAt;

    public User(String userId, String client, String tenant){
        this(null, userId, client, tenant, LocalDateTime.now(), LocalDateTime.now());
    }

}
