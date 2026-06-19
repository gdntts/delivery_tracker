CREATE TABLE location_history
(
    id          UUID PRIMARY KEY        DEFAULT gen_random_uuid(),
    order_id    UUID           NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    lat         NUMERIC(10, 7) NOT NULL,
    lng         NUMERIC(10, 7) NOT NULL,
    recorded_at TIMESTAMPTZ    NOT NULL DEFAULT now()
);