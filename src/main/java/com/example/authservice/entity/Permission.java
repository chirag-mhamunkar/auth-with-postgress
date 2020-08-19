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
@Table(value = "auth_permission")
public class Permission {

    @Id
    private Long id;
    private String key;
    private String name;
    private boolean active;

    @Column("createdAt")
    private LocalDateTime createdAt;

    @Column("updatedAt")
    private LocalDateTime updatedAt;

    public Permission(String key, String name, boolean active){
        this(null, key, name, active, LocalDateTime.now(), LocalDateTime.now());
    }

}
