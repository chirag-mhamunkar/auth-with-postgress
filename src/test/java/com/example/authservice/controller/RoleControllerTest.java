package com.example.authservice.controller;

import com.example.authservice.configuration.DBConfiguration;
import com.example.authservice.entity.Permission;
import com.example.authservice.model.AuthRole;
import com.example.authservice.repository.PermissionRepository;
import com.example.authservice.repository.RolePermissionMappingRepository;
import com.example.authservice.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@Slf4j
@SpringBootTest
@AutoConfigureWebTestClient
@Import(DBConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RoleControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RolePermissionMappingRepository rolePermissionMappingRepository;

    @Autowired
    private RoleRepository roleRepository;


    @BeforeEach
    public void init(){
        permissionRepository.deleteAll().block();
        roleRepository.deleteAll().block();
        rolePermissionMappingRepository.deleteAll().block();
    }

    @Test
    public void addRole(){
        permissionRepository.save(new Permission("PM_KEY", "PM_NAME", true)).block();

        List<String> permissionKeys = Arrays.asList("PM_KEY");
        RoleController.RoleRequestDTO role = new RoleController.RoleRequestDTO("ROLE_KEY", "ROLE_NAME", "tenant", true, permissionKeys);

        webTestClient.post()
                .uri("/role")
                .body(BodyInserters.fromValue(role))
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthRole.class)
                .consumeWith(res -> {
                    AuthRole authRole = res.getResponseBody();
                    log.info("{}", authRole);
                });

        StepVerifier.create(rolePermissionMappingRepository.count())
                .assertNext(ct -> Assertions.assertEquals(1, ct))
                .verifyComplete();

        StepVerifier.create(roleRepository.count())
                .assertNext(ct -> Assertions.assertEquals(1, ct))
                .verifyComplete();

    }

    @Test
    public void addRoleFailurePermissionIsInvalid(){

        List<String> permissionKeys = Arrays.asList("PM_KEY");
        RoleController.RoleRequestDTO role = new RoleController.RoleRequestDTO("ROLE_KEY", "ROLE_NAME", "tenant", true, permissionKeys);

        webTestClient.post()
                .uri("/role")
                .body(BodyInserters.fromValue(role))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .consumeWith(res -> log.info("############: {}", res.getResponseBody()));

        StepVerifier.create(rolePermissionMappingRepository.count())
                .assertNext(ct -> Assertions.assertEquals(0, ct))
                .verifyComplete();

        StepVerifier.create(roleRepository.count())
                .assertNext(ct -> Assertions.assertEquals(0, ct))
                .verifyComplete();

    }
}
