CREATE
OR REPLACE FUNCTION notify_location_updated()
RETURNS TRIGGER AS $$
BEGIN
  PERFORM
pg_notify(
    'location_updated',
    json_build_object(
      'order_id', NEW.order_id,
      'lat',      NEW.lat,
      'lng',      NEW.lng,
      'status',   NEW.status
    )::text
  );
RETURN NEW;
END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER trg_location_updated
    AFTER UPDATE
    ON delivery_status
    FOR EACH ROW EXECUTE FUNCTION notify_location_updated();