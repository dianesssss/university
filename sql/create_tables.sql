CREATE TABLE teachers (
	teacher_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	first_name VARCHAR(20),
	surname VARCHAR(20),
	patronymic VARCHAR(20),
	contacts jsonb
);

CREATE TABLE faculty (
	faculty_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	fac_name VARCHAR(100),
	description text,
	areas_of_study text[],
	address text
);

CREATE TABLE rooms (
	room_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	faculty_id INT NOT NULL REFERENCES faculty(faculty_id) ON DELETE RESTRICT,
	room_number INT NOT NULL,
	max_people INT,
	UNIQUE(faculty_id, room_number)
);

CREATE TYPE course_type AS ENUM('Online','Offline');
CREATE TYPE course_format AS ENUM('Лекция','Семинар');

CREATE TABLE courses (
	course_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	faculty_id INT NOT NULL REFERENCES faculty(faculty_id) ON DELETE RESTRICT,
	course_name text,
	description text,
	obligatory bool,
	duration INT,
	format course_format,
	course_type course_type,
	intensity INT,
	n_people INT DEFAULT 0,
	max_people INT,
	CHECK (n_people <= max_people)
);

CREATE TABLE teachers_courses (
	course_id INT NOT NULL REFERENCES courses(course_id) ON DELETE RESTRICT,
	teacher_id INT NOT NULL REFERENCES teachers(teacher_id) ON DELETE RESTRICT,
	PRIMARY KEY(course_id, teacher_id)
);

CREATE TYPE weekday AS ENUM('Понедельник', 'Вторник', 'Среда', 'Четверг',
							'Пятница', 'Суббота');

CREATE TABLE classes (
	class_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	course_id INT NOT NULL REFERENCES courses(course_id) ON DELETE RESTRICT,
	room_id INT REFERENCES rooms(room_id) ON DELETE RESTRICT,
	course_type course_type,
	time_start time,
	time_end time,
	CHECK (time_start < time_end),
	day_of_week weekday,
	CHECK (
		(room_id IS NULL AND course_type = 'Online')
		OR 
		(room_id IS NOT NULL AND course_type = 'Offline')
	)
);

CREATE TABLE study_groups (
	group_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    faculty_id INT NOT NULL REFERENCES faculty(faculty_id) ON DELETE RESTRICT,
	group_number INT NOT NULL,
	stream SMALLINT,
	cafedra VARCHAR(100),
	UNIQUE(faculty_id, group_number)
);

CREATE TABLE courses_groups (
	course_id INT NOT NULL REFERENCES courses(course_id) ON DELETE RESTRICT,
	group_id INT NOT NULL REFERENCES study_groups(group_id) ON DELETE RESTRICT,
	PRIMARY KEY(course_id, group_id)
);

CREATE TABLE teachers_classes (
	class_id INT NOT NULL REFERENCES classes(class_id) ON DELETE RESTRICT,
	teacher_id INT NOT NULL REFERENCES teachers(teacher_id) ON DELETE RESTRICT,
	PRIMARY KEY(class_id, teacher_id)
);

CREATE TYPE study_grades AS ENUM('Бакалавриат', 'Специалитет', 'Магистратура', 'Аспирантура');
CREATE TABLE students (
	student_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	student_name VARCHAR(20),
	surname VARCHAR(20),
	patronymic VARCHAR(20),
	faculty_id INT NOT NULL REFERENCES faculty(faculty_id) ON DELETE RESTRICT,
	area_of_study text,
	stream SMALLINT,
	group_id INT NOT NULL REFERENCES study_groups(group_id) ON DELETE RESTRICT,
	study_grade study_grades,
	year_of_studying SMALLINT,
	admission_year INT
);

CREATE TABLE students_courses (
	course_id INT NOT NULL REFERENCES courses(course_id) ON DELETE RESTRICT,
	student_id INT NOT NULL REFERENCES students(student_id) ON DELETE RESTRICT,
	PRIMARY KEY(course_id, student_id),
	year_of_studying INT,
	grade VARCHAR(30)
);
