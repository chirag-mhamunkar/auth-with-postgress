package com.example.authservice.repository;

import com.example.authservice.configuration.DBConfiguration;
import com.example.authservice.entity.Role;
import com.example.authservice.entity.dummy.RoleDummy;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@DataR2dbcTest
@Import(DBConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @AfterAll
    @BeforeEach
    public void init(){
        roleRepository.deleteAll().block();
    }

    @Test
    public void notNull(){
        assertNotNull(roleRepository);
    }

    @Test
    public void findRoleByKeyTest(){
        Role role = RoleDummy.create();
        roleRepository.save(role).block();

        StepVerifier.create(roleRepository.findByKey(role.getKey()))
                .assertNext(dbRole -> assertEquals(role, dbRole))
                .verifyComplete();
    }

    @Test
    public void findRoleByKeyFailureTest(){
        Role role = RoleDummy.create();
        roleRepository.save(role).block();

        StepVerifier.create(roleRepository.findByKey("random"))
                .verifyComplete();
    }

    @Test
    public void findRoleByKeysTest(){
        Role role1 = RoleDummy.create();
        Role role2 = RoleDummy.create();
        role2.setKey("ROLE_KEY_2");
        roleRepository.saveAll(Arrays.asList(role1, role2)).collectList().block();

        StepVerifier.create(roleRepository.findByKeyIn(Arrays.asList(role1.getKey(), role2.getKey())).collectList())
                .assertNext(dbRoles ->{
                    assertEquals(2, dbRoles.size());
                })
                .verifyComplete();
    }

    @Test
    public void uniqueTest(){
        roleRepository.save(RoleDummy.create()).block();
        StepVerifier.create(roleRepository.save(RoleDummy.create()))
                .expectError(DataIntegrityViolationException.class)
                .verify()
        ;
    }

}
