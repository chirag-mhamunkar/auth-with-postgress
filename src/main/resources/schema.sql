CREATE TABLE IF NOT EXISTS auth_permission (
	id SERIAL PRIMARY KEY,
	key VARCHAR(255) unique not null,
	name VARCHAR(255) not null,
	active BOOLEAN default true,
	createdAt timestamp default current_timestamp not null,
	updatedAt timestamp default current_timestamp not null
);

-- ALTER TABLE auth_permission ADD COLUMN if not exists old_ref_id VARCHAR(32) unique;


CREATE TABLE IF NOT EXISTS auth_role (
    id SERIAL PRIMARY KEY,
    key VARCHAR(255) unique not null,
    name VARCHAR(255) not null,
    tenant VARCHAR(255),
    active BOOLEAN default true,
    createdAt timestamp default current_timestamp not null,
    updatedAt timestamp default current_timestamp not null
);
-- ALTER TABLE auth_role ADD COLUMN if not exists old_ref_id VARCHAR(32) unique;

CREATE TABLE IF NOT EXISTS auth_user (
    id SERIAL PRIMARY KEY,
    userId VARCHAR(255) not null,
    client VARCHAR(255) not null,
    tenant VARCHAR(255) not null,
    createdAt timestamp default current_timestamp not null,
    updatedAt timestamp default current_timestamp not null
);

CREATE INDEX if not exists idx_auth_user_userid ON auth_user(userId);

-- ALTER TABLE auth_user ADD COLUMN if not exists old_ref_id VARCHAR(32) unique;


CREATE TABLE IF NOT EXISTS user_role_mapping (
    id SERIAL PRIMARY KEY,
    user_id int,
    role_id int,
    createdAt timestamp default current_timestamp not null,
    foreign key(user_id) references auth_user(id),
    foreign key(role_id) references auth_role(id)
);

CREATE INDEX if not exists idx_user_role_user_id ON user_role_mapping(user_id);


CREATE TABLE IF NOT EXISTS role_permission_mapping (
    id SERIAL PRIMARY KEY,
    role_id int,
    permission_id int,
    createdAt timestamp default current_timestamp not null,
    foreign key(role_id) references auth_role(id),
    foreign key(permission_id) references auth_permission(id)
);

CREATE INDEX if not exists idx_role_permission_role_id ON role_permission_mapping(role_id);
