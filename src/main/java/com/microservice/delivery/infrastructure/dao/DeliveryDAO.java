package com.microservice.delivery.infrastructure.dao;

import com.microservice.delivery.infrastructure.entity.DeliveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryDAO extends JpaRepository<DeliveryEntity, Long> {

    Optional<DeliveryEntity> findByEmailAndId(String email, Long id);
    Optional<List<DeliveryEntity>> findByEmail(String email);
    Optional<DeliveryEntity> findById(long id);

}
