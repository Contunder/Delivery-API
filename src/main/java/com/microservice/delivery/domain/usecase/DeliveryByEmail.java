package com.microservice.delivery.domain.usecase;

import com.microservice.delivery.domain.entities.Delivery;
import com.microservice.delivery.domain.exception.DeliveryAPIException;
import com.microservice.delivery.domain.ports.DeliveryPort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeliveryByEmail {

    private final DeliveryPort deliveryPort;

    public DeliveryByEmail(DeliveryPort deliveryPort){
        this.deliveryPort = deliveryPort;
    }

    public List<Delivery> execute(String userEmail) throws DeliveryAPIException {

        return deliveryPort.findDeliveryByEmail(userEmail);
    }

}
