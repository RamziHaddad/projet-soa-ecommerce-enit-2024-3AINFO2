-- Migration script to create the 'orders' table


CREATE TABLE orders (
                        id SERIAL PRIMARY KEY,                        -- Primary key with auto-increment
                        id_cart BIGINT,                               -- Reference to a cart
                        order_status VARCHAR(255),                   -- Order status
                        price NUMERIC(10, 2),                        -- Price field (BigDecimal equivalent)
                        idClient BIGINT,                            -- Reference to a client
                        quantity INT,                                -- Quantity field
                        order_number VARCHAR(255),                  -- Order number
                        payment_verification BOOLEAN,               -- Payment verification status
                        price_verification BOOLEAN,                 -- Price verification status
                        delivery_verification BOOLEAN,              -- Delivery verification status
                        stock_verification BOOLEAN,                 -- Stock verification status
                        sent_to_shipment_at TIMESTAMP,              -- Shipment sent timestamp
                        received_at TIMESTAMP,                      -- Order received timestamp
                        coupon VARCHAR(255),                        -- Coupon code field
                        CONSTRAINT fk_client FOREIGN KEY (idClient) REFERENCES clients (idClient) -- Foreign key to clients
);

