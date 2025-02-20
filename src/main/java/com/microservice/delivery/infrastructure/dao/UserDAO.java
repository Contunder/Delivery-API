package com.microservice.delivery.infrastructure.dao;

import com.microservice.delivery.JsonBodyHandler;
import com.microservice.delivery.application.security.JwtAuthenticationFilter;
import com.microservice.delivery.infrastructure.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.concurrent.ExecutionException;

@Component
public class UserDAO {

    private final HttpClient client;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${user.get}")
    private String userUrl;

    public UserDAO(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.client = HttpClient.newHttpClient();
    }

    public UserEntity getActualUser(String token) {
        UserEntity userEntity;

        HttpRequest getUserDetails = HttpRequest.newBuilder(
                        URI.create(userUrl)
                )
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        var account = client.sendAsync(getUserDetails, new JsonBodyHandler<>(UserEntity.class));

        try {
            userEntity = account.get().body().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return userEntity;

    }
}
