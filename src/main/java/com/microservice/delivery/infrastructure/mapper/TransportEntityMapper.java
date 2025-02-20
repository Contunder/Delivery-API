package com.microservice.delivery.infrastructure.mapper;

import com.microservice.delivery.domain.entities.Delivery;
import com.microservice.delivery.infrastructure.entity.TransportEntity;
import org.springframework.stereotype.Component;

@Component
public class TransportEntityMapper {

    public TransportEntity mapDeliveryToEntity(Delivery delivery) {
        if (delivery.getVehicleActualCapacity() == 0){
            delivery.setVehicleActualCapacity(delivery.getNumberPackage());
        }

        return TransportEntity.builder()
                .vehicleId(delivery.getVehicleId())
                .vehicleMaxCapacity(delivery.getVehicleMaxCapacity())
                .vehicleActualCapacity(delivery.getVehicleActualCapacity())
                .build();
    }

}
