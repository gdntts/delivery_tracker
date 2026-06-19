CREATE TABLE delivery_status
(
    id         UUID PRIMARY KEY              DEFAULT gen_random_uuid(),
    order_id   UUID                 NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    lat        NUMERIC(10, 7)       NOT NULL,
    lng        NUMERIC(10, 7)       NOT NULL,
    status     delivery_status_enum NOT NULL DEFAULT 'in_transit',
    updated_at TIMESTAMPTZ          NOT NULL DEFAULT NOW(),
    UNIQUE (order_id)
);