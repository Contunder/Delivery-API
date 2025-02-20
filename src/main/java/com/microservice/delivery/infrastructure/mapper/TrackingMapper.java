package com.microservice.delivery.infrastructure.mapper;

import com.microservice.delivery.domain.entities.Delivery;
import com.microservice.delivery.infrastructure.entity.TrackingEntity;
import org.springframework.stereotype.Component;

@Component
public class TrackingMapper {

    public TrackingEntity mapToEntity(Delivery delivery, String eventType) {
        return TrackingEntity.builder()
                .email(delivery.getCustomerEmail())
                .createType(eventType)
                .createId(delivery.getId())
                .build();
    }

}
