package com.microservice.delivery.infrastructure.mapper;

import com.microservice.delivery.domain.entities.Delivery;
import com.microservice.delivery.infrastructure.entity.DeliveryEntity;
import com.microservice.delivery.infrastructure.entity.TransportEntity;
import com.microservice.delivery.infrastructure.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DeliveryEntityMapper {

    public DeliveryEntity mapToEntity(Delivery delivery, UserEntity userEntity) {
        return DeliveryEntity.builder()
                .id(delivery.getId())
                .address(userEntity.getAddress())
                .zipCode(userEntity.getZipCode())
                .city(userEntity.getCity())
                .email(userEntity.getEmail())
                .numberPackage(delivery.getNumberPackage())
                .transport(delivery.getTransporter())
                .status(delivery.getStatus())
                .build();
    }

    public Delivery mapToModel(DeliveryEntity deliveryEntity) {
        return Delivery.builder()
                .id(deliveryEntity.getId())
                .numberPackage(deliveryEntity.getNumberPackage())
                .transporter(deliveryEntity.getTransport())
                .status(deliveryEntity.getStatus())
                .address(deliveryEntity.getAddress())
                .zipCode(deliveryEntity.getZipCode())
                .city(deliveryEntity.getCity())
                .customerEmail(deliveryEntity.getEmail())
                .build();
    }

    public DeliveryEntity mapUpdateToEntity(Delivery delivery, DeliveryEntity deliveryEntity, TransportEntity transportEntity) {
        Set<TransportEntity> transportEntities = new HashSet<>();
        transportEntities.add(transportEntity);

        deliveryEntity.setTransport(delivery.getTransporter());
        deliveryEntity.setStatus(delivery.getStatus());
        deliveryEntity.setTransports(transportEntities);

        return deliveryEntity;
    }

}
