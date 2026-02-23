INSERT INTO faculty (fac_name, description, areas_of_study, address) VALUES
	('Факультет информационных технологий',
	 'Подготовка специалистов в области IT',
	 ARRAY['Программирование','Кибербезопасность','Data Science'],
	 'г. Москва, ул. Ленина, 10'),
	
	('Факультет экономики',
	 'Подготовка экономистов и аналитиков',
	 ARRAY['Экономика','Финансы','Менеджмент'],
	 'г. Москва, ул. Пушкина, 15');

INSERT INTO rooms (faculty_id, room_number, max_people) VALUES
	(1, 101, 50),
	(1, 102, 30),
	(2, 201, 40),
	(2, 202, 35);

INSERT INTO teachers (first_name, surname, patronymic, contacts) VALUES
	('Иван', 'Петров', 'Сергеевич',
	 '{"email":"petrov@uni.ru","phone":"+79990000001"}'),
	
	('Мария', 'Сидорова', 'Александровна',
	 '{"email":"sidorova@uni.ru","phone":"+79990000002"}'),
	
	('Алексей', 'Иванов', 'Дмитриевич',
	 '{"email":"ivanov@uni.ru","phone":"+79990000003"}');

INSERT INTO study_groups (faculty_id, group_number, stream, cafedra) VALUES
	(1, 101, 1, 'Кафедра программной инженерии'),
	(1, 102, 1, 'Кафедра кибербезопасности'),
	(2, 201, 2, 'Кафедра финансов');

INSERT INTO courses (faculty_id, course_name, description, obligatory,
	 duration, format, course_type, intensity,
	 n_people, max_people) VALUES
	(1, 'Базы данных',
	 'Изучение SQL и проектирования БД',
	 true, 72, 'Лекция', 'Offline', 3, 0, 50),
	
	(1, 'Кибербезопасность',
	 'Основы защиты информации',
	 false, 48, 'Семинар', 'Online', 2, 0, 40),
	
	(2, 'Микроэкономика',
	 'Базовый курс по микроэкономике',
	 true, 60, 'Лекция', 'Offline', 3, 0, 40);

INSERT INTO teachers_courses (course_id, teacher_id) VALUES
	(1, 1),
	(2, 2),
	(3, 3);

INSERT INTO courses_groups (course_id, group_id) VALUES
	(1, 1),
	(1, 2),
	(2, 2),
	(3, 3);

INSERT INTO classes
(course_id, room_id, course_type, time_start, time_end, day_of_week)
VALUES
	(1, 1, 'Offline', '09:00', '10:30', 'Понедельник'),
	(3, 3, 'Offline', '11:00', '12:30', 'Вторник');

INSERT INTO classes
(course_id, room_id, course_type, time_start, time_end, day_of_week)
VALUES
	(2, NULL, 'Online', '14:00', '15:30', 'Среда');

INSERT INTO teachers_classes (class_id, teacher_id)
VALUES
	(1, 1),
	(2, 3),
	(3, 2);

INSERT INTO students
(student_name, surname, patronymic,
 faculty_id, area_of_study, stream,
 group_id, study_grade,
 year_of_studying, admission_year)
VALUES
	('Андрей', 'Кузнецов', 'Игоревич',
	 1, 'Программирование', 1,
	 1, 'Бакалавриат',
	 2, 2024),
	
	('Ольга', 'Смирнова', 'Павловна',
	 1, 'Кибербезопасность', 1,
	 2, 'Бакалавриат',
	 1, 2025),
	
	('Дмитрий', 'Орлов', 'Сергеевич',
	 2, 'Финансы', 2,
	 3, 'Бакалавриат',
	 3, 2023);

INSERT INTO students_courses
(course_id, student_id, year_of_studying, grade)
VALUES
	(1, 1, 2, 'Отлично'),
	(2, 2, 1, 'Хорошо'),
	(3, 3, 3, 'Отлично');