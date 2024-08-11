CREATE DATABASE root;
ALTER ROLE root SET search_path TO task_db;

CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       first_name VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_roles (
                            user_id BIGINT,
                            role VARCHAR(255),
                            PRIMARY KEY (user_id, role),
                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE task (
                      id BIGSERIAL PRIMARY KEY,
                      title VARCHAR(255),
                      description varchar(255),
                      status SMALLINT,
                      priority SMALLINT,
                      author_id BIGINT,
                      executor_id BIGINT,
                      create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE SET NULL,
                      FOREIGN KEY (executor_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE comment (
                         id BIGSERIAL PRIMARY KEY,
                         comment varchar(255),
                         author_id BIGINT,
                         task_id BIGINT,
                         create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE SET NULL,
                         FOREIGN KEY (task_id) REFERENCES task(id) ON DELETE CASCADE
);