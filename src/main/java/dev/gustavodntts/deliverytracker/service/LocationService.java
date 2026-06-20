package dev.gustavodntts.deliverytracker.service;

import dev.gustavodntts.deliverytracker.domain.DeliveryTracking;
import dev.gustavodntts.deliverytracker.domain.LocationHistory;
import dev.gustavodntts.deliverytracker.domain.Order;
import dev.gustavodntts.deliverytracker.repository.DeliveryTrackingRepository;
import dev.gustavodntts.deliverytracker.repository.LocationHistoryRepository;
import dev.gustavodntts.deliverytracker.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class LocationService {

    private final LocationHistoryRepository locationHistoryRepository;

    private final DeliveryTrackingRepository deliveryTrackingRepository;

    private final OrderRepository orderRepository;

    public LocationService(LocationHistoryRepository locationHistoryRepository,
                           DeliveryTrackingRepository deliveryTrackingRepository,
                           OrderRepository orderRepository) {
        this.locationHistoryRepository = locationHistoryRepository;
        this.deliveryTrackingRepository = deliveryTrackingRepository;
        this.orderRepository = orderRepository;
    }

    private void upsertDeliveryTracking(Order order, BigDecimal lat, BigDecimal lng) {
        DeliveryTracking deliveryTracking = deliveryTrackingRepository.findByOrderId(order.getId()).orElseGet(() -> {
            DeliveryTracking dt = new DeliveryTracking();
            dt.setOrder(order);
            return dt;
        });

        deliveryTracking.setLat(lat);
        deliveryTracking.setLng(lng);
        deliveryTrackingRepository.save(deliveryTracking);
    }

    private void recordLocationHistory(Order order, BigDecimal lat, BigDecimal lng) {
        LocationHistory locationHistory = new LocationHistory();

        locationHistory.setOrder(order);
        locationHistory.setLat(lat);
        locationHistory.setLng(lng);
        locationHistoryRepository.save(locationHistory);
    }

    public void updateLocation(UUID orderId, BigDecimal lat, BigDecimal lng) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + orderId));

        upsertDeliveryTracking(order, lat, lng);
        recordLocationHistory(order, lat, lng);
    }
}
