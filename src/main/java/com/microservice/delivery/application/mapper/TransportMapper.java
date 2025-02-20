package com.microservice.delivery.application.mapper;

import com.microservice.delivery.application.entity.DeliveryDto;
import com.microservice.delivery.application.entity.TransportDto;
import com.microservice.delivery.domain.entities.Delivery;
import org.springframework.stereotype.Component;

@Component
public class TransportMapper {

    public TransportDto mapToDTO(Delivery delivery){
        return TransportDto.builder()
                .deliveryId(delivery.getId())
                .address(delivery.getAddress())
                .zipCode(delivery.getZipCode())
                .city(delivery.getCity())
                .customerEmail(delivery.getCustomerEmail())
                .vehicleActualCapacity(delivery.getVehicleActualCapacity())
                .vehicleMaxCapacity(delivery.getVehicleMaxCapacity())
                .vehicleId(delivery.getVehicleId())
                .transporter(delivery.getTransporter())
                .status(delivery.getStatus())
                .build();
    }

    public Delivery mapToDelivery(TransportDto transportDto) {
        return Delivery.builder()
                .id(transportDto.getDeliveryId())
                .transporter(transportDto.getTransporter())
                .status(transportDto.getStatus())
                .vehicleId(transportDto.getVehicleId())
                .vehicleMaxCapacity(transportDto.getVehicleMaxCapacity())
                .vehicleActualCapacity(transportDto.getVehicleActualCapacity())
                .build();
    }
}
