package com.example.authservice.repository;

import com.example.authservice.entity.UserRole;
import com.example.authservice.model.AuthPermission;
import com.example.authservice.model.AuthRole;
import com.example.authservice.model.AuthUser;
import io.r2dbc.spi.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Repository
public class CustomUserRepositoryImpl implements CustomUserRepository{

    private static final String JOIN_USER_ROLE_PERMISSION_QUERY = "SELECT " +
            "u.id as user_id, u.client as user_client, u.userid as user_partner_id, u.tenant as user_tenant, u.createdat as user_created_at, u.updatedat as user_updated_at" +
            ", r.id as role_id, r.key as role_key, r.name as role_name, r.tenant as role_tenant, r.active as role_active, r.createdat as role_created_at, r.updatedat as role_updated_at" +
            ", p.id as permission_id, p.key as permission_key, p.name as permission_name, p.active as permission_active, p.createdat as permission_created_at, p.updatedat as permission_updated_at" +
            " FROM auth_user u JOIN user_role_mapping urm ON u.id = urm.user_id" +
            " JOIN auth_role r ON r.id = urm.role_id" +
            " JOIN role_permission_mapping rpm ON r.id = rpm.role_id" +
            " JOIN auth_permission p ON p.id = rpm.permission_id" +
            " WHERE u.id = :id";

    @Autowired
    private DatabaseClient databaseClient;


    @Override
    public Flux<UserRole> getUserRole(long id) {
        String joinQuery = "SELECT u.userId, r.key FROM auth_user u JOIN user_role_mapping urm ON u.id = urm.user_id JOIN auth_role r ON r.id = urm.role_id JOIN role_permission_mapping rpm ON r.id = rpm.role_id JOIN auth_permission p ON p.id = rpm.permission_id WHERE u.id = :id";
        return databaseClient.execute(joinQuery)
                .bind("id", id)
                .map( (row, o) -> {
                    return new UserRole(row.get("userId", String.class), row.get("key", String.class));
                })
                .all();
    }

    @Override
    public Mono<AuthUser> getUser(long id) {

        AtomicReference<AuthUser> uar = new AtomicReference<>();
        return databaseClient.execute(JOIN_USER_ROLE_PERMISSION_QUERY)
                .bind("id", id)
                .map((row, o) -> AuthUserJoinPlain.get(row))
                .all()
                .collectList()
                .map(this::convert)
        ;
    }

    private AuthUser convert(List<AuthUserJoinPlain> authUserJoinPlains){
        if(Objects.isNull(authUserJoinPlains) || authUserJoinPlains.isEmpty()) return null;

        Map<Long, AuthRole> roleMap = authUserJoinPlains.stream().collect(Collectors.toMap(a -> a.roleId, a -> a.toRole()));

        authUserJoinPlains.forEach(a -> roleMap.get(a.roleId).addPermission(a.toPermission()));

        AuthUser user = authUserJoinPlains.get(0).toUser();
        user.setRoles(roleMap.values().stream().collect(Collectors.toList()));

        return user;
    }

    static class AuthUserJoinPlain{
        Long userId;
        String userPartnerId;
        String userClient;
        String userTenant;
        LocalDateTime userCreatedAt;
        LocalDateTime userUpdatedAt;

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

        public AuthUser toUser(){
            AuthUser user = new AuthUser();
            user.setId(userId);
            user.setUserId(userPartnerId);
            user.setTenant(userTenant);
            user.setClient(userClient);
            user.setCreatedAt(userCreatedAt);
            user.setUpdatedAt(userUpdatedAt);
            return user;
        }

        public static AuthUserJoinPlain get(Row row){
            AuthUserJoinPlain obj = new AuthUserJoinPlain();
            populateUserColumns(row, obj);
            populateRoleColumns(row, obj);
            populatePermissionColumns(row, obj);
            return obj;
        }

        private static void populatePermissionColumns(Row row, AuthUserJoinPlain obj) {
            obj.permissionId = Long.valueOf(row.get("permission_id", Integer.class));
            obj.permissionKey = row.get("permission_key", String.class);
            obj.permissionName = row.get("permission_name", String.class);
            obj.permissionActive = row.get("permission_active", Boolean.class);
            obj.permissionCreateAt = row.get("permission_created_at", LocalDateTime.class);
            obj.permissionUpdatedAt = row.get("permission_updated_at", LocalDateTime.class);
        }

        private static void populateRoleColumns(Row row, AuthUserJoinPlain obj) {
            obj.roleId = Long.valueOf(row.get("role_id", Integer.class)); //row is storing id as Integer
            obj.roleKey = row.get("role_key", String.class);
            obj.roleName = row.get("role_name", String.class);
            obj.roleTenant = row.get("role_tenant", String.class);
            obj.roleActive = row.get("role_active", Boolean.class);
            obj.roleCreatedAt = row.get("role_created_at", LocalDateTime.class);
            obj.roleUpdatedAt = row.get("role_updated_at", LocalDateTime.class);
        }

        private static void populateUserColumns(Row row, AuthUserJoinPlain obj) {
            obj.userId = Long.valueOf(row.get("user_id", Integer.class));
            obj.userPartnerId = row.get("user_partner_id", String.class);
            obj.userClient = row.get("user_client", String.class);
            obj.userTenant = row.get("user_tenant", String.class);
            obj.userCreatedAt = row.get("user_created_at", LocalDateTime.class);
            obj.userUpdatedAt = row.get("user_updated_at", LocalDateTime.class);
        }
    }
}
