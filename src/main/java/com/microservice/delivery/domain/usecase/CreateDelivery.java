package com.microservice.delivery.domain.usecase;

import com.microservice.delivery.domain.entities.Delivery;
import com.microservice.delivery.domain.ports.DeliveryPort;
import org.springframework.stereotype.Component;

@Component
public class CreateDelivery {

    public static final String STATUS_PENDING = "PENDING";
    private final DeliveryPort deliveryPort;

    public CreateDelivery(DeliveryPort deliveryPort) {
        this.deliveryPort = deliveryPort;
    }

    public Delivery execute(Delivery delivery, String token) {

        delivery.setStatus(STATUS_PENDING);

        return deliveryPort.createDelivery(delivery, "Create Delivery", token);
    }

}
