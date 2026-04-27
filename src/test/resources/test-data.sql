INSERT INTO faculties (name, description, address) VALUES
('CMC', 'Computational mathematics and cybernetics', 'Leninskie Gory, 1'),
('MATH', 'Mechanics and mathematics', 'Leninskie Gory, 1');

INSERT INTO groups_table (name, study_year, faculty_id) VALUES
('103', 1, 1),
('104', 1, 1),
('201', 2, 1);

INSERT INTO teachers (surname, first_name, patronymic, email, phone) VALUES
('Ivanov', 'Ivan', 'Ivanovich', 'ivanov@msu.ru', '+79990000001'),
('Petrov', 'Petr', 'Petrovich', 'petrov@msu.ru', '+79990000002'),
('Sidorova', 'Anna', 'Sergeevna', 'sidorova@msu.ru', '+79990000003');

INSERT INTO classrooms (room_number, capacity, faculty_id) VALUES
('P-8', 120, 1),
('P-9', 60, 1),
('L-12', 40, 2);

INSERT INTO courses (name, course_type, max_students, free_places, description, teacher_id) VALUES
('Databases', 'MANDATORY', 100, 100, 'Mandatory database course', 1),
('Java', 'MANDATORY', 100, 100, 'Mandatory Java course', 2),
('Machine Learning', 'SPECIAL', 30, 28, 'ML special course', 3),
('Web Development', 'SPECIAL', 25, 24, 'Spring MVC special course', 2);

INSERT INTO students (surname, first_name, patronymic, birth_date, email, group_id) VALUES
('Smirnov', 'Aleksey', 'Dmitrievich', '2005-04-11', 'smirnov1@student.msu.ru', 1),
('Kuznetsova', 'Maria', 'Igorevna', '2005-07-21', 'kuznetsova1@student.msu.ru', 1),
('Orlov', 'Nikita', 'Andreevich', '2005-01-15', 'orlov1@student.msu.ru', 2),
('Fedorova', 'Elena', 'Pavlovna', '2004-10-03', 'fedorova1@student.msu.ru', 3);

INSERT INTO lessons (course_id, classroom_id, teacher_id, lesson_date, start_time, end_time) VALUES
(1, 1, 1, '2026-03-23', '09:00', '10:30'),
(2, 2, 2, '2026-03-23', '10:45', '12:15'),
(3, 2, 3, '2026-03-24', '13:00', '14:30'),
(4, 3, 2, '2026-03-25', '15:00', '16:30');

INSERT INTO lesson_groups (lesson_id, group_id) VALUES
(1, 1),
(1, 2),
(2, 1),
(2, 3);

INSERT INTO student_courses (student_id, course_id, grade) VALUES
(1, 1, NULL),
(2, 1, NULL),
(3, 1, NULL),
(1, 2, NULL),
(4, 2, 5),
(1, 3, NULL),
(2, 4, 4);
