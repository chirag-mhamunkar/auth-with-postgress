package com.example.authservice.repository;

import com.example.authservice.configuration.DBConfiguration;
import com.example.authservice.entity.RolePermissionMapping;
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
public class RolePermissionMappingRepositoryTest {

    @Autowired
    private RolePermissionMappingRepository rolePermissionMappingRepository;

    @BeforeEach
    public void init(){
        rolePermissionMappingRepository.deleteAll().block();
        log.info("BeforeEach: cleaning everything");
    }

    @AfterAll
    public void destroy(){
        rolePermissionMappingRepository.deleteAll().block();
        log.info("AfterAll: cleaning everything");
    }

    @Test
    public void notNull(){
        assertNotNull(rolePermissionMappingRepository);
    }


    @Test
    public void findByRoleIdTest(){
        RolePermissionMapping rolePermissionMapping = new RolePermissionMapping(1l, 1l);
        rolePermissionMappingRepository.save(rolePermissionMapping).block();
        StepVerifier.create(rolePermissionMappingRepository.findByRoleId(1l).collectList())
                .assertNext(list -> {
                    assertEquals(1, list.size());
                    assertEquals(rolePermissionMapping, list.get(0));
                })
                .verifyComplete();
    }

    @Test
    public void findByUserIdsTest(){
        rolePermissionMappingRepository.saveAll(
                Arrays.asList(new RolePermissionMapping(1l, 1l), new RolePermissionMapping(2l, 2l)
                )).collectList().block();
        StepVerifier.create(rolePermissionMappingRepository.findByRoleIdIn(Arrays.asList(1l, 2l)).collectList())
                .assertNext(list -> assertEquals(2, list.size()))
                .verifyComplete();
    }
}
