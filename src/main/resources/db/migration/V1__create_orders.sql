CREATE TYPE delivery_status_enum AS ENUM (
    'pending',
    'accepted',
    'in_transit',
    'delivered',
    'cancelled'
);

CREATE TABLE orders
(
    id          UUID PRIMARY KEY              DEFAULT gen_random_uuid(),
    customer_id UUID                 NOT NULL,
    delivery_id UUID,
    status      delivery_status_enum NOT NULL DEFAULT 'pending',
    created_at  TIMESTAMP            NOT NULL DEFAULT NOW()
);