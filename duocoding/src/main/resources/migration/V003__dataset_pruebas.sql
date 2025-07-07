
INSERT INTO subject (id, name, description, active) VALUES
(1, 'Programación I', 'Introducción a la programación en Java.', TRUE),
(2, 'Bases de Datos', 'Modelado y consultas SQL.', TRUE),
(3, 'Desarrollo Web', 'HTML, CSS y JavaScript.', TRUE),
(4, 'Sistemas Operativos', 'Conceptos básicos de SO.', TRUE),
(5, 'Algoritmia', 'Estructuras de datos y algoritmos.', TRUE),
(6, 'Ingeniería del Software', 'Conceptos de diseño y mantenimiento de software.', TRUE);

INSERT INTO user (id, name, surname, email, username, password, birthday, dni, role, active) VALUES
(1, 'Juan', 'Pérez', 'juanp@example.com', 'juanp', SHA2("profe1", 256), '1980-01-01', '12345678A', 'TEACHER', TRUE),
(2, 'Laura', 'García', 'laurag@example.com', 'laurag', SHA2("profe2", 256), '1985-05-20', '87654321B', 'TEACHER', TRUE),
(3, 'Carlos', 'Ramírez', 'carlosr@example.com', 'carlosr', SHA2("profe3", 256), '1983-09-15', '23456789C', 'TEACHER', TRUE),
(4, 'Ana', 'López', 'ana@example.com', 'ana', SHA2("admin", 256), '1990-12-12', '11112222D', 'ADMIN', TRUE),
(5, 'Lucía', 'Sánchez', 'lucias@example.com', 'lucias', SHA2("superadmin", 256), '1975-04-04', '33334444E', 'SUPERADMIN', TRUE),
(6, 'Pedro', 'Gómez', 'pedrog@example.com', 'pedrog', SHA2('alumno1', 256), '2005-01-01', 'DNI1', 'PUPIL', TRUE),
(7, 'María', 'Díaz', 'mariad@example.com', 'mariad', SHA2('alumno2', 256), '2005-01-02', 'DNI2', 'PUPIL', TRUE),
(8, 'Luis', 'Torres', 'luist@example.com', 'luist', SHA2('alumno3', 256), '2005-01-03', 'DNI3', 'PUPIL', TRUE),
(9, 'Elena', 'Ruiz', 'elenar@example.com', 'elenar', SHA2('alumno4', 256), '2005-01-04', 'DNI4', 'PUPIL', TRUE),
(10, 'Javier', 'Molina', 'javierm@example.com', 'javierm', SHA2('alumno5', 256), '2005-01-05', 'DNI5', 'PUPIL', TRUE),
(11, 'Nerea', 'Cano', 'nerea@example.com', 'nerea', SHA2('alumno6', 256), '2005-01-06', 'DNI6', 'PUPIL', TRUE),
(12, 'Alberto', 'Vega', 'albertov@example.com', 'albertov', SHA2('alumno7', 256), '2005-01-07', 'DNI7', 'PUPIL', TRUE),
(13, 'Sara', 'Navarro', 'saran@example.com', 'saran', SHA2('alumno8', 256), '2005-01-08', 'DNI8', 'PUPIL', TRUE),
(14, 'Miguel', 'Ortega', 'miguel@example.com', 'miguel', SHA2('alumno9', 256), '2005-01-09', 'DNI9', 'PUPIL', TRUE),
(15, 'Claudia', 'Castro', 'claudia@example.com', 'claudia', SHA2('alumno10', 256), '2005-01-10', 'DNI10', 'PUPIL', TRUE);

INSERT INTO question_cat (id, type, description, answer, active) VALUES
(1, 'MONOSELECTION', '¿Qué lenguaje se utiliza comúnmente para desarrollo web en el navegador?', 'JavaScript', TRUE),
(2, 'FREETEXT', 'Explica qué es una base de datos relacional.', 'Una base de datos que organiza datos en tablas con relaciones.', TRUE),
(3, 'CODE', 'Escribe un bucle for que imprima los números del 1 al 5 en Java.', 'for(int i=1;i<=5;i++){System.out.println(i);}', TRUE);


INSERT INTO response_cat (id, description, response_order, question_id, active) VALUES
(1, 'Java', 1, 1, TRUE),
(2, 'Python', 2, 1, TRUE),
(3, 'JavaScript', 3, 1, TRUE),
(4, 'C++', 4, 1, TRUE);

INSERT INTO test (id, name, description, subject_id, active) VALUES
(1, 'Test de JavaScript', 'Prueba básica sobre JavaScript.', 3, TRUE),
(2, 'Test de Bases de Datos', 'Prueba sobre conceptos relacionales.', 2, TRUE);


INSERT INTO question (id, type, description, question_order, answer, test_id, active) VALUES
(1, 'MONOSELECTION', '¿Qué lenguaje se utiliza comúnmente para desarrollo web en el navegador?', 1, 'JavaScript', 1, TRUE),
(2, 'FREETEXT', 'Explica qué es una base de datos relacional.', 2, 'Una base de datos que organiza datos en tablas con relaciones.', 2, TRUE);

INSERT INTO response (id, description, response_order, question_id, active) VALUES
(1, 'Java', 1, 1, TRUE),
(2, 'Python', 2, 1, TRUE),
(3, 'JavaScript', 3, 1, TRUE),
(4, 'C++', 4, 1, TRUE);


INSERT INTO test_subject (id, test_id, subject_id) VALUES
(1, 1, 3),
(2, 2, 2);


INSERT INTO user_subject (id, user_id, subject_id) VALUES
(1, 6, 3),
(2, 7, 3),
(3, 8, 2),
(4, 9, 2),
(5, 10, 2),
(6,1,1),
(7, 1, 2),
(8, 1, 3),
(9,1,4),
(10,1,5),
(11,1,6);

INSERT INTO test_execution (id, test_id, user_id, date, start_time, finish_time, result, notes, active) VALUES
(1, 1, 6, '2025-07-01', '2025-07-01 10:00:00', '2025-07-01 10:30:00', 85.5, 'Ejecutado correctamente', TRUE),
(2, 2, 8, '2025-07-02', '2025-07-02 11:00:00', '2025-07-02 11:40:00', 75.0, 'Ejecutado correctamente', TRUE);


INSERT INTO test_execution_response (id, test_execution_id, question_id, answer, correct, notes, active) VALUES
(1, 1, 1, 'JavaScript', TRUE, 'Buena respuesta', TRUE),
(2, 2, 2, 'Una base de datos relacional almacena datos en tablas.', TRUE, 'Respuesta completa', TRUE);