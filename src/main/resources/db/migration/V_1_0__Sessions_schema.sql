create schema if not exists sessions;

create table if not exists t_session
(
    id serial primary key,
    c_uuid varchar not null,
    c_expires_at timestamp(6),
    id_user int not null references users.t_user (id)
);