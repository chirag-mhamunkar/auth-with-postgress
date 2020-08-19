package com.example.authservice.repository;

import com.example.authservice.entity.UserRoleMapping;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface UserRoleMappingRepository extends ReactiveCrudRepository<UserRoleMapping, Long> {

    Flux<UserRoleMapping> findByUserId(Long userId);
    Flux<UserRoleMapping> findByUserIdIn(List<Long> userId);

}
