--CREATE USER test WITH PASSWORD 'test' CREATEDB;
CREATE DATABASE ecom_test
    WITH
    OWNER = admin_ec_user
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;