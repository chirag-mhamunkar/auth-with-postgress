package com.example.authservice.repository;

import com.example.authservice.configuration.DBConfiguration;
import com.example.authservice.entity.Permission;
import com.example.authservice.entity.Role;
import com.example.authservice.entity.RolePermissionMapping;
import com.example.authservice.entity.dummy.RoleDummy;
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

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @AfterAll
    @BeforeEach
    public void init(){
        rolePermissionMappingRepository.deleteAll().block();
        roleRepository.deleteAll().block();
        permissionRepository.deleteAll().block();
    }

    @Test
    public void notNull(){
        assertNotNull(rolePermissionMappingRepository);
    }


    @Test
    public void findByRoleIdTest(){
        Role role = roleRepository.save(RoleDummy.create()).block();
        Permission permission = permissionRepository.save(new Permission("PERMISSION_KEY", "PERMISSION_NAME", true)).block();


        RolePermissionMapping rolePermissionMapping = new RolePermissionMapping(role.getId(), permission.getId());
        rolePermissionMappingRepository.save(rolePermissionMapping).block();
        StepVerifier.create(rolePermissionMappingRepository.findByRoleId(role.getId()).collectList())
                .assertNext(list -> {
                    assertEquals(1, list.size());
                    assertEquals(rolePermissionMapping, list.get(0));
                })
                .verifyComplete();
    }

    @Test
    public void findByRoleIdsTest(){
        Role role = roleRepository.save(RoleDummy.create()).block();
        Permission permission = permissionRepository.save(new Permission("PERMISSION_KEY", "PERMISSION_NAME", true)).block();


        RolePermissionMapping rolePermissionMapping = new RolePermissionMapping(role.getId(), permission.getId());
        rolePermissionMappingRepository.save(rolePermissionMapping).block();

        StepVerifier.create(rolePermissionMappingRepository.findByRoleIdIn(Arrays.asList(role.getId())).collectList())
                .assertNext(list -> assertEquals(1, list.size()))
                .verifyComplete();
    }
}
