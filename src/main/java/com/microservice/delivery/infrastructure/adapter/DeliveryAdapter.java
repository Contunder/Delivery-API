package com.microservice.delivery.infrastructure.adapter;

import com.microservice.delivery.domain.entities.Delivery;
import com.microservice.delivery.domain.exception.DeliveryAPIException;
import com.microservice.delivery.domain.ports.DeliveryPort;
import com.microservice.delivery.infrastructure.dao.DeliveryDAO;
import com.microservice.delivery.infrastructure.dao.TransportDAO;
import com.microservice.delivery.infrastructure.dao.UserDAO;
import com.microservice.delivery.infrastructure.entity.DeliveryEntity;
import com.microservice.delivery.infrastructure.entity.TransportEntity;
import com.microservice.delivery.infrastructure.entity.UserEntity;
import com.microservice.delivery.infrastructure.mapper.DeliveryEntityMapper;
import com.microservice.delivery.infrastructure.mapper.TransportEntityMapper;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class DeliveryAdapter implements DeliveryPort {

    public static final String DELIVERY_ID_NOT_FOUND = "User not found with email: ";
    private final DeliveryDAO deliveryDAO;
    private final TransportDAO transportDAO;
    private final DeliveryEntityMapper deliveryEntityMapper;
    private final TransportEntityMapper transportEntityMapper;
    private final UserDAO userDAO;
    private final TrackingAdapter trackingAdapter;
    private final JavaMailSender mailSender;


    public DeliveryAdapter(DeliveryDAO deliveryDAO, TransportDAO transportDAO, DeliveryEntityMapper deliveryEntityMapper, TransportEntityMapper transportEntityMapper, UserDAO userDAO, TrackingAdapter trackingAdapter, JavaMailSender mailSender) {
        this.deliveryDAO = deliveryDAO;
        this.transportDAO = transportDAO;
        this.deliveryEntityMapper = deliveryEntityMapper;
        this.transportEntityMapper = transportEntityMapper;
        this.userDAO = userDAO;
        this.trackingAdapter = trackingAdapter;
        this.mailSender = mailSender;
    }


    @Override
    public Delivery createDelivery(Delivery delivery, String trackingEvent, String token) {
        UserEntity user = userDAO.getActualUser(token);

        Delivery newDelivery =  trackingAdapter.sendTrackingEvent(
                deliveryEntityMapper.mapToModel(
                        deliveryDAO.save(
                                deliveryEntityMapper.mapToEntity(delivery, user)
                        )
                ),
                trackingEvent
        );

//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setTo(newDelivery.getCustomerEmail());
//        msg.setText(
//                "Dear Customer, your delivery is now on status"
//                        + newDelivery.getStatus()
//                        + ". Your order number is "
//                        + newDelivery.getId());
//        try{
//            this.mailSender.send(msg);
//        }
//        catch(MailException ex) {
//            System.err.println(ex.getMessage());
//        }

        return newDelivery;
    }

    @Override
    public List<Delivery> findAllDelivery() {
        return deliveryDAO.findAll().stream()
                .map(deliveryEntityMapper::mapToModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Delivery> findDeliveryByEmail(String email) throws DeliveryAPIException {
        return deliveryDAO.findByEmail(email)
                .orElseThrow(() -> new DeliveryAPIException(DELIVERY_ID_NOT_FOUND + email, BAD_REQUEST))
                .stream().map(deliveryEntityMapper::mapToModel).toList();
    }

    @Override
    public Delivery deleteDelivery(Delivery delivery, String trackingEvent) throws DeliveryAPIException {
        deliveryDAO.delete(deliveryDAO.findByEmailAndId(delivery.getCustomerEmail(),delivery.getId()).orElseThrow(
                    () -> new DeliveryAPIException(DELIVERY_ID_NOT_FOUND + delivery.getCustomerEmail(), BAD_REQUEST)
                )
        );

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(delivery.getCustomerEmail());
        msg.setText(
                "Dear Customer, your delivery has been delete by an Admin. Your order number is "
                        + delivery.getId()
                        + ", your transport are not doing."
        );
        try{
            this.mailSender.send(msg);
        }
        catch(MailException ex) {
            System.err.println(ex.getMessage());
        }

        return trackingAdapter.sendTrackingEvent(delivery, trackingEvent);
    }

    @Override
    public Delivery updateDelivery(Delivery delivery, String trackingEvent) throws DeliveryAPIException {

        DeliveryEntity deliveryEntity = deliveryDAO.findById(delivery.getId()).orElseThrow(
                () -> new DeliveryAPIException(DELIVERY_ID_NOT_FOUND + delivery.getId(), BAD_REQUEST)
        );

        Optional<TransportEntity> optionalTransportEntity= transportDAO.findByVehicleId(delivery.getVehicleId());

        if (isNewValidation(optionalTransportEntity, deliveryEntity)) {
            optionalTransportEntity.get().setVehicleActualCapacity((optionalTransportEntity.get().getVehicleActualCapacity() + deliveryEntity.getNumberPackage()));
            optionalTransportEntity = Optional.of(transportDAO.save(optionalTransportEntity.get()));

        }
        if (isNotAtMaxCapacity(optionalTransportEntity)){
            throw new DeliveryAPIException("Not the place in this Vehicle max capacity is " + optionalTransportEntity.get().getVehicleMaxCapacity() , BAD_REQUEST);
        }
        if(optionalTransportEntity.isEmpty() && deliveryEntity.getTransports().isEmpty()){
            delivery.setNumberPackage(deliveryEntity.getNumberPackage());
            optionalTransportEntity = Optional.of(transportDAO.save(transportEntityMapper.mapDeliveryToEntity(delivery)));
        }

        Delivery updatedDelivery = trackingAdapter.sendTrackingEvent(
                deliveryEntityMapper.mapToModel(
                        deliveryDAO.save(deliveryEntityMapper.mapUpdateToEntity(
                                delivery,
                                deliveryEntity,
                                optionalTransportEntity.get()
                            )
                        )
                ),
                trackingEvent
        );

        updatedDelivery.setVehicleId(optionalTransportEntity.get().getVehicleId());
        updatedDelivery.setVehicleMaxCapacity(optionalTransportEntity.get().getVehicleMaxCapacity());
        updatedDelivery.setVehicleActualCapacity(optionalTransportEntity.get().getVehicleActualCapacity());

//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setTo(updatedDelivery.getCustomerEmail());
//        msg.setText(
//                "Dear Customer, your delivery is now on status"
//                        + updatedDelivery.getStatus()
//                        + ". Your order number is "
//                        + updatedDelivery.getId()
//                        + ", your transport are doing by "
//                        + updatedDelivery.getTransporter());
//        try{
//            this.mailSender.send(msg);
//        }
//        catch(MailException ex) {
//            System.err.println(ex.getMessage());
//        }

        return updatedDelivery;
    }

    private static boolean isNotAtMaxCapacity(Optional<TransportEntity> optionalTransportEntity) {
        return optionalTransportEntity.isPresent() && optionalTransportEntity.get().getVehicleMaxCapacity() == optionalTransportEntity.get().getVehicleActualCapacity();
    }

    private static boolean isNewValidation(Optional<TransportEntity> optionalTransportEntity, DeliveryEntity deliveryEntity) {
        return optionalTransportEntity.isPresent() && optionalTransportEntity.get().getVehicleMaxCapacity() > optionalTransportEntity.get().getVehicleActualCapacity() && deliveryEntity.getTransports().isEmpty();
    }

}
