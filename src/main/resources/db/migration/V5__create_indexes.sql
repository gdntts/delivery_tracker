CREATE INDEX idx_delivery_status_order
    ON delivery_status (order_id);

CREATE INDEX idx_location_history_order
    ON location_history (order_id);

CREATE INDEX idx_location_history_recorded
    ON location_history (order_id, recorded_at DESC);