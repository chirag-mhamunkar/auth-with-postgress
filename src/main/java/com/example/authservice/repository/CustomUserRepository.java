package com.example.authservice.repository;

import com.example.authservice.entity.UserRole;
import com.example.authservice.model.AuthUser;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomUserRepository {
    Flux<UserRole> getUserRole(long id);
    Mono<AuthUser> getUser(long id);
}
