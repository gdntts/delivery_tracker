package dev.gustavodntts.deliverytracker.repository;

import dev.gustavodntts.deliverytracker.domain.DeliveryTracking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryTrackingRepository extends JpaRepository<DeliveryTracking, UUID> {

    Optional<DeliveryTracking> findByOrderId(UUID orderId);
}
