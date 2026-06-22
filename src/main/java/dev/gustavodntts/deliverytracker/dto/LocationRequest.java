package dev.gustavodntts.deliverytracker.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record LocationRequest(
        @NotNull
        UUID orderId,

        @NotNull
        @DecimalMin("-90.0") @DecimalMax("90.0")
        BigDecimal lat,

        @NotNull
        @DecimalMin("-180.0") @DecimalMax("180.0")
        BigDecimal lng) {
}
