-- Trigger to check if the student has done all the prerequisites for the course


CREATE OR REPLACE FUNCTION check_prereq()
RETURNS trigger AS $$
DECLARE
    is_eligible boolean DEFAULT true;
    course_id CHAR(11);
    prereq record;
    grade_var grade DEFAULT 'NA';
    cgpa_req float;
    code_var CHAR(5);
    entry_no_var VARCHAR(11);
    credit_limit_var float;
    total_credits float;
    yr INT;
    credits_var float;
    sem semester;
BEGIN
    -- Check if the student has already enrolled in the course
    
    grade_var ='NA';
    SELECT code INTO code_var FROM offerings WHERE id = NEW.id;
    SELECT grade INTO grade_var FROM enrollments, offerings WHERE enrollments.id = offerings.id AND enrollments.entry_no = NEW.entry_no AND offerings.code = code_var;
    entry_no_var=NEW.entry_no;
    IF grade_var = 'A' OR grade_var = 'A-' OR grade_var = 'B' OR grade_var = 'B-' OR grade_var = 'C' OR grade_var = 'C-' OR grade_var = 'D' OR grade_var='NP' OR grade_var='S' THEN
        raise SQLSTATE '99905';
    END IF;
    FOR prereq IN SELECT * FROM prerequisite WHERE id = NEW.id
    LOOP
        grade_var = 'NA';
        SELECT code INTO course_id FROM offerings WHERE id = prereq.id;
        SELECT grade INTO grade_var FROM enrollments, offerings WHERE enrollments.id = offerings.id AND enrollments.entry_no = NEW.entry_no;
        IF prereq.min_grade<grade_var OR grade_var IS NULL THEN
            is_eligible = false;
        END IF;
        grade_var = 'NA';
    END LOOP;
    SELECT min_cgpa FROM offerings WHERE id = NEW.id INTO cgpa_req;
    IF calculate_cgpa(entry_no_var)<cgpa_req then
        is_eligible = false;
    END IF;
    SELECT year, semester INTO yr, sem FROM offerings WHERE id = NEW.id;
    SELECT credit_limit INTO credit_limit_var FROM students WHERE entry_no = NEW.entry_no;
    total_credits = 0;
    SELECT sum(C) FROM enrollments, offerings, catalog WHERE enrollments.id = offerings.id AND offerings.code = catalog.code AND entry_no = NEW.entry_no AND year = yr AND semester = sem AND (enrollments.status='Enrolled' OR enrollments.status= 'Audit') INTO total_credits;
    SELECT credits INTO credits_var FROM catalog WHERE code = code_var;
    IF total_credits + credits_var > credit_limit_var then
        RAISE SQLSTATE '99906';
    END IF;
    IF is_eligible=true then
        RETURN NEW;
    else
        RAISE SQLSTATE '99907';
    END IF;
END;
$$ LANGUAGE plpgsql;
CREATE OR REPLACE TRIGGER check_prereq_trigger BEFORE INSERT OR UPDATE ON enrollments FOR EACH ROW EXECUTE PROCEDURE check_prereq();
