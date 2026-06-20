package dev.gustavodntts.deliverytracker.repository;

import dev.gustavodntts.deliverytracker.domain.Order;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface OrderRepository extends JpaRepository<Order, UUID> {

    @NonNull Optional<Order> findById(@NonNull UUID id);
}
