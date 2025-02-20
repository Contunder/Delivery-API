package com.microservice.delivery.domain.ports;

import com.microservice.delivery.domain.entities.Delivery;

import java.util.List;

public interface DeliveryPort {

    Delivery createDelivery(Delivery delivery, String trackingEvent, String token);

    List<Delivery> findAllDelivery();

    List<Delivery> findDeliveryByEmail(String email);

    Delivery deleteDelivery(Delivery delivery, String trackingEvent);

    Delivery updateDelivery(Delivery delivery, String trackingEvent);
}
