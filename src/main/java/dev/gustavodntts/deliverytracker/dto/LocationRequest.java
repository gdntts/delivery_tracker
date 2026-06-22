package dev.gustavodntts.deliverytracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Dados de atualização de localização do entregador")
public record LocationRequest(
        @Schema(description = "ID único do pedido", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        @NotNull
        UUID orderId,

        @Schema(description = "Latitude da posição atual (entre -90 e 90)", example = "-23.5505199")
        @NotNull
        @DecimalMin("-90.0") @DecimalMax("90.0")
        BigDecimal lat,

        @Schema(description = "Longitude da posição atual (entre -180 e 180)", example = "-46.6333094")
        @NotNull
        @DecimalMin("-180.0") @DecimalMax("180.0")
        BigDecimal lng) {
}
