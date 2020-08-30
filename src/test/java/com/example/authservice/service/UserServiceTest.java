package com.example.authservice.service;


import com.example.authservice.configuration.DBConfiguration;
import com.example.authservice.entity.Role;
import com.example.authservice.entity.User;
import com.example.authservice.entity.UserRoleMapping;
import com.example.authservice.exception.InvalidRoleKeysException;
import com.example.authservice.exception.UserNotFoundException;
import com.example.authservice.model.AuthUser;
import com.example.authservice.repository.RoleRepository;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.repository.UserRoleMappingRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
@Import(DBConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRoleMappingRepository userRoleMappingRepository;

    @BeforeEach
    public void cleanUp() {

        log.info("#########################");
        userRoleMappingRepository.deleteAll().block();
        userRepository.deleteAll().block();
        roleRepository.deleteAll().block();
    }

    @Test
    public void assignRolesTestSuccess(){
        User dbUser = userRepository.save(new User( "12345", "client", "tenant")).block();
        Role dbRole = roleRepository.save(new Role("ROLE_KEY", "role-name", "tenant", true)).block();

        List<String> roleKeys = Arrays.asList(dbRole.getKey());

        StepVerifier.create(userService.assignRolesToUser(dbUser, roleKeys).collectList())
                .assertNext(userRoleMappings -> {
                    assertEquals(1, userRoleMappings.size());
                    UserRoleMapping urm = userRoleMappings.get(0);
                    assertEquals(dbUser.getId(), urm.getUserId());
                    assertEquals(dbRole.getId(), urm.getRoleId());
                })
                .verifyComplete();
    }

    @Test
    public void assignRolesTestRoleDoesNotExist(){
        User dbUser = userRepository.save(new User( "12345", "client", "tenant")).block();
        //storing another role, just for fun
        Role dbRole = roleRepository.save(new Role("ROLE_KEY", "role-name", "tenant", true)).block();

        List<String> roleKeys = Arrays.asList("KEY_WHICH_DOES_NOT_EXIST");

        StepVerifier.create(userService.assignRolesToUser(dbUser, roleKeys).collectList())
                .expectError(InvalidRoleKeysException.class)
                .verify();
    }

    @Test
    public void assignRolesToAuthUserTestSuccess(){
        User dbUser = userRepository.save(new User( "12345", "client", "tenant")).block();
        Role dbRole = roleRepository.save(new Role("ROLE_KEY", "role-name", "tenant", true)).block();

        List<String> roleKeys = Arrays.asList(dbRole.getKey());

        StepVerifier.create(userService.assignRolesToUser(new AuthUser(dbUser), roleKeys).collectList())
                .assertNext(userRoleMappings -> {
                    assertEquals(1, userRoleMappings.size());
                    UserRoleMapping urm = userRoleMappings.get(0);
                    assertEquals(dbUser.getId(), urm.getUserId());
                    assertEquals(dbRole.getId(), urm.getRoleId());
                })
                .verifyComplete();
    }

    @Test
    public void assignRolesToAuthUserTestRoleDoesNotExist(){
        List<String> roleKeys = Arrays.asList("KEY_WHICH_DOES_NOT_EXIST");
        StepVerifier.create(userService.assignRolesToUser(AuthUser.from("usrId", "client", "tenant"), roleKeys).collectList())
                .expectError(UserNotFoundException.class)
                .verify();
    }

    @Test
    public void saveUserSuccess(){
        AuthUser au = AuthUser.from("123456", "client", "tenant");

        StepVerifier.create(userService.save(au))
                .assertNext(dbUser -> assertEquals(au, dbUser))
                .verifyComplete();
    }

    @Test
    public void saveUserAlreadyFoundInDB(){
        User dbUser = userRepository.save(new User("userId", "client", "tenant")).block();

        AuthUser au = new AuthUser(dbUser);

        StepVerifier.create(userService.save(au))
                .assertNext(dbAu -> {
                    assertEquals(dbUser.getId(), dbAu.getId());
                    assertEquals(dbUser.getUserId(), dbAu.getUserId());
                    assertEquals(dbUser.getClient(), dbAu.getClient());
                    assertEquals(dbUser.getTenant(), dbAu.getTenant());
                    assertEquals(dbUser.getCreatedAt(), dbAu.getCreatedAt());
                    assertEquals(dbUser.getUpdatedAt(), dbAu.getUpdatedAt());
                })
                .verifyComplete();
    }
}
