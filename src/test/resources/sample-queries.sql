drop table auth_user;
drop table auth_role;
drop table auth_permission;



CREATE TABLE IF NOT EXISTS auth_user (id SERIAL PRIMARY KEY, client VARCHAR(255), userId VARCHAR(255), tenant VARCHAR(255), createdAt timestamp, updatedAt timestamp);
CREATE TABLE IF NOT EXISTS auth_role (id SERIAL PRIMARY KEY, key VARCHAR(255), name VARCHAR(255), tenant VARCHAR(255), active BOOLEAN,createdAt timestamp, updatedAt timestamp);
CREATE TABLE IF NOT EXISTS auth_permission (id SERIAL PRIMARY KEY, key VARCHAR(255), name VARCHAR(255), active BOOLEAN,createdAt timestamp, updatedAt timestamp);
CREATE TABLE IF NOT EXISTS user_role_mapping (id SERIAL PRIMARY KEY, user_id numeric, role_id numeric);
CREATE TABLE IF NOT EXISTS role_permission_mapping (id SERIAL PRIMARY KEY, role_id numeric, permission_id numeric);

select * from auth_user;
select * from auth_role;
select * from auth_permission;
select * from user_role_mapping;
select * from role_permission_mapping;


-- Adding auth user
insert into auth_user(client, userId, tenant, createdAt, updatedAt) values('client', '123456', 'tenant', current_timestamp, current_timestamp);

-- Adding auth role
insert into auth_role(key, name, tenant, active, createdAt, updatedAt) values('ROLE_KEY_1', 'ROLE_NAME_1', 'tenant', true, current_timestamp, current_timestamp);
insert into auth_role(key, name, tenant, active, createdAt, updatedAt) values('ROLE_KEY_2', 'ROLE_NAME_2', 'tenant', true, current_timestamp, current_timestamp);

-- Adding auth permission
insert into auth_permission(key, name, active, createdAt, updatedAt) values('PERMISSION_KEY_1', 'PERMISSION_NAME_1', true, current_timestamp, current_timestamp);
insert into auth_permission(key, name, active, createdAt, updatedAt) values('PERMISSION_KEY_2', 'PERMISSION_NAME_2', true, current_timestamp, current_timestamp);
insert into auth_permission(key, name, active, createdAt, updatedAt) values('PERMISSION_KEY_3', 'PERMISSION_NAME_3', true, current_timestamp, current_timestamp);
insert into auth_permission(key, name, active, createdAt, updatedAt) values('PERMISSION_KEY_4', 'PERMISSION_NAME_4', true, current_timestamp, current_timestamp);

insert into role_permission_mapping(role_id, permission_id) values(1, 1);
insert into role_permission_mapping(role_id, permission_id) values(1, 2);
insert into role_permission_mapping(role_id, permission_id) values(2, 3);
insert into role_permission_mapping(role_id, permission_id) values(2, 4);


-- For formatting use https://sqlformat.org/
-- JOIN query to fetch permissions for roles
SELECT *
FROM auth_role r
JOIN role_permission_mapping rpm ON r.id = rpm.role_id
JOIN auth_permission p ON p.id = rpm.permission_id;

insert into user_role_mapping(user_id, role_id) values(1, 1);
insert into user_role_mapping(user_id, role_id) values(1, 2);

--JOIN query to fetch all roles and their permissions for a user
SELECT *
FROM auth_user u
JOIN user_role_mapping urm ON u.id = urm.user_id
JOIN auth_role r ON r.id = urm.role_id
JOIN role_permission_mapping rpm ON r.id = rpm.role_id
JOIN auth_permission p ON p.id = rpm.permission_id
WHERE u.id = 1;

--JOIN with selected fields
SELECT
u.id as user_id,
u.client as user_client,
u.userid as user_partner_id,
u.tenant as user_tenant,
u.createdat as user_created_at,
u.updatedat as user_updated_at,
r.id as role_id,
r.key as role_key,
r.name as role_name,
r.tenant as role_tenant,
r.active as role_active,
r.createdat as role_created_at,
r.updatedat as role_updated_at,
p.id as permission_id,
p.key as permission_key,
p.name as permission_name,
p.active as permission_active,
p.createdat as permission_created_at,
p.updatedat as permission_updated_at
FROM auth_user u
JOIN user_role_mapping urm ON u.id = urm.user_id
JOIN auth_role r ON r.id = urm.role_id
JOIN role_permission_mapping rpm ON r.id = rpm.role_id
JOIN auth_permission p ON p.id = rpm.permission_id
WHERE u.id = 1;


--query to find duplicate users
select userid, client, tenant, count(id) from auth_user group by userid, client, tenant having count(id) > 1;



