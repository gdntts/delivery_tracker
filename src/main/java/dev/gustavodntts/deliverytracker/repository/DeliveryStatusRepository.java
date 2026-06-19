package dev.gustavodntts.deliverytracker.repository;

import dev.gustavodntts.deliverytracker.domain.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryStatusRepository extends JpaRepository<DeliveryStatus, UUID> {
}
