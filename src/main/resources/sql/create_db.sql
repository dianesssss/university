CREATE TABLE faculties (
    faculty_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    address TEXT
);

CREATE TABLE groups_table (
    group_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE,
    study_year INT NOT NULL CHECK (study_year BETWEEN 1 AND 6),
    faculty_id BIGINT REFERENCES faculties(faculty_id) ON DELETE SET NULL
);

CREATE TABLE teachers (
    teacher_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    surname VARCHAR(50) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    patronymic VARCHAR(50),
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(30)
);

CREATE TABLE classrooms (
    classroom_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    room_number VARCHAR(20) NOT NULL UNIQUE,
    capacity INT NOT NULL CHECK (capacity > 0),
    faculty_id BIGINT REFERENCES faculties(faculty_id) ON DELETE SET NULL
);

CREATE TABLE courses (
    course_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(150) NOT NULL UNIQUE,
    course_type VARCHAR(20) NOT NULL CHECK (course_type IN ('MANDATORY', 'SPECIAL')),
    max_students INT CHECK (max_students > 0),
    free_places INT CHECK (free_places >= 0),
    description TEXT,
    teacher_id BIGINT REFERENCES teachers(teacher_id) ON DELETE SET NULL
);

CREATE TABLE students (
    student_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    surname VARCHAR(50) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    patronymic VARCHAR(50),
    birth_date DATE NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    group_id BIGINT NOT NULL REFERENCES groups_table(group_id) ON DELETE RESTRICT
);

CREATE TABLE lessons (
    lesson_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    course_id BIGINT NOT NULL REFERENCES courses(course_id) ON DELETE CASCADE,
    classroom_id BIGINT NOT NULL REFERENCES classrooms(classroom_id) ON DELETE RESTRICT,
    teacher_id BIGINT NOT NULL REFERENCES teachers(teacher_id) ON DELETE RESTRICT,
    lesson_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    CHECK (end_time > start_time)
);

CREATE TABLE lesson_groups (
    lesson_group_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    lesson_id BIGINT NOT NULL REFERENCES lessons(lesson_id) ON DELETE CASCADE,
    group_id BIGINT NOT NULL REFERENCES groups_table(group_id) ON DELETE CASCADE,
    UNIQUE (lesson_id, group_id)
);

CREATE TABLE student_courses (
    student_course_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    student_id BIGINT NOT NULL REFERENCES students(student_id) ON DELETE CASCADE,
    course_id BIGINT NOT NULL REFERENCES courses(course_id) ON DELETE CASCADE,
    grade INT CHECK (grade BETWEEN 2 AND 5 OR grade IS NULL),
    enrolled_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (student_id, course_id)
);