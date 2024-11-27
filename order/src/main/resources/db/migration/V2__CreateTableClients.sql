/*-- Migration script to create the 't_clients' table
CREATE TABLE clients (
                           idClient SERIAL PRIMARY KEY,        -- Primary key with auto-increment
                           firstName VARCHAR(255),             -- First name of the client
                           lastName VARCHAR(255),              -- Last name of the client
                           email VARCHAR(255) UNIQUE,           -- Email address (unique constraint)
                           phone VARCHAR(50)                    -- Phone number
);
*/



