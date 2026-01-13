-- liquibase formatted sql

-- changeset vladfilyushkin:1
CREATE TABLE IF NOT EXISTS users (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(255),
                       surname VARCHAR(255),
                       birth_date TIMESTAMP,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       status BOOLEAN DEFAULT true,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- changeset vladfilyushkin:2
CREATE TABLE IF NOT EXISTS payment_cards (
                               id BIGSERIAL PRIMARY KEY,
                               number BIGINT NOT NULL,
                               holder VARCHAR(255) NOT NULL,
                               expiration_date TIMESTAMP NOT NULL,
                               status BOOLEAN DEFAULT true,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               user_id BIGINT NOT NULL,
                               CONSTRAINT fk_payment_cards_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
