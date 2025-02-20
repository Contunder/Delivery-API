package com.microservice.delivery.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.Set;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Delivery")
public class DeliveryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String zipCode;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private int numberPackage;
    private String transport;
    private String status;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "deliveries_transports",
            joinColumns = @JoinColumn(name = "delivery_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "transport_id", referencedColumnName = "id")
    )
    private Set<TransportEntity> transports;

}
