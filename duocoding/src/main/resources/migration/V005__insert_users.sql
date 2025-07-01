INSERT INTO user (name, surname, email, username, password, birthday, dni, role, active)
VALUES ('SuperAdmin', 'SuperAdmin', 'super@admin.com', 'super', SHA2('super', 256), '1990-01-01 00:00:00', '12345678X', 'SUPER', TRUE);

INSERT INTO user (name, surname, email, username, password, birthday, dni, role, active)
VALUES ('Admin', 'Admin', 'admin@admin.com', 'admin', SHA2('admin', 256), '1990-01-01 00:00:00', '12345678X', 'ADMIN', TRUE);


INSERT INTO user (name, surname, email, username, password, birthday, dni, role, active)
VALUES ('Juan', 'Pérez', 'juan.perez@example.com', 'juanp', SHA2('1234', 256), '1990-01-01 00:00:00', '12345678X', 'PUPIL', TRUE);

INSERT INTO subject (name, description, active) VALUES
('Matemáticas', 'Asignatura de matemáticas básicas', TRUE),
('Historia', 'Historia mundial y local', TRUE),
('Programación', 'Introducción a la programación', TRUE);

INSERT INTO user (name, surname, email, username, password, birthday, dni, role, active) VALUES
 ('Pedro', 'González', 'pedro.g@example.com', 'pedrog', SHA2('profesor1', 256), '1980-05-12', '12345678A', 'TEACHER', TRUE),
 ('Laura', 'Martínez', 'laura.m@example.com', 'lauram', SHA2('profesor2', 256), '1982-09-21', '87654321B', 'TEACHER', TRUE);

INSERT INTO user (name, surname, email, username, password, birthday, dni, role, active) VALUES
 ('Carlos', 'Sánchez', 'carlos.s@example.com', 'carloss', SHA2('alumno1', 256), '2005-03-15', '11112222C', 'PUPIL', TRUE),
 ('Ana', 'Ruiz', 'ana.r@example.com', 'anar', SHA2('alumno2', 256), '2006-07-30', '33334444D', 'PUPIL', TRUE),
 ('Luis', 'Fernández', 'luis.f@example.com', 'luisf', SHA2('alumno3', 256), '2005-12-01', '55556666E', 'PUPIL', TRUE);

INSERT INTO user_subject (user_id, subject_id) VALUES
(4, 1),
(5, 2),
(5, 3);

INSERT INTO user_subject (user_id, subject_id) VALUES
(6, 1),
(6, 3),
(7, 2),
(7, 3),
(8, 1);


INSERT INTO test_execution (test_id, user_id, date, start_time, finish_time, result, notes, active) VALUES
(1, 3, '2025-06-10', '2025-06-10 09:00:00', '2025-06-10 09:30:00', 80.0, 'Buen desempeño en general', TRUE),
(1, 4, '2025-06-11', '2025-06-11 10:00:00', '2025-06-11 10:40:00', 75.5, 'Necesita repasar algunos conceptos', TRUE);


INSERT INTO test_execution_response (test_execution_id, question_id, answer, correct, notes, active) VALUES
(1, 1, '3', TRUE, 'Respuesta correcta', TRUE),
(1, 2, '2,3', TRUE, 'Respuestas correctas', TRUE),
(1, 3, 'framework,arquitectura', TRUE, 'Correcto', TRUE),
(1, 4, 'lavadora', TRUE, 'Correcto', TRUE),
(1, 5, 'z', TRUE, 'Correcto', TRUE);

INSERT INTO test_execution_response (test_execution_id, question_id, answer, correct, notes, active) VALUES
(2, 1, '2', FALSE, 'Respuesta incorrecta', TRUE),
(2, 2, '1,4', FALSE, 'Respuestas incorrectas', TRUE),
(2, 3, 'framework,arquitectura', TRUE, 'Correcto', TRUE),
(2, 4, 'lavadora', TRUE, 'Correcto', TRUE),
(2, 5, 'x', FALSE, 'Incorrecto', TRUE);


