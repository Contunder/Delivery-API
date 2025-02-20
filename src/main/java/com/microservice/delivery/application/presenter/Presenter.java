package com.microservice.delivery.application.presenter;

import com.microservice.delivery.application.entity.DeliveryDto;
import com.microservice.delivery.application.entity.TransportDto;
import com.microservice.delivery.application.mapper.DeliveryMapper;
import com.microservice.delivery.application.mapper.TransportMapper;
import com.microservice.delivery.domain.entities.Delivery;
import com.microservice.delivery.domain.exception.DeliveryAPIException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Presenter {

    private final DeliveryMapper deliveryMapper;
    private final TransportMapper transportMapper;

    public Presenter(DeliveryMapper deliveryMapper, TransportMapper transportMapper) {
        this.deliveryMapper = deliveryMapper;
        this.transportMapper = transportMapper;
    }

    public ResponseEntity<DeliveryDto> presentSuccess(Delivery delivery) {
        return new ResponseEntity<>(deliveryMapper.mapToDTO(delivery), HttpStatus.OK);
    }

    public ResponseEntity<TransportDto> presentTransportSuccess(Delivery delivery) {
        return new ResponseEntity<>(transportMapper.mapToDTO(delivery), HttpStatus.OK);
    }

    public ResponseEntity<List<DeliveryDto>> presentSuccess(List<Delivery> deliveries) {
        return new ResponseEntity<>(deliveries.stream().map(deliveryMapper::mapToDTO).collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<String> presentSuccess(String successResponse) {
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    public ResponseEntity<DeliveryAPIException> presentFailure(DeliveryAPIException deliveryAPIException) {
        return new ResponseEntity<>(deliveryAPIException, HttpStatus.BAD_REQUEST);
    }

}