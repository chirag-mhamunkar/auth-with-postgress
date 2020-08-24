package com.example.authservice.controller;

import com.example.authservice.exception.WebExceptionHandler;
import com.example.authservice.model.AuthPermission;
import com.example.authservice.model.AuthRole;
import com.example.authservice.service.RoleService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController extends WebExceptionHandler {

    @Autowired
    private RoleService roleService;

    @GetMapping("/{id}")
    Mono<AuthRole> findById(@PathVariable("id") long id){
        return roleService.findFullRoleById(id);
    }

    @PostMapping
    public Mono<AuthRole> saveRole(@RequestBody RoleRequestDTO rq){
        //TODO check for unique keys in roles
        return roleService.save(rq.toModel());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class RoleRequest{
        private List<RoleRequestDTO> roles;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class RoleRequestDTO {
        private String key;
        private String name;
        private String tenant;
        private boolean active;

        private List<String> permissionKeys;

        public AuthRole toModel(){
            AuthRole authRole =  new AuthRole(key, name, tenant, active);
            permissionKeys.forEach(pk -> authRole.addPermission(new AuthPermission(pk, null, false)));
            return authRole;
        }
    }
}
