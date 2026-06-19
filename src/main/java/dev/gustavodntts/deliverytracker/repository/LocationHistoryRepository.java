package dev.gustavodntts.deliverytracker.repository;

import dev.gustavodntts.deliverytracker.domain.LocationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LocationHistoryRepository extends JpaRepository<LocationHistory, UUID> {
}
