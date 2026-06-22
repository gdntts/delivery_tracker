package dev.gustavodntts.deliverytracker.controller;

import dev.gustavodntts.deliverytracker.dto.LocationRequest;
import dev.gustavodntts.deliverytracker.exception.ErrorResponse;
import dev.gustavodntts.deliverytracker.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Localização", description = "Atualização de localização de entregas em tempo real")
@RestController
@RequestMapping("/api/location")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @Operation(
            summary = "Atualizar localização do pedido",
            description = """
                    Registra a nova posição GPS do entregador para o pedido informado.

                    Além de persistir a posição atual e o histórico, emite um evento em tempo real
                    via WebSocket no tópico `/topic/order.{orderId}` para todos os clientes conectados.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Localização atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou pedido não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void updateLocation(@RequestBody @Valid LocationRequest locationRequest) {
        locationService.updateLocation(locationRequest.orderId(), locationRequest.lat(), locationRequest.lng());
    }
}
