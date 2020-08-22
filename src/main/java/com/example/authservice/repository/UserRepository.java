package com.example.authservice.repository;

import com.example.authservice.entity.Role;
import com.example.authservice.entity.User;
import com.example.authservice.entity.UserRole;
import io.r2dbc.spi.Row;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ResultProcessor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.ResultSet;
import java.util.List;

public interface UserRepository extends ReactiveCrudRepository<User, Long>, CustomUserRepository{

    Flux<User> findByUserIdIn(List<String> userIds);
    Mono<User> findByUserIdAndClientAndTenant(String userId, String client, String tenant);

    //@Query("SELECT u.userid, r.key FROM auth_user u JOIN user_role_mapping urm ON u.id = urm.user_id JOIN auth_role r ON r.id = urm.role_id JOIN role_permission_mapping rpm ON r.id = rpm.role_id JOIN auth_permission p ON p.id = rpm.permission_id WHERE u.id = :id")
    //Flux<UserRole> findFullUser(long id);

}
