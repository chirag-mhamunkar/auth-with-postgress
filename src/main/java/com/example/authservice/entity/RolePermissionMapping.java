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
@Table(value = "role_permission_mapping")
public class RolePermissionMapping {

    @Id
    private Long id;

    @Column("role_id")
    private Long roleId;

    @Column("permission_id")
    private Long permissionId;

    public RolePermissionMapping(Long roleId, Long permissionId){
        this(null, roleId, permissionId);
    }
}
