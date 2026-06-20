INSERT INTO orders (id, customer_id, status)
VALUES (gen_random_uuid(),
        gen_random_uuid(),
        'PENDING');