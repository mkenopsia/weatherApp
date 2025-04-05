create schema if not exists users;

create table if not exists t_user
(
    id serial primary key,
    c_name varchar(255) not null,
    c_password varchar(255) not null,
    c_authority varchar not null
);