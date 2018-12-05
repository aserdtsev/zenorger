--liquibase formatted sql

--preconditions onFail:HALT onError:HALT
set search_path to zenorger;

--changeset Andrey Serdtsev:init-schema
create table if not exists service_user (
  id uuid not null constraint service_user_pk primary key,
  created_at timestamp not null,
  login text not null,
  password text not null
);

create unique index if not exists service_user_login_ix on service_user (login);

create table if not exists organizer (
  id uuid not null constraint organizer_pk primary key,
  created_at timestamp not null,
  user_id uuid not null constraint organizer_user_fk references service_user,
  name text
);
