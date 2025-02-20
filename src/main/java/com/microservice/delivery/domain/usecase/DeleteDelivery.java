package com.microservice.delivery.domain.usecase;

import com.microservice.delivery.domain.entities.Delivery;
import com.microservice.delivery.domain.exception.DeliveryAPIException;
import com.microservice.delivery.domain.ports.DeliveryPort;
import org.springframework.stereotype.Component;

@Component
public class DeleteDelivery {

    private final DeliveryPort deliveryPort;

    public DeleteDelivery(DeliveryPort deliveryPort) {
        this.deliveryPort = deliveryPort;
    }

    public String execute(Delivery delivery) throws DeliveryAPIException {
        deliveryPort.deleteDelivery(
                delivery,
                "Delete User"
        );

        return "Delivery doing by User " + delivery.getCustomerEmail() + " deleted successfully.";
    }

}
