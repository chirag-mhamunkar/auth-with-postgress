package com.example.authservice.repository;

import com.example.authservice.configuration.DBConfiguration;
import com.example.authservice.entity.*;
import com.example.authservice.entity.dummy.PermissionDummy;
import com.example.authservice.entity.dummy.RoleDummy;
import com.example.authservice.entity.dummy.UserDummy;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@DataR2dbcTest
@Import(DBConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JoinTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private UserRoleMappingRepository userRoleMappingRepository;
    @Autowired
    private RolePermissionMappingRepository rolePermissionMappingRepository;

    @Test
    public void join(){
        User user = UserDummy.create();
        userRepository.save(user).block();

        Role role = RoleDummy.create();
        roleRepository.save(role).block();

        Permission permission = PermissionDummy.create();
        permissionRepository.save(permission).block();

        rolePermissionMappingRepository.save(new RolePermissionMapping(role.getId(), permission.getId())).block();
        userRoleMappingRepository.save(new UserRoleMapping(user.getId(), role.getId())).block();

        StepVerifier.create(userRepository.findFullUser(user.getId()))
                .assertNext(dbUser -> assertEquals(user, dbUser))
                .verifyComplete();
    }
}