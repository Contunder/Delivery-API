package com.microservice.delivery.domain.usecase;

import com.microservice.delivery.domain.entities.Delivery;
import com.microservice.delivery.domain.exception.DeliveryAPIException;
import com.microservice.delivery.domain.ports.DeliveryPort;
import org.springframework.stereotype.Component;

@Component
public class UpdateDelivery {
    private final DeliveryPort deliveryPort;

    public UpdateDelivery(DeliveryPort deliveryPort) {
        this.deliveryPort = deliveryPort;
    }

    public Delivery execute(Delivery delivery) throws DeliveryAPIException {

        return deliveryPort.updateDelivery(delivery, "Update User");
    }

}
