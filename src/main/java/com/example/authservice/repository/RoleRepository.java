package com.example.authservice.repository;

import com.example.authservice.entity.Role;
import com.example.authservice.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface RoleRepository  extends ReactiveCrudRepository<Role, Long> {
}
