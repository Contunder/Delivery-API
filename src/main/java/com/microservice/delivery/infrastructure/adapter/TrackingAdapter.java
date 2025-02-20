package com.microservice.delivery.infrastructure.adapter;

import com.microservice.delivery.domain.entities.Delivery;
import com.microservice.delivery.infrastructure.dao.TrackingDAO;
import com.microservice.delivery.infrastructure.mapper.TrackingMapper;
import org.springframework.stereotype.Component;

@Component
public class TrackingAdapter {

    private final TrackingDAO trackingDAO;
    private final TrackingMapper trackingMapper;

    public TrackingAdapter(TrackingDAO trackingDAO, TrackingMapper trackingMapper) {
        this.trackingDAO = trackingDAO;
        this.trackingMapper = trackingMapper;
    }

    public Delivery sendTrackingEvent(Delivery delivery, String event) {
        trackingDAO.sendUserTrackingEvent(trackingMapper.mapToEntity(delivery, event));

        return delivery;
    }
}
