package com.example.authservice.repository;

import com.example.authservice.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    Flux<User> findByUserIdIn(List<String> userIds);
    Mono<User> findByUserIdAndClientAndTenant(String userId, String client, String tenant);

}
