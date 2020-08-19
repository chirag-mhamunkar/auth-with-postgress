package com.example.authservice.repository;

import com.example.authservice.entity.RolePermissionMapping;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface RolePermissionMappingRepository extends ReactiveCrudRepository<RolePermissionMapping, Long> {

    Flux<RolePermissionMapping> findByRoleId(Long roleId);
    Flux<RolePermissionMapping> findByRoleIdIn(List<Long> roleIds);
}
