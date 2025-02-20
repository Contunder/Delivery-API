package com.microservice.delivery.domain.usecase;

import com.microservice.delivery.domain.entities.Delivery;
import com.microservice.delivery.domain.ports.DeliveryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MyDelivery {

    private final DeliveryPort deliveryPort;

    public MyDelivery(DeliveryPort deliveryPort){
        this.deliveryPort = deliveryPort;
    }

    public List<Delivery> execute(String userEmail) {

        return deliveryPort.findAllDelivery().stream()
                .filter(user -> user != deliveryPort.findDeliveryByEmail(userEmail))
                .collect(Collectors.toList());
    }

}
