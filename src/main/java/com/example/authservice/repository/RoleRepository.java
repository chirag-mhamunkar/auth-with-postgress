package com.example.authservice.repository;

import com.example.authservice.entity.Role;
import com.example.authservice.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RoleRepository  extends ReactiveCrudRepository<Role, Long> {

    Mono<Role> findByKey(String key);
    Flux<Role> findByKeyIn(List<String> keys);
}
