package com.example.authservice.service;

import com.example.authservice.entity.Permission;
import com.example.authservice.exception.PermissionKeysFoundException;
import com.example.authservice.repository.PermissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    public Flux<Permission> saveAll(List<Permission> permissions){
        log.info("Inserting permissions[{}] in db", permissions.size());
        List<String> keys = permissions.stream().map(Permission::getKey).collect(Collectors.toList());
        return permissionRepository.findByKeyIn(keys)
                .collectList()
                .flatMapMany(dbPermissions -> {
                    if(!dbPermissions.isEmpty())
                        return Mono.error(new PermissionKeysFoundException(dbPermissions.stream().map(Permission::getKey).collect(Collectors.toList())));
                    permissions.forEach(p -> p.setId(null)); //just to ensure we insert fresh entry
                    return permissionRepository.saveAll(permissions);
                })
                ;
    }

    public Flux<Permission> findByKeys(List<String> keys){
        return permissionRepository.findByKeyIn(keys);
    }

    public Flux<Permission> findAll() {
        return permissionRepository.findAll();
    }
}
