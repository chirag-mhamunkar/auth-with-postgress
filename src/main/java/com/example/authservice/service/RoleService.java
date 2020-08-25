package com.example.authservice.service;

import com.example.authservice.entity.Permission;
import com.example.authservice.entity.Role;
import com.example.authservice.entity.RolePermissionMapping;
import com.example.authservice.exception.InvalidPermissionKeysException;
import com.example.authservice.exception.RoleKeysFoundException;
import com.example.authservice.model.AuthPermission;
import com.example.authservice.model.AuthRole;
import com.example.authservice.repository.RolePermissionMappingRepository;
import com.example.authservice.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RolePermissionMappingRepository rolePermissionMappingRepository;

    //TODO what if role with same key present in the DB?
    public Mono<AuthRole> save(AuthRole authRole){
        Role role = authRole.toEntity();
        List<String> permissionKeys = authRole.getPermissions().stream().map(AuthPermission::getKey).collect(Collectors.toList());

        return permissionService.findByKeys(permissionKeys)
                .map(Permission::getId)
                .collectList()
                .flatMap(permissionIds -> {
                    if(permissionIds.isEmpty()) return Mono.error(new InvalidPermissionKeysException(permissionKeys));
                    return roleRepository.save(role)
                            .flatMap(dbRole -> {
                                authRole.setId(dbRole.getId());
                                List<RolePermissionMapping> rolePermissionMappings =
                                        permissionIds.stream().map(pId -> new RolePermissionMapping(dbRole.getId(), pId)).collect(Collectors.toList());
                                return rolePermissionMappingRepository.saveAll(rolePermissionMappings)
                                        .then(Mono.just(authRole));
                            });
                });
    }

    public Flux<Role> saveAll(List<Role>roles){
        log.info("Inserting {} roles in DB", roles.size());
        return roleRepository.findByKeyIn(roles.stream().map(Role::getKey).collect(Collectors.toList()))
                .collectList()
                .flatMapMany(dbRoles -> {
                    if(!dbRoles.isEmpty())
                        return Mono.error(new RoleKeysFoundException(dbRoles.stream().map(Role::getKey).collect(Collectors.toList())));
                    roles.forEach(r -> r.setId(null)); //just to ensure we insert fresh entry
                    return roleRepository.saveAll(roles);
                });
    }

    public Mono<AuthRole> findFullRoleById(long id) {
        return roleRepository.getRole(id);
    }

    public Flux<Role> findByRoleKeys(List<String> roleKeys) {
        return roleRepository.findByKeyIn(roleKeys);
    }
}
