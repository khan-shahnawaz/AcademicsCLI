-- Function to calculate the CGPA of a student
CREATE OR REPLACE FUNCTION calculate_cgpa(entry_num CHAR(11))
RETURNS float AS $$
DECLARE
    total_credits float DEFAULT 0;
    total_grade_points float DEFAULT 0;
    cgpa_var float DEFAULT 0;
    credits float;
    grade grade;
    student record;
    code_var CHAR(5);
BEGIN
    FOR student IN SELECT * FROM enrollments WHERE entry_no = entry_num
    LOOP
        SELECT code INTO code_var FROM offerings WHERE student.id = id;
        SELECT C INTO credits FROM catalog WHERE code = code_var;
        grade = student.grade;
        IF grade = 'A' THEN
            total_grade_points = total_grade_points + 10*credits;
            total_credits = total_credits + credits;
        ELSIF grade = 'A-' THEN
            total_grade_points = total_grade_points + 9*credits;
            total_credits = total_credits + credits;
        ELSIF grade = 'B' THEN
            total_grade_points = total_grade_points + 8*credits;
            total_credits = total_credits + credits;
        ELSIF grade = 'B-' THEN
            total_grade_points = total_grade_points + 7*credits;
            total_credits = total_credits + credits;
        ELSIF grade = 'C' THEN
            total_grade_points = total_grade_points + 6*credits;
            total_credits = total_credits + credits;
        ELSIF grade = 'C-' THEN
            total_grade_points = total_grade_points + 5*credits;
            total_credits = total_credits + credits;
        ELSIF grade = 'D' THEN
            total_grade_points = total_grade_points + 4*credits;
            total_credits = total_credits + credits;
        END IF;
    END LOOP;
    IF total_credits = 0 THEN
        RETURN 0;
    END IF;
    cgpa_var = total_grade_points/total_credits;
    return cgpa_var;
END;
$$ LANGUAGE plpgsql;


-- Procedure to update the CGPA to the student profile
CREATE OR REPLACE PROCEDURE update_cgpa(entry_num CHAR(11)) SECURITY DEFINER 
AS $$
DECLARE
    cgpa_var float;
BEGIN
    cgpa_var = calculate_cgpa(entry_num);
    UPDATE students SET cgpa = cgpa_var WHERE entry_no = entry_num;
END;
$$ LANGUAGE plpgsql;

-- Procedure to update credit_limit of a student
CREATE OR REPLACE PROCEDURE update_credit_limit(entry_num CHAR(11), year_par INT, semester_par semester)
AS $$
DECLARE
    total_credits float;
    prev_year INT;
    prev_prev_year INT;
    prev_sem semester;
    prev_prev_sem semester;
BEGIN
    IF semester_par = 'I' THEN
        prev_year = year_par - 1;
        prev_sem = 'II';
        prev_prev_year = year_par - 1;
        prev_prev_sem = 'I';
    ELSE
        prev_year = year_par;
        prev_sem = 'I';
        prev_prev_year = year_par - 1;
        prev_prev_sem = 'II';
    END IF;
    SELECT SUM(C) FROM enrollments, offerings, catalog  WHERE ((year = prev_year AND semester = prev_sem) OR (year = prev_prev_year AND semester = prev_prev_sem)) AND entry_no = entry_num INTO total_credits;
    total_credits = total_credits/2;
    UPDATE students SET credit_limit = 1.25*total_credits WHERE entry_no = entry_num;
END;
$$ LANGUAGE plpgsql;

-- Triggers to update the CGPA after enrollment update
CREATE OR REPLACE FUNCTION update_cgpa_trigger_function() RETURNS trigger AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        CALL update_cgpa(NEW.entry_no);
    ELSIF TG_OP = 'UPDATE' THEN
        CALL update_cgpa(NEW.entry_no);
    ELSIF TG_OP = 'DELETE' THEN
        CALL update_cgpa(OLD.entry_no);
    END IF;
    RETURN NULL;
END;

$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER update_cgpa_trigger AFTER INSERT OR UPDATE OR DELETE ON enrollments FOR EACH ROW EXECUTE PROCEDURE update_cgpa_trigger_function();