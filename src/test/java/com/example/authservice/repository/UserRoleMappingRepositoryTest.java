package com.example.authservice.repository;

import com.example.authservice.configuration.DBConfiguration;
import com.example.authservice.entity.Role;
import com.example.authservice.entity.User;
import com.example.authservice.entity.UserRoleMapping;
import com.example.authservice.entity.dummy.RoleDummy;
import com.example.authservice.entity.dummy.UserDummy;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@DataR2dbcTest
@Import(DBConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRoleMappingRepositoryTest {

    @Autowired
    private UserRoleMappingRepository userRoleMappingRepository;


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void init(){
        userRoleMappingRepository.deleteAll().block();
        userRepository.deleteAll().block();
        roleRepository.deleteAll().block();
        log.info("BeforeEach: cleaning everything");
    }

    @Test
    public void notNull(){
        assertNotNull(userRoleMappingRepository);
    }

    @Test
    public void findByUserIdTest(){

        User user = userRepository.save(UserDummy.create()).block();
        Role role = roleRepository.save(RoleDummy.create()).block();

        UserRoleMapping userRoleMapping = new UserRoleMapping(user.getId(), role.getId());
        userRoleMappingRepository.save(userRoleMapping).block();
        StepVerifier.create(userRoleMappingRepository.findByUserId(user.getId()).collectList())
                .assertNext(list -> {
                    assertEquals(1, list.size());
                    assertEquals(userRoleMapping, list.get(0));
                })
                .verifyComplete();
    }

    @Test
    public void findByUserIdsTest(){
        User user = userRepository.save(UserDummy.create()).block();
        Role role = roleRepository.save(RoleDummy.create()).block();

        UserRoleMapping userRoleMapping = new UserRoleMapping(user.getId(), role.getId());
        userRoleMappingRepository.save(userRoleMapping).block();
        StepVerifier.create(userRoleMappingRepository.findByUserIdIn(Arrays.asList(user.getId())).collectList())
                .assertNext(list -> assertEquals(1, list.size()))
                .verifyComplete();
    }
}
