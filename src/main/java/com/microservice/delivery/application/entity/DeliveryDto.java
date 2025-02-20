package com.microservice.delivery.application.entity;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class DeliveryDto {
    private long id;
    private String address;
    private String zipCode;
    private String city;
    private String customerEmail;
    private int numberPackage;
    private String transporter;
    private String status;
}
