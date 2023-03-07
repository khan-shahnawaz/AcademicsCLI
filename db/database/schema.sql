-- Creates all the tables for the database

-- Table to store Course Catalog
CREATE TABLE catalog (
    code CHAR(5) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description text,
    credits float NOT NULL,
    L float NOT NULL,
    T float NOT NULL,
    P float NOT NULL,
    S float NOT NULL,
    C float NOT NULL,
    PRIMARY KEY(code)
);

-- Table to store the departments
CREATE TABLE departments (
    name VARCHAR(100) NOT NULL,
    dep_id VARCHAR(2) NOT NULL,
    PRIMARY KEY(dep_id)
);

-- Table to store instructor details
CREATE TABLE instructors (
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    address text NOT NULL,
    department VARCHAR(2) NOT NULL,
    PRIMARY KEY(email),
    FOREIGN KEY(department) REFERENCES departments(dep_id)
);

-- Table to store student details
CREATE TABLE students (
    entry_no VARCHAR(11) NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    department VARCHAR(2) NOT NULL,
    entry_year int NOT NULL,
    address text NOT NULL,
    program VARCHAR(100) NOT NULL,
    cgpa float NOT NULL,
    credit_limit float NOT NULL DEFAULT 24,
    advisor VARCHAR(100) NOT NULL,
    PRIMARY KEY(entry_no),
    FOREIGN KEY(department) REFERENCES departments(dep_id),
    FOREIGN KEY(advisor) REFERENCES instructors(email)
);

-- Creating a type for storing the grades
CREATE TYPE grade AS ENUM ('A', 'A-', 'B', 'B-', 'C', 'C-', 'D', 'E', 'NP','S', 'F', 'I', 'U', 'NF', 'W', 'NA');

-- Table to store the default prerequisite for a course
CREATE TABLE prerequisite_default (
    code CHAR(5) NOT NULL,
    prereq CHAR(5) NOT NULL,
    min_grade grade NOT NULL,
    FOREIGN KEY(code) REFERENCES catalog(code),
    FOREIGN KEY(prereq) REFERENCES catalog(code),
    PRIMARY KEY(code, prereq)
);

-- Creating a type for storing the semester
CREATE TYPE semester AS ENUM ('I', 'II', 'S');

-- Creating a type for storing the status of a course
CREATE TYPE course_status AS ENUM ('Proposed', 'Enrolling', 'Running', 'Completed', 'Cancelled');

-- Table to store the course offerings
CREATE TABLE offerings (
    id SERIAL NOT NULL UNIQUE,
    code CHAR(5) NOT NULL,
    semester semester NOT NULL,
    year int NOT NULL,
    section int NOT NULL,
    status course_status NOT NULL,
    department VARCHAR(2) NOT NULL,
    coordinator VARCHAR(100) NOT NULL,
    min_cgpa float NOT NULL,
    PRIMARY KEY(code, semester, year ,section),
    FOREIGN KEY(code) REFERENCES catalog(code),
    FOREIGN KEY(department) REFERENCES departments(dep_id),
    FOREIGN KEY(coordinator) REFERENCES instructors(email)
);

-- Table to store teaching team for a course offering
CREATE TABLE teaching_team (
    id INT NOT NULL,
    instructor VARCHAR(100) NOT NULL,
    is_coordinator boolean NOT NULL,
    PRIMARY KEY(id, instructor),
    FOREIGN KEY(id) REFERENCES offerings(id),
    FOREIGN KEY(instructor) REFERENCES instructors(email)
);


-- Table to store the prerequisite for a course offering
CREATE TABLE prerequisite (
    id INT NOT NULL,
    prereq CHAR(5) NOT NULL,
    min_grade grade NOT NULL,
    FOREIGN KEY(id) REFERENCES offerings(id),
    FOREIGN KEY(prereq) REFERENCES catalog(code),
    PRIMARY KEY(id, prereq)
);

-- Creating a type for storing the type of a course
CREATE TYPE course_types AS ENUM ('SC', 'SE', 'GE', 'PC', 'PE', 'HC', 'HE', 'II', 'CP', 'EC', 'OE');

-- Table to store the course category
CREATE TABLE course_category (
    id INT NOT NULL,
    type course_types NOT NULL,
    entry_year int NOT NULL,
    department VARCHAR(2) NOT NULL,
    program VARCHAR(100) NOT NULL,
    FOREIGN KEY(id) REFERENCES offerings(id),
    FOREIGN KEY(department) REFERENCES departments(dep_id)
);

-- Creating a type for storing the enrolment status
CREATE TYPE enrolment_status AS ENUM ('Enrolled',  'Audit', 'Withdrawn', 'Dropped by Student', 'Instructor Rejected');


-- Table to store the course enrollments
CREATE TABLE enrollments (
    id INT NOT NULL,
    entry_no VARCHAR(11) NOT NULL,
    grade grade NOT NULL DEFAULT 'NA',
    status enrolment_status NOT NULL,
    course_type course_types NOT NULL,
    PRIMARY KEY(id, entry_no),
    FOREIGN KEY(id) REFERENCES offerings(id),
    FOREIGN KEY(entry_no) REFERENCES students(entry_no)
);

-- Creating a type for storing academic event
CREATE TYPE academic_event AS ENUM ('Academic Session', 'Course Add/Drop', 'Grade Submission', 'Course Withdrawal/Audit');

-- Table to store the academic calendar
CREATE TABLE academic_calender(
    year int NOT NULL,
    semester semester NOT NULL,
    event academic_event NOT NULL,
    start_date date NOT NULL,
    end_date date NOT NULL,
    PRIMARY KEY(year, semester, event)
);


-- Table to store curriculum for different programs
CREATE TABLE curriculum (
    program VARCHAR(100) NOT NULL,
    course_type course_types NOT NULL,
    min_credits float NOT NULL,
    PRIMARY KEY(program, course_type)
);

CREATE TABLE login_logs (
    username text,
    time_stamp timestamp,
    activity text
);
CREATE OR REPLACE PROCEDURE log_activity(is_login boolean) AS $$
DECLARE
    activity text;
BEGIN
    IF is_login THEN
        activity = 'login';
    ELSE
        activity = 'logout';
    END IF;
    INSERT INTO login_logs VALUES (current_user, now(), activity);
END;
$$ LANGUAGE plpgsql;