package dev.gustavodntts.deliverytracker.domain;

import dev.gustavodntts.deliverytracker.domain.enums.DeliveryStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "delivery_status")
@Getter
@Setter
@NoArgsConstructor
public class DeliveryStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(precision = 10, scale = 7)
    private BigDecimal lat;

    @Column(precision = 10, scale = 7)
    private BigDecimal lng;

    @Enumerated(EnumType.STRING)
    private DeliveryStatusEnum status = DeliveryStatusEnum.in_transit;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
