package com.example.authservice.repository;

import com.example.authservice.entity.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    Flux<User> findByUserIdIn(List<String> userIds);

    //@Query(value = "{'userId': ?0, 'client': ?1, 'tenant': ?2}")
    Mono<User> findByUserIdAndClientAndTenant(String userId, String client, String tenant);

}
