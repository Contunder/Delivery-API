package com.microservice.delivery.application.security;

import com.microservice.delivery.application.entity.UserDetailsDto;
import com.microservice.delivery.JsonBodyHandler;
import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@NonNullApi
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Value("${authentication.user_details}")
    private String authenticationUserDetailsURL;

    private final HttpClient client;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.client = HttpClient.newHttpClient();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = getTokenFromRequest(request);

        if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)){

            UserDetailsDto userDetailsDto = getUserDetailsDtoFromAuthenticationAPI(token);
            setAuthentication(request, userDetailsDto);

        }

        filterChain.doFilter(request, response);
    }

    private UserDetailsDto getUserDetailsDtoFromAuthenticationAPI(String token) {

        UserDetailsDto userDetailsDto;
        HttpRequest getUserDetails = HttpRequest.newBuilder(
                        URI.create(authenticationUserDetailsURL)
                )
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        var accountShipToResponseFuture = client.sendAsync(getUserDetails, new JsonBodyHandler<>(UserDetailsDto.class));

        try {
            userDetailsDto = accountShipToResponseFuture.get().body().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return userDetailsDto;

    }

    private void setAuthentication(HttpServletRequest request, UserDetailsDto userDetailsDto) {

        Set<GrantedAuthority> authorities = userDetailsDto
                .getRoles()
                .stream()
                .map((role) -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(userDetailsDto.getEmail(),
                userDetailsDto.getPassword(),
                authorities);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

    }

    public String getTokenFromRequest(HttpServletRequest request){

        String bearerToken = request.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }

        return null;
    }

}
