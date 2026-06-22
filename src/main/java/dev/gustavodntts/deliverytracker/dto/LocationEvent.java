package dev.gustavodntts.deliverytracker.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record LocationEvent(UUID orderId, BigDecimal lat, BigDecimal lng) {
}
