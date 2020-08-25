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
import java.util.Objects;
import java.util.stream.Collectors;

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
        return userRepository.findByUserIdAndClientAndTenant(au.getUserId(), au.getClient(), au.getTenant())
                .defaultIfEmpty(new AuthUser())
                .flatMap(user -> {
                    if(Objects.nonNull(user.getId())) //found in DB
                        return Mono.just(new AuthUser(user));
                    return userRepository.save(au.toEntity())
                            .map(AuthUser::new);
                });
    }

    /*public Flux<UserRoleMapping> assignRolesToUser(AuthUser authUser, List<String> roleKeys){
        return findUser(authUser)
                .switchIfEmpty(Mono.error(new UserNotFoundException(authUser)))
                .flatMap(dbUser -> )
    }*/

    private Flux<UserRoleMapping> assignRolesToUser(User dbUser, List<String> roleKeys){
        return roleService.findByRoleKeys(roleKeys)
                .collectList()
                .flatMapMany(roles -> {
                    if(roles.isEmpty()) return Mono.error(new InvalidRoleKeysException(roleKeys));
                    List<UserRoleMapping> userRoleMappings =
                            roles.stream().map(r -> new UserRoleMapping(r.getId(), dbUser.getId())).collect(Collectors.toList());
                    return userRoleMappingRepository.saveAll(userRoleMappings);
                });
    }

    private Mono<User> findUser(AuthUser au){
        return userRepository.findByUserIdAndClientAndTenant(au.getUserId(), au.getClient(), au.getTenant());
    }
}
