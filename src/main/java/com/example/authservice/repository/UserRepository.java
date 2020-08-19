package com.example.authservice.repository;

import com.example.authservice.entity.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    Flux<User> findByUserIdIn(List<String> userIds);
    Mono<User> findByUserIdAndClientAndTenant(String userId, String client, String tenant);

    @Query("SELECT * FROM auth_user u JOIN user_role_mapping urm ON u.id = urm.user_id JOIN auth_role r ON r.id = urm.role_id JOIN role_permission_mapping rpm ON r.id = rpm.role_id JOIN auth_permission p ON p.id = rpm.permission_id WHERE u.id = :id")
    Mono<User> findFullUser(long id);

}
