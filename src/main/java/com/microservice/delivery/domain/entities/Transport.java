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
public class Transport {

    private long id;
    private String vehicleId;
    private int vehicleMaxCapacity;
    private int vehicleActualCapacity;

}
