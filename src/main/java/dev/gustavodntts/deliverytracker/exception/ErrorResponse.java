package dev.gustavodntts.deliverytracker.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Estrutura padrão de resposta de erro")
public record ErrorResponse(
        @Schema(description = "Momento em que o erro ocorreu", example = "2026-06-22T14:30:00")
        LocalDateTime timestamp,

        @Schema(description = "Código HTTP do erro", example = "400")
        Integer status,

        @Schema(description = "Tipo do erro", example = "Validation Failed")
        String error,

        @Schema(description = "Lista de mensagens detalhando o erro", example = "[\"orderId: must not be null\"]")
        List<String> messages) {
}
