package com.microservice.delivery.application.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransportDto {
    private long id;
    private long deliveryId;
    private String address;
    private String zipCode;
    private String city;
    private String customerEmail;
    private String vehicleId;
    private int vehicleMaxCapacity;
    private int vehicleActualCapacity;
    private String transporter;
    private String status;
}
