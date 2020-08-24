package com.example.authservice.repository;

import com.example.authservice.model.AuthPermission;
import com.example.authservice.model.AuthRole;
import com.example.authservice.model.AuthUser;
import io.r2dbc.spi.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Repository
public class CustomRoleRepositoryImpl implements CustomRoleRepository{

    public static final String JOIN_ROLE_PERMISSION_QUERY = "SELECT " +
            " r.id as role_id, r.key as role_key, r.name as role_name, r.tenant as role_tenant, r.active as role_active, r.createdat as role_created_at, r.updatedat as role_updated_at" +
            ", p.id as permission_id, p.key as permission_key, p.name as permission_name, p.active as permission_active, p.createdat as permission_created_at, p.updatedat as permission_updated_at" +
            " FROM auth_role r JOIN role_permission_mapping rpm ON r.id = rpm.role_id" +
            " JOIN auth_permission p ON p.id = rpm.permission_id" +
            " WHERE r.id = :id";


    @Autowired
    private DatabaseClient databaseClient;

    @Override
    public Mono<AuthRole> getRole(long id) {

        AtomicReference<AuthUser> uar = new AtomicReference<>();
        return databaseClient.execute(JOIN_ROLE_PERMISSION_QUERY)
                .bind("id", id)
                .map((row, o) -> AuthRolePermissionJoinPlain.get(row))
                .all()
                .collectList()
                .map(this::convert)
                ;
    }

    private AuthRole convert(List<AuthRolePermissionJoinPlain> authUserJoinPlains){
        if(Objects.isNull(authUserJoinPlains) || authUserJoinPlains.isEmpty()) return null;

        List<AuthPermission> permissions = authUserJoinPlains.stream().map(AuthRolePermissionJoinPlain::toPermission).collect(Collectors.toList());

        AuthRole authRole = authUserJoinPlains.get(0).toRole();
        authRole.setPermissions(permissions);
        return authRole;
    }



    static class AuthRolePermissionJoinPlain {
        Long roleId;
        String roleKey;
        String roleName;
        String roleTenant;
        boolean roleActive;
        LocalDateTime roleCreatedAt;
        LocalDateTime roleUpdatedAt;

        Long permissionId;
        String permissionKey;
        String permissionName;
        boolean permissionActive;
        LocalDateTime permissionCreateAt;
        LocalDateTime permissionUpdatedAt;

        public AuthRole toRole(){
            AuthRole role = new AuthRole();
            role.setId(roleId);
            role.setKey(roleKey);
            role.setName(roleName);
            role.setTenant(roleTenant);
            role.setActive(roleActive);
            role.setCreatedAt(roleCreatedAt);
            role.setUpdatedAt(roleUpdatedAt);
            return role;
        }

        public AuthPermission toPermission(){
            AuthPermission pm = new AuthPermission();
            pm.setId(permissionId);
            pm.setKey(permissionKey);
            pm.setName(permissionName);
            pm.setActive(permissionActive);
            pm.setCreatedAt(permissionCreateAt);
            pm.setUpdatedAt(permissionUpdatedAt);
            return pm;
        }

        public static AuthRolePermissionJoinPlain get(Row row){
            AuthRolePermissionJoinPlain obj = new AuthRolePermissionJoinPlain();
            populateRoleColumns(row, obj);
            populatePermissionColumns(row, obj);
            return obj;
        }

        private static void populatePermissionColumns(Row row, AuthRolePermissionJoinPlain obj) {
            obj.permissionId = Long.valueOf(row.get("permission_id", Integer.class));
            obj.permissionKey = row.get("permission_key", String.class);
            obj.permissionName = row.get("permission_name", String.class);
            obj.permissionActive = row.get("permission_active", Boolean.class);
            obj.permissionCreateAt = row.get("permission_created_at", LocalDateTime.class);
            obj.permissionUpdatedAt = row.get("permission_updated_at", LocalDateTime.class);
        }

        private static void populateRoleColumns(Row row, AuthRolePermissionJoinPlain obj) {
            obj.roleId = Long.valueOf(row.get("role_id", Integer.class)); //row is storing id as Integer
            obj.roleKey = row.get("role_key", String.class);
            obj.roleName = row.get("role_name", String.class);
            obj.roleTenant = row.get("role_tenant", String.class);
            obj.roleActive = row.get("role_active", Boolean.class);
            obj.roleCreatedAt = row.get("role_created_at", LocalDateTime.class);
            obj.roleUpdatedAt = row.get("role_updated_at", LocalDateTime.class);
        }
    }
}
