package com.example.authservice.repository;

import com.example.authservice.entity.Permission;
import com.example.authservice.entity.Role;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PermissionRepository extends ReactiveCrudRepository<Permission, Long> {

}
