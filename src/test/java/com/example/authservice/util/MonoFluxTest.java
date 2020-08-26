package com.example.authservice.util;

import com.example.authservice.exception.InvalidRoleKeysException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

public class MonoFluxTest {


    @Test
    public void switchEmptyTest(){
        Mono<String> monoStr = Mono.empty();

        StepVerifier.create(
                monoStr
                .switchIfEmpty(Mono.error(new RuntimeException("empty mono")))
                .map(s -> s.toUpperCase())
                .doOnNext(s -> System.out.println(s))
                .doOnError(throwable -> System.out.println(throwable))
        )
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void switchEmptySuccessTest(){
        Mono<String> monoStr = Mono.just("chirag");

        StepVerifier.create(
                monoStr
                        .switchIfEmpty(Mono.error(new RuntimeException("empty mono")))
                        .map(s -> s.toUpperCase())
                        .doOnNext(s -> System.out.println(s))
                        .doOnError(throwable -> System.out.println(throwable))
        )
                .assertNext(s -> Assertions.assertEquals("CHIRAG", s))
                //.expectError(RuntimeException.class)
                .verifyComplete();
    }

    @Test void switchIfEmptyFluxTest(){
        Flux<String> fluxStr = Flux.empty();

        fluxStr
                //.switchIfEmpty(Flux.error(new RuntimeException("flux is empty")))
                .switchIfEmpty(Flux.error(new InvalidRoleKeysException(null)))
                .map(s -> s.toUpperCase());

    }
}
