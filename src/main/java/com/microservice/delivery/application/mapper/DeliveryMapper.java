package com.microservice.delivery.application.mapper;

import com.microservice.delivery.application.entity.DeliveryDto;
import com.microservice.delivery.domain.entities.Delivery;
import org.springframework.stereotype.Component;

@Component
public class DeliveryMapper {

    public DeliveryDto mapToDTO(Delivery delivery){
        return DeliveryDto.builder()
                .id(delivery.getId())
                .address(delivery.getAddress())
                .zipCode(delivery.getZipCode())
                .city(delivery.getCity())
                .customerEmail(delivery.getCustomerEmail())
                .numberPackage(delivery.getNumberPackage())
                .transporter(delivery.getTransporter())
                .status(delivery.getStatus())
                .build();
    }

    public Delivery mapToModel(DeliveryDto deliveryDto) {
        return Delivery.builder()
                .id(deliveryDto.getId())
                .address(deliveryDto.getAddress())
                .zipCode(deliveryDto.getZipCode())
                .city(deliveryDto.getCity())
                .customerEmail(deliveryDto.getCustomerEmail())
                .numberPackage(deliveryDto.getNumberPackage())
                .transporter(deliveryDto.getTransporter())
                .status(deliveryDto.getStatus())
                .build();
    }

}
