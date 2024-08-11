SET search_path TO task_db,public;

-- Insert Users
INSERT INTO users (first_name, last_name, email, password) VALUES
                                                               ('John', 'Doe', 'john.doe@email.com', '$2a$10$Y8Efr5dH0frGcyNdIpWHFO1TEtbdfLaPBpeu.B/PkMVGorw4VwSg2'),
                                                               ('Jane', 'Smith', 'jane.smith@email.com', '$2a$10$Y8Efr5dH0frGcyNdIpWHFO1TEtbdfLaPBpeu.B/PkMVGorw4VwSg2');

-- Assign Roles to Users
INSERT INTO user_roles (user_id, role) VALUES
                                           (1, 'ROLE_USER'),
                                           (1, 'ROLE_ADMIN'),
                                           (2, 'ROLE_USER');

-- Insert Tasks
INSERT INTO task (title, description, status, priority, author_id, executor_id) VALUES
                                                                                    ('First Task', 'This is the first task description', 1, 1, 1, 2),
                                                                                    ('Second Task', 'This is the second task description', 2, 2, 2, 1);

-- Insert Comments
INSERT INTO comment (comment, author_id, task_id) VALUES
                                                      ('This is a comment on the first task by John', 1, 1),
                                                      ('This is another comment on the first task by Jane', 2, 1),
                                                      ('This is a comment on the second task by Jane', 2, 2);