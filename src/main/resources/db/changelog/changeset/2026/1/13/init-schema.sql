-- liquibase formatted sql

-- changeset vladfilyushkin:1
CREATE TABLE IF NOT EXISTS users (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       surname VARCHAR(255) NOT NULL,
                       birth_date TIMESTAMP NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       active BOOLEAN DEFAULT true,
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
-- changeset vladfilyushkin:3
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_name_surname ON users(name, surname);
CREATE INDEX IF NOT EXISTS idx_cards_user_id ON payment_cards(user_id);
