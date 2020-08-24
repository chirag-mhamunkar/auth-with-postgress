package com.example.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

public class WebExceptionHandler {

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {KeysFoundException.class})
    public Mono<String> keysFound(KeysFoundException ex){
        return Mono.just(ex.getMessage());
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {InvalidPermissionKeysException.class})
    public Mono<String> inValidPermissionKey(InvalidPermissionKeysException ex){
        return Mono.just(ex.getMessage());
    }
}
