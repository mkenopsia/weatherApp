create schema if not exists locations;

create table if not exists t_location
(
    id serial primary key,
    c_latitude decimal,
    c_longitude decimal,
    c_name varchar not null,
    id_user int not null references users.t_user (id)
);