-- Commands to create the roles
DROP ROLE IF EXISTS student;
DROP ROLE IF EXISTS instructor;
DROP ROLE IF EXISTS academics_office;
CREATE ROLE student;
CREATE ROLE instructor;
CREATE ROLE academics_office;


-- Creating a role dean_ug
DROP ROLE IF EXISTS dean_ug;
CREATE USER dean_ug WITH PASSWORD 'iitrpr' CREATEROLE;
GRANT academics_office TO dean_ug;

-- Revoking all privileges from the users by default
REVOKE ALL ON SCHEMA public FROM student;
REVOKE ALL ON SCHEMA public FROM instructor;
REVOKE ALL ON SCHEMA public FROM academics_office;

-- Revoking all privileges to functions from the users by default
REVOKE ALL ON ALL FUNCTIONS IN SCHEMA public FROM student;
REVOKE ALL ON ALL FUNCTIONS IN SCHEMA public FROM instructor;
REVOKE ALL ON ALL FUNCTIONS IN SCHEMA public FROM academics_office;

--Granting only execute privileges to the functions
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA PUBLIC TO student;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA PUBLIC TO instructor;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA PUBLIC TO academics_office;

-- Granting privilege to instructors and academics_office to offerings_id_seq
GRANT USAGE, SELECT ON SEQUENCE offerings_id_seq TO instructor;

-- Revoking create table privileges from the users by default
REVOKE CREATE ON SCHEMA public FROM student;
REVOKE CREATE ON SCHEMA public FROM instructor;
REVOKE CREATE ON SCHEMA public FROM academics_office;

-- Granting privileges to the users

-- For Table Catalog
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE catalog TO academics_office;
GRANT SELECT ON TABLE catalog TO student;
GRANT SELECT ON TABLE catalog TO instructor;

-- For Table Department
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE departments TO academics_office;
GRANT SELECT ON TABLE departments TO student;
GRANT SELECT ON TABLE departments TO instructor;

-- For Table Instructor
ALTER TABLE instructors ENABLE ROW LEVEL SECURITY;
CREATE POLICY admin_all ON instructors TO academics_office USING (true);
CREATE POLICY instructor_sel ON instructors FOR SELECT TO instructor USING (true);
CREATE POLICY student_sel ON instructors FOR SELECT TO student USING (true);
CREATE POLICY instructor_upd ON instructors FOR UPDATE TO instructor USING (current_user = email);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE instructors TO academics_office;
GRANT SELECT ON TABLE instructors TO student;
GRANT SELECT ON TABLE instructors TO instructor;
GRANT UPDATE (phone, address) ON TABLE instructors TO instructor;

-- For Table Student
ALTER TABLE students ENABLE ROW LEVEL SECURITY;
CREATE POLICY admin_all ON students TO academics_office USING (true);
CREATE POLICY instructor_sel ON students FOR SELECT TO instructor USING (true);
CREATE POLICY instructor_upd ON students FOR UPDATE TO instructor USING (true);
CREATE POLICY student_sel ON students FOR SELECT TO student USING (current_user = entry_no);
CREATE POLICY student_upd ON students FOR UPDATE TO student USING (current_user = entry_no);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE students TO academics_office;
GRANT SELECT, UPDATE ON TABLE students TO instructor;
GRANT UPDATE (phone, address) ON TABLE students TO student;
GRANT SELECT ON TABLE students TO student;



-- For Table prerequisite_default
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE prerequisite_default TO academics_office;
GRANT SELECT ON TABLE prerequisite_default TO student;
GRANT SELECT ON TABLE prerequisite_default TO instructor;

-- For Table offerings
ALTER TABLE offerings ENABLE ROW LEVEL SECURITY;
CREATE POLICY admin_all ON offerings TO academics_office USING (true);
CREATE POLICY student_sel ON offerings FOR SELECT TO student USING (true);
CREATE POLICY instructor_sel ON offerings FOR SELECT TO instructor USING (true);
CREATE POLICY instructor_upd ON offerings FOR UPDATE TO instructor USING (current_user IN (SELECT instructor FROM teaching_team WHERE id = offerings.id));
CREATE POLICY instructor_ins ON offerings FOR INSERT TO instructor WITH CHECK (current_user = coordinator);
CREATE POLICY instructor_del ON offerings FOR DELETE TO instructor USING (current_user = coordinator);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE offerings TO student;
GRANT SELECT ON TABLE offerings TO academics_office;
GRANT SELECT, INSERT, UPDATE ON TABLE offerings TO instructor;


-- For Table prerequisite
ALTER TABLE prerequisite ENABLE ROW LEVEL SECURITY;
CREATE POLICY admin_all ON prerequisite TO academics_office USING (true);
CREATE POLICY student_sel ON prerequisite FOR SELECT TO student USING (true);
CREATE POLICY instructor_sel ON prerequisite FOR SELECT TO instructor USING (true);
CREATE POLICY instructor_upd ON prerequisite FOR UPDATE TO instructor USING (current_user IN (SELECT instructor FROM teaching_team WHERE id = prerequisite.id));
CREATE POLICY instructor_ins ON prerequisite FOR INSERT TO instructor WITH CHECK (current_user IN (SELECT instructor FROM teaching_team WHERE id = prerequisite.id));
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE prerequisite TO academics_office;
GRANT SELECT ON TABLE prerequisite TO student;
GRANT SELECT, UPDATE, INSERT ON TABLE prerequisite TO instructor;


-- For Table course_category
ALTER TABLE course_category ENABLE ROW LEVEL SECURITY;
CREATE POLICY admin_all ON course_category TO academics_office USING (true);
CREATE POLICY student_sel ON course_category FOR SELECT TO student USING (true);
CREATE POLICY instructor_sel ON course_category FOR SELECT TO instructor USING (true);
CREATE POLICY instructor_upd ON course_category FOR UPDATE TO instructor USING (current_user IN (SELECT coordinator FROM offerings WHERE id=course_category.id));
CREATE POLICY instructor_ins ON course_category FOR INSERT TO instructor WITH CHECK (current_user IN (SELECT coordinator FROM offerings WHERE id=course_category.id));
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE course_category TO academics_office;
GRANT SELECT ON TABLE course_category TO student;
GRANT SELECT, UPDATE, DELETE, INSERT ON TABLE course_category TO instructor;


-- For Table enrollments
ALTER TABLE enrollments ENABLE ROW LEVEL SECURITY;
CREATE POLICY admin_all ON enrollments TO academics_office USING (true);
CREATE POLICY student_sel ON enrollments FOR SELECT TO student USING (current_user = entry_no);
CREATE POLICY instructor_sel ON enrollments FOR SELECT TO instructor USING (true);
CREATE POLICY student_ins ON enrollments FOR INSERT TO student WITH CHECK (current_user = entry_no AND grade='NA');
CREATE POLICY student_upd ON enrollments FOR UPDATE TO student USING (current_user = entry_no);
CREATE POLICY instructor_upd ON enrollments FOR UPDATE TO instructor USING (current_user IN (SELECT instructor FROM teaching_team WHERE id = enrollments.id));
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE enrollments TO academics_office;
GRANT SELECT ON TABLE enrollments TO instructor;
GRANT SELECT ON TABLE enrollments TO student;
GRANT INSERT ON TABLE enrollments TO student;
GRANT UPDATE(status) ON TABLE enrollments TO student;
GRANT UPDATE ON TABLE enrollments TO instructor;

-- For Table teaching_team
ALTER TABLE teaching_team ENABLE ROW LEVEL SECURITY;
CREATE POLICY admin_all ON teaching_team TO academics_office USING (true);
CREATE POLICY student_sel ON teaching_team FOR SELECT TO student USING (true);
CREATE POLICY instructor_sel ON teaching_team FOR SELECT TO instructor USING (true);
CREATE POLICY instructor_upd ON teaching_team FOR UPDATE TO instructor USING (current_user = (SELECT coordinator FROM offerings WHERE id=teaching_team.id));
CREATE POLICY instructor_ins ON teaching_team FOR INSERT TO instructor WITH CHECK (current_user = (SELECT coordinator FROM offerings WHERE id=teaching_team.id));
CREATE POLICY instructor_del ON teaching_team FOR DELETE TO instructor USING (current_user = (SELECT coordinator FROM offerings WHERE id=teaching_team.id));
GRANT SELECT ON TABLE teaching_team TO academics_office;
GRANT SELECT ON TABLE teaching_team TO student;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE teaching_team TO instructor;


-- For table academic_calendar
GRANT SELECT ON TABLE academic_calender TO student;
GRANT SELECT ON TABLE academic_calender TO instructor;
GRANT SELECT, UPDATE, DELETE, INSERT ON TABLE academic_calender TO academics_office;

-- For table curriculum
GRANT SELECT ON TABLE curriculum TO student;
GRANT SELECT ON TABLE curriculum TO instructor;
GRANT SELECT, UPDATE, DELETE, INSERT ON TABLE curriculum TO academics_office;

-- For the function calculate_gpa
GRANT EXECUTE ON FUNCTION calculate_cgpa(CHAR(11)) TO student;
GRANT EXECUTE ON FUNCTION calculate_cgpa(CHAR(11)) TO instructor;
GRANT EXECUTE ON FUNCTION calculate_cgpa(CHAR(11)) TO academics_office;

-- For the procedure update_cgpa
GRANT EXECUTE ON PROCEDURE update_cgpa(CHAR(11)) TO academics_office;
GRANT EXECUTE ON PROCEDURE update_cgpa(CHAR(11)) TO instructor;

-- For the procedure update_credit_limit
GRANT EXECUTE ON PROCEDURE update_credit_limit(CHAR(11), INT, semester) TO academics_office;

-- For table login_logs
GRANT SELECT, UPDATE, DELETE, INSERT ON TABLE login_logs TO student;
GRANT SELECT, UPDATE, DELETE, INSERT ON TABLE login_logs TO instructor;
GRANT SELECT, UPDATE, DELETE, INSERT ON TABLE login_logs TO academics_office;
GRANT EXECUTE ON PROCEDURE log_activity(is_login boolean) TO student;
GRANT EXECUTE ON PROCEDURE log_activity(is_login boolean) TO instructor;
GRANT EXECUTE ON PROCEDURE log_activity(is_login boolean) TO academics_office;