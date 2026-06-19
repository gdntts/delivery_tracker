package dev.gustavodntts.deliverytracker.repository;

import dev.gustavodntts.deliverytracker.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface OrderRepository extends JpaRepository<Order, UUID> {
}
