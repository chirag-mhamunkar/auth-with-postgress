package com.example.authservice.repository;

import com.example.authservice.entity.Permission;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PermissionRepository extends ReactiveCrudRepository<Permission, Long> {

    Mono<Permission> findByKey(String key);
    Flux<Permission> findByKeyIn(List<String> keys);
}
