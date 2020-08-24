package com.example.authservice.repository;

import com.example.authservice.configuration.DBConfiguration;
import com.example.authservice.entity.Permission;
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
public class PermissionRepositoryTest {

    @Autowired
    private PermissionRepository permissionRepository;

    @BeforeEach
    public void init(){
        permissionRepository.deleteAll().block();
        log.info("BeforeEach: cleaning everything");
    }

    @AfterAll
    public void destroy(){
        permissionRepository.deleteAll().block();
        log.info("AfterAll: cleaning everything");
    }

    @Test
    public void notNull(){
        assertNotNull(permissionRepository);
    }

    @Test
    public void saveRole(){
        Permission permission = new Permission("TAB_CUSTOMER_READ", "TAB_CUSTOMER_READ", true);
        StepVerifier.create(permissionRepository.save(permission))
                .assertNext(dbPermission -> {
                    assertNotNull(dbPermission.getId());
                    assertEquals(permission.getKey(), dbPermission.getKey());
                    assertEquals(permission, dbPermission);
                })
                .verifyComplete();
    }

    @Test
    public void findByKey(){
        Permission permission1 = new Permission("TAB_CUSTOMER_READ", "TAB_CUSTOMER_READ", true);
        Permission permission2 = new Permission("ADMIN_PERMISSION", "ADMIN_PERMISSION", true);
        permissionRepository.saveAll(Arrays.asList(permission1, permission2)).collectList().block();

        StepVerifier.create(permissionRepository.findByKey("TAB_CUSTOMER_READ"))
                .assertNext(dbPermission -> {
                    assertEquals(permission1.getKey(), dbPermission.getKey());
                    assertEquals(permission1.getId(), dbPermission.getId());
                    assertEquals(permission1, dbPermission);
                })
                .verifyComplete();
    }

    @Test
    public void findByKeys(){
        Permission permission1 = new Permission("TAB_CUSTOMER_READ", "TAB_CUSTOMER_READ", true);
        Permission permission2 = new Permission("ADMIN_PERMISSION", "ADMIN_PERMISSION", true);
        permissionRepository.saveAll(Arrays.asList(permission1, permission2)).collectList().block();

        StepVerifier.create(permissionRepository.findByKeyIn(Arrays.asList("TAB_CUSTOMER_READ", "ADMIN_PERMISSION", "TEST")).collectList())
                .assertNext(dbPermissions -> assertEquals(2, dbPermissions.size()))
                .verifyComplete();
    }

    @Test
    public void uniqueTest(){
        Permission pm = new Permission("PERMISSION_KEY", "PERMISSION_NAME", true);
        permissionRepository.save(pm).block();
        StepVerifier.create(permissionRepository.save(new Permission("PERMISSION_KEY", "PERMISSION_NAME", true)))
                .expectError(DataIntegrityViolationException.class)
                .verify();
    }
}
