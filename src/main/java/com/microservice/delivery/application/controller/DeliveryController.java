package com.microservice.delivery.application.controller;

import com.microservice.delivery.application.entity.DeliveryDto;
import com.microservice.delivery.application.entity.TransportDto;
import com.microservice.delivery.application.mapper.DeliveryMapper;
import com.microservice.delivery.application.mapper.TransportMapper;
import com.microservice.delivery.application.presenter.Presenter;
import com.microservice.delivery.application.security.JwtAuthenticationFilter;
import com.microservice.delivery.application.security.JwtTokenProvider;
import com.microservice.delivery.domain.exception.DeliveryAPIException;
import com.microservice.delivery.domain.usecase.MyDelivery;
import com.microservice.delivery.domain.usecase.CreateDelivery;
import com.microservice.delivery.domain.usecase.DeleteDelivery;
import com.microservice.delivery.domain.usecase.UpdateDelivery;
import com.microservice.delivery.domain.usecase.DeliveryByEmail;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/delivery")
public class DeliveryController {

    private final DeliveryByEmail deliveryByEmail;
    private final Presenter presenter;
    private final MyDelivery myDelivery;
    private final DeleteDelivery deleteDelivery;
    private final UpdateDelivery updateDelivery;
    private final CreateDelivery createDelivery;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtTokenProvider jwtTokenProvider;
    private final DeliveryMapper deliveryMapper;
    private final TransportMapper transportMapper;

    public DeliveryController(DeliveryByEmail deliveryByEmail, Presenter presenter, MyDelivery myDelivery, DeleteDelivery deleteDelivery, UpdateDelivery updateDelivery, CreateDelivery createDelivery, JwtAuthenticationFilter jwtAuthenticationFilter, JwtTokenProvider jwtTokenProvider, DeliveryMapper deliveryMapper, TransportMapper transportMapper) {
        this.deliveryByEmail = deliveryByEmail;
        this.presenter = presenter;
        this.myDelivery = myDelivery;
        this.deleteDelivery = deleteDelivery;
        this.updateDelivery = updateDelivery;
        this.createDelivery = createDelivery;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtTokenProvider = jwtTokenProvider;
        this.deliveryMapper = deliveryMapper;
        this.transportMapper = transportMapper;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = {"/email/{email}"})
    public ResponseEntity<?> getDeliveryByEmail(@PathVariable("email") String email) {
        try {

            return presenter.presentSuccess(deliveryByEmail.execute(email));
        } catch (DeliveryAPIException deliveryAPIException) {

            return presenter.presentFailure(deliveryAPIException);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_TRANSPORT','ROLE_ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<?> updateDelivery(@Valid @RequestBody TransportDto transportDto) {
        try {

            return presenter.presentTransportSuccess(updateDelivery.execute(transportMapper.mapToDelivery(transportDto)));
        } catch (DeliveryAPIException deliveryAPIException) {

            return presenter.presentFailure(deliveryAPIException);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createDelivery(@Valid @RequestBody DeliveryDto deliveryDto, HttpServletRequest request) {
        try {
            String token = jwtAuthenticationFilter.getTokenFromRequest(request);
            jwtTokenProvider.validateToken(token);

            return presenter.presentSuccess(createDelivery.execute(deliveryMapper.mapToModel(deliveryDto), token));
        } catch (DeliveryAPIException deliveryAPIException) {

            return presenter.presentFailure(deliveryAPIException);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteDelivery(@Valid @RequestBody DeliveryDto deliveryDto) {
        try {
            return presenter.presentSuccess(deleteDelivery.execute(deliveryMapper.mapToModel(deliveryDto)));
        } catch (DeliveryAPIException deliveryAPIException) {

            return presenter.presentFailure(deliveryAPIException);
        }
    }

    @GetMapping(value = {"/actual"})
    public ResponseEntity<?> getMyDelivery(HttpServletRequest request) {
        try {
            String token = jwtAuthenticationFilter.getTokenFromRequest(request);
            jwtTokenProvider.validateToken(token);
            String email = jwtTokenProvider.getUsername(token);

            return presenter.presentSuccess(deliveryByEmail.execute(email));
        } catch (DeliveryAPIException deliveryAPIException) {

            return presenter.presentFailure(deliveryAPIException);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllDelivery(HttpServletRequest request) {
        try {
            String token = jwtAuthenticationFilter.getTokenFromRequest(request);
            jwtTokenProvider.validateToken(token);
            String email = jwtTokenProvider.getUsername(token);

            return presenter.presentSuccess(myDelivery.execute(email));
        } catch (DeliveryAPIException deliveryAPIException) {

            return presenter.presentFailure(deliveryAPIException);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_TRANSPORT','ROLE_ADMIN')")
    @GetMapping("/pending")
    public ResponseEntity<?> getAllPendingDelivery(HttpServletRequest request) {
        try {
            String token = jwtAuthenticationFilter.getTokenFromRequest(request);
            jwtTokenProvider.validateToken(token);
            String email = jwtTokenProvider.getUsername(token);

            return presenter.presentSuccess(myDelivery.execute(email).stream().filter(delivery -> delivery.getStatus().equals("PENDING")).toList());
        } catch (DeliveryAPIException deliveryAPIException) {

            return presenter.presentFailure(deliveryAPIException);
        }
    }

}