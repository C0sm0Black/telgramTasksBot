-- liquibase formatted sql

-- changeset cosmoblack:create_table_notification_task

CREATE TABLE notification_task (

    id_task BIGINT PRIMARY KEY,
    id_chat BIGINT,
    notify_task_date_time TIMESTAMP,
    text TEXT NOT NULL,
    user_name TEXT

)
