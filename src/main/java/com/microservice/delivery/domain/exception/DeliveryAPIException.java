package com.microservice.delivery.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class DeliveryAPIException extends RuntimeException {

    private final String message;
    @Getter
    private final HttpStatus status;

    public DeliveryAPIException(String message, HttpStatus status) {
        this.status = status;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
