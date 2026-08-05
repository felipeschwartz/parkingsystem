-- V1__create_initial_schema.sql

CREATE TABLE tb_users (
                          id BIGSERIAL PRIMARY KEY,
                          phone VARCHAR(255),
                          email VARCHAR(255),
                          street VARCHAR(255),
                          street_number VARCHAR(255),
                          complement VARCHAR(255),
                          city VARCHAR(255),
                          state VARCHAR(255),
                          zip VARCHAR(255),
                          country VARCHAR(255),
                          user_type VARCHAR(255),
                          user_profile VARCHAR(255),
                          password VARCHAR(255) NOT NULL,
                          created_at TIMESTAMP,
                          updated_at TIMESTAMP
);

CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL REFERENCES tb_users(id) ON DELETE CASCADE,
                            role VARCHAR(255) NOT NULL
);

CREATE TABLE user_individuals (
                                  id BIGINT PRIMARY KEY REFERENCES tb_users(id) ON DELETE CASCADE,
                                  cpf VARCHAR(11) NOT NULL UNIQUE,
                                  first_name VARCHAR(255),
                                  last_name VARCHAR(255),
                                  birth_date DATE
);

CREATE TABLE user_entities (
                               id BIGINT PRIMARY KEY REFERENCES tb_users(id) ON DELETE CASCADE,
                               cnpj VARCHAR(14) NOT NULL UNIQUE,
                               billing_contact VARCHAR(255),
                               corporate_name VARCHAR(255),
                               fantasy_name VARCHAR(255)
);

CREATE TABLE vehicle (
                         id BIGSERIAL PRIMARY KEY,
                         license_plate VARCHAR(255) NOT NULL UNIQUE,
                         type VARCHAR(20) NOT NULL,
                         user_id BIGINT REFERENCES tb_users(id),
                         created_at TIMESTAMP,
                         updated_at TIMESTAMP
);

CREATE TABLE parking_lot (
                             id BIGSERIAL PRIMARY KEY,
                             parking_lot_name VARCHAR(255),
                             street VARCHAR(255),
                             street_number VARCHAR(255),
                             complement VARCHAR(255),
                             city VARCHAR(255),
                             state VARCHAR(255),
                             zip VARCHAR(255),
                             country VARCHAR(255),
                             phone_number VARCHAR(255),
                             total_spaces INTEGER,
                             active BOOLEAN,
                             car_spaces INTEGER,
                             motorcycle_spaces INTEGER,
                             truck_spaces INTEGER,
                             created_at TIMESTAMP,
                             updated_at TIMESTAMP
);

CREATE TABLE parking_space (
                               id BIGSERIAL PRIMARY KEY,
                               floor VARCHAR(255),
                               position VARCHAR(255),
                               vehicle_type VARCHAR(20) NOT NULL,
                               status VARCHAR(20),
                               active BOOLEAN,
                               parking_lot_id BIGINT NOT NULL REFERENCES parking_lot(id),
                               created_at TIMESTAMP,
                               updated_at TIMESTAMP
);

CREATE TABLE reservation (
                             id BIGSERIAL PRIMARY KEY,
                             vehicle_id BIGINT REFERENCES vehicle(id),
                             parking_space_id BIGINT NOT NULL REFERENCES parking_space(id),
                             start_time TIMESTAMP,
                             end_time TIMESTAMP,
                             status INTEGER,
                             created_at TIMESTAMP,
                             updated_at TIMESTAMP
);

CREATE TABLE parking_session (
                                 id BIGSERIAL PRIMARY KEY,
                                 vehicle_id BIGINT REFERENCES vehicle(id),
                                 license_plate VARCHAR(20) NOT NULL,
                                 vehicle_type VARCHAR(20) NOT NULL,
                                 parking_space_id BIGINT NOT NULL REFERENCES parking_space(id),
                                 entry_time TIMESTAMP,
                                 exit_time TIMESTAMP,
                                 status VARCHAR(20),
                                 amount_charged DECIMAL(10,2),
                                 created_at TIMESTAMP,
                                 updated_at TIMESTAMP
);

CREATE TABLE payment (
                         id BIGSERIAL PRIMARY KEY,
                         parking_session_id BIGINT UNIQUE REFERENCES parking_session(id),
                         amount DECIMAL(10,2),
                         payment_date TIMESTAMP,
                         payment_method INTEGER,
                         payment_status INTEGER,
                         reference VARCHAR(255),
                         created_date TIMESTAMP,
                         updated_date TIMESTAMP
);

CREATE TABLE plan (
                      id BIGSERIAL PRIMARY KEY,
                      name VARCHAR(50),
                      active BOOLEAN,
                      created_at TIMESTAMP,
                      updated_at TIMESTAMP
);

CREATE TABLE plan_rate (
                           id BIGSERIAL PRIMARY KEY,
                           plan_id BIGINT REFERENCES plan(id),
                           vehicle_type VARCHAR(20) NOT NULL,
                           duration_months INTEGER NOT NULL,
                           monthly_price DECIMAL(10,2) NOT NULL,
                           discount_percent DECIMAL(10,2),
                           active BOOLEAN,
                           created_at TIMESTAMP,
                           updated_at TIMESTAMP
);

CREATE TABLE subscription_contract (
                                       id BIGSERIAL PRIMARY KEY,
                                       vehicle_id BIGINT NOT NULL REFERENCES vehicle(id),
                                       plan_id BIGINT NOT NULL REFERENCES plan(id),
                                       start_date DATE NOT NULL,
                                       end_date DATE,
                                       status VARCHAR(20) NOT NULL,
                                       user_id BIGINT NOT NULL REFERENCES tb_users(id),
                                       created_at TIMESTAMP,
                                       updated_at TIMESTAMP
);

CREATE TABLE hourly_rate (
                             id BIGSERIAL PRIMARY KEY,
                             vehicle_type VARCHAR(20) NOT NULL,
                             rate_per_hour DECIMAL(10,2) NOT NULL,
                             active BOOLEAN,
                             created_at TIMESTAMP,
                             updated_at TIMESTAMP
);