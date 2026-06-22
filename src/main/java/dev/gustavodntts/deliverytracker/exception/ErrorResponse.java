package dev.gustavodntts.deliverytracker.exception;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(LocalDateTime timestamp, Integer status,
                            String error, List<String> messages) {
}
