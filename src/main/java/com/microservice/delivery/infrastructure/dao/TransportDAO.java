package com.microservice.delivery.infrastructure.dao;

import com.microservice.delivery.infrastructure.entity.TransportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransportDAO extends JpaRepository<TransportEntity, Long> {

    Optional<TransportEntity> findByVehicleId(String vehicleId);
    Optional<TransportEntity> findById(long id);

}
