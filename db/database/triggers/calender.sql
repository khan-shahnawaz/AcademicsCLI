-- Triggers to enforce that operations are performed as per the academic calender.

-- Trigger to check if course is added in offering before the course Add/Drop event.
CREATE OR REPLACE FUNCTION check_course_add_drop_event()
RETURNS TRIGGER AS $$
DECLARE
    start_d DATE;
    end_d DATE;
BEGIN
    SELECT start_date, end_date INTO start_d, end_d FROM academic_calender WHERE event = 'Course Add/Drop' AND year = NEW.year AND semester = NEW.semester;
    -- Check if the result is null
    IF (start_d IS NULL OR end_d IS NULL) THEN
        RAISE SQLSTATE '99901';
    END IF;
    -- Check if the current date is within the course add/drop event
    IF (CURRENT_DATE < start_d OR CURRENT_DATE > end_d) THEN
        RAISE SQLSTATE '99902';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE OR REPLACE TRIGGER check_course_add_drop_event BEFORE INSERT OR UPDATE ON offerings FOR EACH ROW EXECUTE PROCEDURE check_course_add_drop_event();

-- Trigger to check if teaching_team is updated/deleted/inserted with the academic session.
CREATE OR REPLACE FUNCTION check_teaching_team_event()
RETURNS TRIGGER AS $$
DECLARE
    start_d DATE;
    end_d DATE;
BEGIN
    SELECT start_date, end_date INTO start_d, end_d FROM academic_calender, offerings WHERE event = 'Academic Session' AND NEW.id=offerings.id AND academic_calender.year = offerings.year AND academic_calender.semester = offerings.semester;
    -- Check if the result is null
    IF (start_d IS NULL OR end_d IS NULL) THEN
        RAISE SQLSTATE '99901';
    END IF;
    -- Check if the current date is within the academic session
    IF (CURRENT_DATE < start_d OR CURRENT_DATE > end_d) THEN
        RAISE SQLSTATE '99902';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE OR REPLACE TRIGGER check_teaching_team_event BEFORE INSERT OR UPDATE ON teaching_team FOR EACH ROW EXECUTE PROCEDURE check_teaching_team_event();

-- Trigger to check if course prerequisite is updated/deleted/inserted within the Course Add/Drop timeframe.
CREATE OR REPLACE FUNCTION check_course_prerequisite_event()
RETURNS TRIGGER AS $$
DECLARE
    start_d DATE;
    end_d DATE;
    yr INTEGER;
    sem semester;
BEGIN
    SELECT year, semester INTO yr, sem FROM offerings WHERE id = NEW.id;
    SELECT start_date, end_date INTO start_d, end_d FROM academic_calender WHERE event = 'Course Add/Drop' AND year = yr AND semester = sem;
    -- Check if the result is null
    IF (start_d IS NULL OR end_d IS NULL) THEN
        RAISE SQLSTATE '99901';
    END IF;
    -- Check if the current date is within the course add/drop event
    IF (CURRENT_DATE < start_d OR CURRENT_DATE > end_d) THEN
        RAISE SQLSTATE '99902';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE OR REPLACE TRIGGER check_course_prerequisite_event BEFORE INSERT OR UPDATE ON prerequisite FOR EACH ROW EXECUTE PROCEDURE check_course_prerequisite_event();
CREATE OR REPLACE TRIGGER check_course_category_event BEFORE INSERT OR UPDATE ON course_category FOR EACH ROW EXECUTE PROCEDURE check_course_prerequisite_event();

-- Trigger to check if course enrollment is updated/deleted/inserted within the Course Add/Drop timeframe.
CREATE OR REPLACE FUNCTION check_course_enrollment_event()
RETURNS TRIGGER AS $$
DECLARE
    start_d DATE;
    end_d DATE;
    yr INTEGER;
    sem semester;
    st course_status;
BEGIN
    SELECT year, semester INTO yr, sem FROM offerings WHERE id = NEW.id;
    SELECT start_date, end_date INTO start_d, end_d FROM academic_calender WHERE event = 'Course Add/Drop' AND year = yr AND semester = sem;
    -- Check if the result is null
    IF (start_d IS NULL OR end_d IS NULL) THEN
        RAISE SQLSTATE '99901';
    END IF;
    -- Check if the current date is within the course add/drop event
    IF (CURRENT_DATE < start_d OR CURRENT_DATE > end_d) THEN
        RAISE SQLSTATE '99902';
    END IF;
    SELECT status INTO st FROM offerings WHERE id = NEW.id;
    IF st<>'Enrolling' THEN
        RAISE SQLSTATE '99903';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE OR REPLACE TRIGGER check_course_enrollment_event BEFORE INSERT ON enrollments FOR EACH ROW EXECUTE PROCEDURE check_course_enrollment_event();

-- Trigger to check if the enrollment is updated within the timeframe
CREATE OR REPLACE FUNCTION check_enrollment_update_event()
RETURNS TRIGGER AS $$
DECLARE
    start_d DATE;
    end_d DATE;
    yr INTEGER;
    sem semester;
BEGIN
    IF NEW.id<>OLD.id THEN
        RAISE SQLSTATE '99900';
    END IF;
    SELECT year, semester INTO yr, sem FROM offerings WHERE id = NEW.id;
    IF OLD.status<>NEW.status THEN
        IF NEW.status='Instructor Rejected' OR NEW.status='Dropped by Student' OR NEW.status='Enrolled' THEN
            SELECT start_date, end_date INTO start_d, end_d FROM academic_calender WHERE event = 'Course Add/Drop' AND year = yr AND semester = sem;
            -- Check if the result is null
            IF (start_d IS NULL OR end_d IS NULL) THEN
                RAISE SQLSTATE '99901';
            END IF;
            -- Check if the current date is within the course add/drop event
            IF (CURRENT_DATE < start_d OR CURRENT_DATE > end_d) THEN
                RAISE SQLSTATE '99902';
            END IF;
        END IF;

        IF NEW.status='Withdrawn' OR NEW.status='Audit' THEN
            SELECT start_date, end_date INTO start_d, end_d FROM academic_calender WHERE event = 'Course Withdrawal/Audit' AND year = yr AND semester = sem;
            -- Check if the result is null
            IF (start_d IS NULL OR end_d IS NULL) THEN
                RAISE SQLSTATE '99901';
            END IF;
            -- Check if the current date is within the course add/drop event
            IF (CURRENT_DATE < start_d OR CURRENT_DATE > end_d) THEN
                RAISE SQLSTATE '99902';
            END IF;
        END IF;
    END IF;

    IF OLD.grade<>NEW.grade THEN
        SELECT start_date, end_date INTO start_d, end_d FROM academic_calender WHERE event = 'Grade Submission' AND year = yr AND semester = sem;
        -- Check if the result is null
        IF (start_d IS NULL OR end_d IS NULL) THEN
            RAISE SQLSTATE '99901';
        END IF;
        -- Check if the current date is within the course add/drop event
        IF (CURRENT_DATE < start_d OR CURRENT_DATE > end_d) THEN
            RAISE SQLSTATE '99902';
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE OR REPLACE TRIGGER check_enrollment_update_event BEFORE UPDATE ON enrollments FOR EACH ROW EXECUTE PROCEDURE check_enrollment_update_event();