package dev.gustavodntts.deliverytracker.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Delivery Tracker API")
                        .description("""
                                API para rastreamento em tempo real de entregas.
                                
                                ## REST
                                Recebe atualizações de localização do entregador, persiste o histórico de posições e a posição atual do pedido.
                                
                                ## WebSocket (STOMP)
                                Conecte-se ao endpoint `/ws` via SockJS/STOMP para receber atualizações em tempo real.
                                
                                - **Endpoint de conexão:** `/ws`
                                - **Subscribe:** `/topic/order.{orderId}`
                                - **Payload recebido:** `{ "orderId": "uuid", "lat": 0.0000000, "lng": 0.0000000 }`
                                """)
                        .version("0.0.1-SNAPSHOT")
                        .contact(new Contact()
                                .name("Gustavo Dantas")
                                .url("https://www.linkedin.com/in/gustavodantasmarim/"))
                        .license(new License()
                                .name("CC BY-NC 4.0 — uso não comercial")
                                .url("https://creativecommons.org/licenses/by-nc/4.0/")))
                .servers(List.of(new Server()
                        .url("http://localhost:8080")
                        .description("Servidor Local")))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentação do Projeto")
                        .url("https://github.com/gdntts/delivery_tracker"));
    }
}
