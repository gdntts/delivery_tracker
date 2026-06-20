-- Rename enum type
ALTER TYPE delivery_status_enum RENAME TO order_status;

-- Rename enum values to UPPER_SNAKE_CASE
ALTER TYPE order_status RENAME VALUE 'pending' TO 'PENDING';
ALTER TYPE order_status RENAME VALUE 'accepted' TO 'ACCEPTED';
ALTER TYPE order_status RENAME VALUE 'in_transit' TO 'IN_TRANSIT';
ALTER TYPE order_status RENAME VALUE 'delivered' TO 'DELIVERED';
ALTER TYPE order_status RENAME VALUE 'cancelled' TO 'CANCELLED';

-- Update column defaults to match renamed enum values
ALTER TABLE orders
    ALTER COLUMN status SET DEFAULT 'PENDING';
ALTER TABLE delivery_status
    ALTER COLUMN status SET DEFAULT 'IN_TRANSIT';

-- Rename delivery_id to courier_id in orders
ALTER TABLE orders RENAME COLUMN delivery_id TO courier_id;

-- Drop trigger before renaming the table it depends on
DROP TRIGGER trg_location_updated ON delivery_status;
DROP FUNCTION notify_location_updated();

-- Rename table
ALTER TABLE delivery_status RENAME TO delivery_tracking;

-- Recreate trigger on renamed table
CREATE
OR REPLACE FUNCTION notify_location_updated()
    RETURNS TRIGGER AS $$
BEGIN
    PERFORM
pg_notify(
            'location_updated',
            json_build_object(
                    'order_id', NEW.order_id,
                    'lat', NEW.lat,
                    'lng', NEW.lng,
                    'status', NEW.status
            )::text
    );
RETURN NEW;
END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER trg_location_updated
    AFTER UPDATE
    ON delivery_tracking
    FOR EACH ROW EXECUTE FUNCTION notify_location_updated();

-- Rename index
ALTER
INDEX idx_delivery_status_order RENAME TO idx_delivery_tracking_order;
