package dev.gustavodntts.deliverytracker.listener;

import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.postgresql.jdbc.PgConnection;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Component
public class LocationNotificationListener {

    private final DataSource dataSource;

    private final SimpMessagingTemplate messagingTemplate;

    private final ObjectMapper objectMapper;

    public LocationNotificationListener(
            ObjectMapper objectMapper,
            SimpMessagingTemplate messagingTemplate,
            DataSource dataSource
    ) {
        this.objectMapper = objectMapper;
        this.messagingTemplate = messagingTemplate;
        this.dataSource = dataSource;
    }

    @Async
    @EventListener(ApplicationReadyEvent.class)
    public void listen() throws Exception {
        Connection connection = dataSource
                .getConnection()
                .unwrap(PgConnection.class)
                .unwrap(Connection.class);

        PGConnection pgConnection = connection.unwrap(PGConnection.class);

        try (Statement statement = connection.createStatement()) {
            statement.execute("LISTEN location_updated");
        }

        while (true) {
            PGNotification[] notifications = pgConnection.getNotifications(5000);

            if (notifications != null) {
                for (PGNotification notification : notifications) {
                    handleNotification(notification.getParameter());
                }
            }
        }
    }

    private void handleNotification(String payload) throws Exception {
        JsonNode json = objectMapper.readTree(payload);
        String orderId = json.get("order_id").asString();
        messagingTemplate.convertAndSend("/topic/order/" + orderId, json);
    }
}
