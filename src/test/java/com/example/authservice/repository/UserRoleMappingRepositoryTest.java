package com.example.authservice.repository;

import com.example.authservice.configuration.DBConfiguration;
import com.example.authservice.entity.UserRoleMapping;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
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

    @BeforeEach
    public void init(){
        userRoleMappingRepository.deleteAll().block();
        log.info("BeforeEach: cleaning everything");
    }

    @AfterAll
    public void destroy(){
        userRoleMappingRepository.deleteAll().block();
        log.info("AfterAll: cleaning everything");
    }

    @Test
    public void notNull(){
        assertNotNull(userRoleMappingRepository);
    }

    @Test
    public void findByUserIdTest(){
        userRoleMappingRepository.saveAll(
                Arrays.asList(new UserRoleMapping(1l, 1l), new UserRoleMapping(1l, 2l)
                )).collectList().block();
        StepVerifier.create(userRoleMappingRepository.findByUserId(1l).collectList())
                .assertNext(list -> assertEquals(2, list.size()))
                .verifyComplete();
    }

    @Test
    public void findByUserIdsTest(){
        userRoleMappingRepository.saveAll(
                Arrays.asList(new UserRoleMapping(1l, 1l), new UserRoleMapping(2l, 2l)
                )).collectList().block();
        StepVerifier.create(userRoleMappingRepository.findByUserIdIn(Arrays.asList(1l, 2l)).collectList())
                .assertNext(list -> assertEquals(2, list.size()))
                .verifyComplete();
    }
}
