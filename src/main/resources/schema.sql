CREATE TABLE IF NOT EXISTS auth_user (id SERIAL PRIMARY KEY, client VARCHAR(255), userId VARCHAR(255), tenant VARCHAR(255), createdAt timestamp, updatedAt timestamp);
CREATE TABLE IF NOT EXISTS auth_role (id SERIAL PRIMARY KEY, key VARCHAR(255) unique, name VARCHAR(255), tenant VARCHAR(255), active BOOLEAN,createdAt timestamp, updatedAt timestamp);

CREATE TABLE IF NOT EXISTS auth_permission (id SERIAL PRIMARY KEY, key VARCHAR(255) unique, name VARCHAR(255), active BOOLEAN,createdAt timestamp, updatedAt timestamp);

CREATE TABLE IF NOT EXISTS user_role_mapping (id SERIAL PRIMARY KEY, user_id numeric, role_id numeric);
CREATE TABLE IF NOT EXISTS role_permission_mapping (id SERIAL PRIMARY KEY, role_id numeric, permission_id numeric);