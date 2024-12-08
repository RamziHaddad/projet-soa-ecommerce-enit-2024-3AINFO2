CREATE TABLE clients (
                         idClient UUID PRIMARY KEY,        -- Primary key with auto-increment
                         firstName VARCHAR(255),             -- First name of the client
                         lastName VARCHAR(255),              -- Last name of the client
                         email VARCHAR(255) UNIQUE,           -- Email address (unique constraint)
                         phone VARCHAR(50)             ,       -- Phone number
                         numero_cart_bancaire NUMERIC(19, 0),                  -- Credit card number (up to 19 digits)
                         code_secret CHAR(4)
);






