package com.example.authservice.repository;

import com.example.authservice.model.AuthRole;
import reactor.core.publisher.Mono;

public interface CustomRoleRepository {

    Mono<AuthRole> getRole(long id);
}
