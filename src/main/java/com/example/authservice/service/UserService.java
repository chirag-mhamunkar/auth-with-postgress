package com.example.authservice.service;

import com.example.authservice.entity.User;
import com.example.authservice.entity.UserRoleMapping;
import com.example.authservice.exception.InvalidRoleKeysException;
import com.example.authservice.exception.UserNotFoundException;
import com.example.authservice.model.AuthUser;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.repository.UserRoleMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleMappingRepository userRoleMappingRepository;

    public Mono<AuthUser> save(AuthUser au){

        //if we create unique index on these three attrs then we can directly insert entry
        // and catch exception if entry already exists
        return findUser(au)
                .switchIfEmpty(userRepository.save(au.toEntity()).map(AuthUser::new))
                .map(dbUser -> new AuthUser(dbUser));
    }

    public Flux<UserRoleMapping> assignRolesToUser(AuthUser au, List<String> roleKeys){
        return findUser(au)
                .switchIfEmpty(Mono.error(new UserNotFoundException(au)))
                .flatMapMany(dbUser -> assignRolesToUser(dbUser, roleKeys));

    }

    public Flux<UserRoleMapping> assignRolesToUserId(long id, List<String> roleKeys){
        return findUser(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException(AuthUser.from(id))))
                .flatMapMany(dbUser -> assignRolesToUser(dbUser, roleKeys));

    }

    //Note: This should be private method and NOT public
    public Flux<UserRoleMapping> assignRolesToUser(User dbUser, List<String> roleKeys){
        return roleService.findByRoleKeys(roleKeys)
                .switchIfEmpty(Flux.error(new InvalidRoleKeysException(roleKeys)))
                .map(r -> new UserRoleMapping(dbUser.getId(), r.getId()))
                .collectList()
                .flatMapMany(userRoleMappingRepository :: saveAll)
                ;
    }

    private Mono<User> findUser(AuthUser au){
        return userRepository.findByUserIdAndClientAndTenant(au.getUserId(), au.getClient(), au.getTenant());
    }

    private Mono<User> findUser(long id){
        return userRepository.findById(id);
    }
}
