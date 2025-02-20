package com.microservice.delivery.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {

    private long id;
    private String customerEmail;
    private String address;
    private String zipCode;
    private String city;
    private int numberPackage;
    private String transporter;
    private String vehicleId;
    private int vehicleMaxCapacity;
    private int vehicleActualCapacity;
    private String status;

}
