create table service_user (
    id uuid not null
        constraint service_user_id_pk primary key,
    login text not null,
    password text not null
);

create unique index service_user_login_ix on service_user(login);

create table organizer (
    id uuid not null
        constraint organizer_id_pk primary key,
    user_id uuid not null references service_user,
    name text
);

create table context (
    id uuid not null
        constraint context_id_pk primary key,
    organizer_id uuid not null references organizer,
    name text not null
);
create unique index context_organizer_id_name_uix on context(organizer_id, name);

create table tag (
    id uuid not null
        constraint tag_id_pk primary key,
    organizer_id uuid not null references organizer,
    name text not null
);
create unique index tag_organizer_id_name_uix on tag(organizer_id, name);

create table periodicity (
    id uuid not null
        constraint periodicity_id_pk primary key,
    organizer_id uuid not null references organizer,
    period text,
    period_qty int,
    repeat_qty int,
    start_date date,
    start_time time without time zone,
    finish_date date,
    next_date date
);

create table task (
    id uuid not null
        constraint task_id_pk primary key,
    organizer_id uuid not null references organizer,
    created_at timestamptz,
    name text not null,
    status text not null,
    description text,
    start_date date,
    start_time time without time zone,
    complete_date date,
    complete_time time without time zone,
    periodicity_id uuid references periodicity,
    is_project boolean,
    project_tasks_in_order boolean
);

create table project_task (
    project_id uuid references task,
    task_id uuid references task,
    index numeric not null,
    constraint project_task_project_id_task_id_pk
        primary key (project_id, task_id)
);
create unique index project_task_task_id_project_id_uix on project_task(task_id, project_id);

create table task_context (
    task_id uuid not null references task,
    context_id uuid not null references context,
    index numeric not null,
    constraint task_context_task_id_context_id_pk
        primary key (task_id, context_id)
);

create table context_task (
    context_id uuid not null references context,
    task_id uuid not null references task,
    index numeric not null,
    constraint context_task_context_id_task_id_index_pk
        primary key (context_id, task_id, index)
);

create table task_tag (
    task_id uuid not null references task,
    tag_id uuid not null references tag,
    index numeric not null,
    constraint task_tag_task_id_tag_id_pk
        primary key (task_id, tag_id)
);

create table comment (
    id uuid not null
        constraint comment_id_pk primary key,
    organizer_id uuid not null references organizer,
    task_id uuid not null references task,
    created_at timestamptz not null,
    content text not null
);
