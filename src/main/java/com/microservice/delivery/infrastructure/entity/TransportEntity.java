package com.microservice.delivery.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Transport")
public class TransportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, unique = true)
    private String vehicleId;
    @Column(nullable = false)
    private int vehicleMaxCapacity;
    private int vehicleActualCapacity;

}
