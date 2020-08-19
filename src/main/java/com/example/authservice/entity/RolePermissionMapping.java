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
@Table(value = "permission_roles__role_permissions")
public class RolePermissionMapping {

    @Id
    private Long id;

    @Column("role_permissions")
    private Long roleId;

    @Column("permission_roles")
    private Long permissionId;

    public RolePermissionMapping(Long roleId, Long permissionId){
        this(null, roleId, permissionId);
    }
}
