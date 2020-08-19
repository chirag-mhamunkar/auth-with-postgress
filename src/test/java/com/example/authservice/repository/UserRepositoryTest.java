package com.example.authservice.repository;

import com.example.authservice.configuration.DBConfiguration;
import com.example.authservice.entity.User;
import com.example.authservice.entity.dummy.UserDummy;
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
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void init(){
        userRepository.deleteAll().block();
        log.info("BeforeEach: cleaning everything");
    }

    @AfterAll
    public void destroy(){
        userRepository.deleteAll().block();
        log.info("AfterAll: cleaning everything");
    }

    @Test
    public void notNull(){
        assertNotNull(userRepository);
    }

    @Test
    public void insertUserTest(){
        User user = UserDummy.create();
        StepVerifier.create(userRepository.save(user))
                .assertNext(dbUser -> assertNotNull(dbUser.getId()))
                .verifyComplete();
    }

    @Test
    public void findByIdTest(){
        User user = UserDummy.create();
        userRepository.save(user).block();
        StepVerifier.create(userRepository.findById(user.getId()))
                .assertNext(dbUser -> assertEquals(user, dbUser))
                .verifyComplete();
    }

    @Test
    public void findByIdsTest(){
        User user1 = UserDummy.create();
        User user2 = UserDummy.create();
        user2.setUserId("2345");
        userRepository.saveAll(Arrays.asList(user1, user2)).collectList().block();
        StepVerifier.create(userRepository.findAllById(Arrays.asList(user1.getId(), user2.getId())).collectList())
                .assertNext(dbUsers -> {
                    assertEquals(2, dbUsers.size());
                })
                .verifyComplete();
    }

    @Test
    public void findByUserIdTest(){
        User user = UserDummy.create();
        userRepository.save(user).block();
        StepVerifier.create(userRepository.findByUserIdIn(Arrays.asList(user.getUserId())).collectList())
                .assertNext(dbUsers -> {
                    assertEquals(1, dbUsers.size());
                    assertEquals(user, dbUsers.get(0));
                })
                .verifyComplete();
    }

    @Test
    public void findByUserIdClientTenantTest(){
        User u = UserDummy.create();
        userRepository.save(u).block();
        StepVerifier.create(userRepository.findByUserIdAndClientAndTenant(u.getUserId(), u.getClient(), u.getTenant()))
                .assertNext(dbUser -> assertEquals(u, dbUser))
                .verifyComplete();
    }

    @Test
    public void findByUserIdClientTenantFailureTest(){
        User u = UserDummy.create();
        userRepository.save(u).block();
        StepVerifier.create(userRepository.findByUserIdAndClientAndTenant(u.getUserId(), u.getClient(), "random"))
                .verifyComplete();
    }
    
}
