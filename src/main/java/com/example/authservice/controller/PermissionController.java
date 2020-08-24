package com.example.authservice.controller;

import com.example.authservice.entity.Permission;
import com.example.authservice.exception.WebExceptionHandler;
import com.example.authservice.repository.PermissionRepository;
import com.example.authservice.service.PermissionService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/permission")
public class PermissionController extends WebExceptionHandler {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PermissionService permissionService;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static class PermissionRequest{
        private List<PermissionDTO> permissions;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static class PermissionDTO{
        private String key;
        private String name;
        private boolean active;

        public Permission toEntity(){
            return new Permission(key, name, active);
        }
    }

    @PostMapping
    public Flux<Permission> addPermissions(@RequestBody PermissionRequest rq){
        //TODO check for duplicate KEYS in request
        List<Permission> permissions = rq.getPermissions().stream().map(PermissionDTO::toEntity).collect(Collectors.toList());
        return permissionService.saveAll(permissions);
    }

    @GetMapping
    public Flux<Permission> fetchAll(){
        return permissionService.findAll();
    }
}
