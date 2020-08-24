package com.example.authservice.controller;

import com.example.authservice.configuration.DBConfiguration;
import com.example.authservice.entity.Permission;
import com.example.authservice.repository.PermissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

/*
    This is an integration test. It uses in memory HR database.
 */

@Slf4j
@SpringBootTest
@AutoConfigureWebTestClient
@Import(DBConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PermissionControllerTest {


    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PermissionRepository permissionRepository;

    @BeforeEach
    public void beforeEach(){
        permissionRepository.deleteAll().block();
    }

    @AfterAll
    public void afterAll(){
        permissionRepository.deleteAll().block();
    }

    @Test
    public void test(){
        List<PermissionController.PermissionDTO> permissions = new ArrayList<>();
        permissions.add(new PermissionController.PermissionDTO("KEY_1", "NAME_1", true));
        permissions.add(new PermissionController.PermissionDTO("KEY_2", "NAME_2", true));


        PermissionController.PermissionRequest rq =
                PermissionController.PermissionRequest.builder()
                        .permissions(permissions)
                        .build();
        webTestClient.post()
                .uri("/permission")
                .body(BodyInserters.fromValue(rq))
                .exchange()
                .expectStatus().isOk();

        StepVerifier.create(permissionRepository.count())
                .assertNext(aLong -> {
                    log.info("Permissions db has: {}", aLong);
                    Assertions.assertEquals(2, aLong);
                })
                .verifyComplete();
    }

    @Test
    public void keyAlreadyFoundTest(){

        permissionRepository.save(new Permission("KEY_1", "NAME_1", true)).block();

        List<PermissionController.PermissionDTO> permissions = new ArrayList<>();
        permissions.add(new PermissionController.PermissionDTO("KEY_1", "NAME_1", true));
        permissions.add(new PermissionController.PermissionDTO("KEY_2", "NAME_2", true));


        PermissionController.PermissionRequest rq =
                PermissionController.PermissionRequest.builder()
                        .permissions(permissions)
                        .build();
        webTestClient.post()
                .uri("/permission")
                .body(BodyInserters.fromValue(rq))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .consumeWith(s -> log.info("************** {}", s.getResponseBody()))

        ;

        StepVerifier.create(permissionRepository.count())
                .assertNext(aLong -> Assertions.assertEquals(1, aLong))
                .verifyComplete();
    }
}
