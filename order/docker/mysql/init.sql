DO
$$
    BEGIN
        IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'db_order') THEN
            PERFORM dblink_connect('dbname=postgres');
            PERFORM dblink_exec('CREATE DATABASE db_order');
        END IF;
    END
$$;
