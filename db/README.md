# AcademicSystemDatabase
Database for the Academic Management Project

## Dependency

- Postgresql V14.5 or above

## Setup
- Make sure that your system don't already have database with name *academics*
- Execute the following Commands after starting postgres in terminal in the same directory
```
\i setup.sql
```

This will load all the tables and procedures required.
This also creates a user named *dean_ug* with academics_office permission with password 'iitrpr' which can be changes later on.

## Tables

### Student

| Field        | Type         | Null | Key | Default | Extra |
|--------------|--------------|------|-----|---------|-------|
| entry_no     | varchar(11)  | NO   | PRI | NULL    |       |
| name         | varchar(100) | NO   |     | NULL    |       |
| email        | varchar(100) | NO   | UNI | NULL    |       |
| phone        | varchar(20)  | NO   |     | NULL    |       |
| department   | varchar(2)   | NO   | MUL | NULL    |       |
| entry_year   | int(11)      | NO   |     | NULL    |       |
| address      | text         | NO   |     | NULL    |       |
| program      | varchar(100) | NO   |     | NULL    |       |
| cgpa         | float        | NO   |     | NULL    |       |
| credit_limit | float        | NO   |     | 24      |       |
| advisor      | varchar(100) | NO   | MUL | NULL    |       |

### Instructor
| Field      | Type         | Null | Key | Default | Extra |
|------------|--------------|------|-----|---------|-------|
| email      | varchar(100) | NO   | PRI | NULL    |       |
| name       | varchar(100) | NO   |     | NULL    |       |
| department | varchar(2)   | NO   | MUL | NULL    |       |
| phone      | varchar(20)  | NO   |     | NULL    |       |
| address    | text         | NO   |     | NULL    |       |

### Departments
| Field  | Type         | Null | Key | Default | Extra |
|--------|--------------|------|-----|---------|-------|
| dep_id | varchar(2)   | NO   | PRI | NULL    |       |
| name   | varchar(100) | NO   |     | NULL    |       |

### Catalog
| Field      | Type         | Null | Key | Default | Extra |
|------------|--------------|------|-----|---------|-------|
| code       | char(5)      | NO   | PRI | NULL    |       |
| name       | varchar(100) | NO   |     | NULL    |       |
| credits    | float        | NO   |     | NULL    |       |
| department | varchar(2)   | NO   | MUL | NULL    |       |
| L          | float        | NO   |     | NULL    |       |
| T          | float        | NO   |     | NULL    |       |
| P          | float        | NO   |     | NULL    |       |
| S          | float        | NO   |     | NULL    |       |
| C          | float        | NO   |     | NULL    |       |

### Offerings
| Field       | Type            | Null | Key | Default | Extra |
|-------------|-----------------|------|-----|---------|-------|
| id          | int(11)         | NO   | PRI | NULL    |       |
| code        | char(5)         | NO   | MUL | NULL    |       |
| section     | int(11)         | NO   |     | NULL    |       |
| semester    | semester        | NO   |     | NULL    |       |
| year        | int(11)         | NO   |     | NULL    |       |
| coordinator | varchar(100)    | NO   | MUL | NULL    |       |
| department  | varchar(2)      | NO   | MUL | NULL    |       |
| status      | offering_status | NO   |     | NULL    |       |
| min_cgpa    | float           | NO   |     | NULL    |       |

### Prerequisite\_Default
| Field      | Type    | Null | Key | Default | Extra |
|------------|---------|------|-----|---------|-------|
| code       | char(5) | NO   | MUL | NULL    |       |
| prereq     | char(5) | NO   | MUL | NULL    |       |
| min\_grade | grade   | NO   |     | NULL    |       |


### Prerequisite
| Field      | Type    | Null | Key | Default | Extra |
|------------|---------|------|-----|---------|-------|
| id         | int     | NO   | MUL | NULL    |       |
| prereq     | char(5) | NO   | MUL | NULL    |       |
| min\_grade | grade   | NO   |     | NULL    |       |

### Academic\_Calendar
| Field       | Type            | Null | Key | Default | Extra |
|-------------|-----------------|------|-----|---------|-------|
| year        | int             | NO   |     | NULL    |       |
| semester    | semester        | NO   |     | NULL    |       |
| event       | academic\_event | NO   |     | NULL    |       |
| start\_date | date            | NO   |     | NULL    |       |
| end\_date   | date            | NO   |     | NULL    |       |

### Course\_Category
| Field       | Type          | Null | Key | Default | Extra |
|-------------|---------------|------|-----|---------|-------|
| id          | int           | NO   | MUL | NULL    |       |
| type        | course\_types | NO   |     | NULL    |       |
| entry\_year | int           | NO   |     | NULL    |       |
| department  | varchar(2)    | NO   | MUL | NULL    |       |
| program     | varchar(100)  | NO   |     | NULL    |       |


### Teaching\_Team
| Field           | Type         | Null | Key | Default | Extra |
|-----------------|--------------|------|-----|---------|-------|
| id              | int          | NO   | MUL | NULL    |       |
| instructor      | varchar(100) | NO   | MUL | NULL    |       |
| is\_coordinator | tinyint(1)   | NO   |     | NULL    |       |

### Curriculum
| Field         | Type          | Null | Key | Default | Extra |
|---------------|---------------|------|-----|---------|-------|
| Program       | varchar(100)  | NO   | MUL | NULL    |       |
| Course\_types | course\_types | NO   | MUL | NULL    |       |
| min_credits   | float         | NO   |     | NULL    |       |

### login_logs
| Field      | Type         | Null | Key | Default | Extra |
|------------|--------------|------|-----|---------|-------|
| username   | text         | NO   |     | NULL    |       |
| time_stamp | timestamp    | NO   |     | NULL    |       |
| activity   | text         | NO   |     | NULL    |       |

## Privileges

### Student
| Name                   | Privilege                      | Type      |
|------------------------|--------------------------------|-----------|
| catalog                | SELECT                         | Table     |
| departments            | SELECT                         | Table     |
| students               | SELECT, UPDATE(phone, address) | Table     |
| prerequisite_default   | SELECT                         | Table     |
| offerings              | SELECT                         | Table     |
| prerequisite           | SELECT                         | Table     |
| course_category        | SELECT                         | Table     |
| enrollments            | SELECT, INSERT, UPDATE(status) | Table     |
| teaching_team          | SELECT                         | Table     |
| academic_calender      | SELECT                         | Table     |
| curriculum             | SELECT                         | Table     |
| calculate_cgpa         | EXECUTE                        | Function  |
| update_cgpa            | EXECUTE                        | Procedure |
| update_credit_limit    | EXECUTE                        | Procedure |
| login_logs             | SELECT, INSERT, UPDATE         | Table     |
| log_activity           | EXECUTE                        | Procedure |

### Instructor
| Name                   | Privilege                      | Type      |
|------------------------|--------------------------------|-----------|
| catalog                | SELECT                         | Table     |
| departments            | SELECT                         | Table     |
| students               | SELECT                         | Table     |
| instructor             | SELECT, UPDATE(address, phone) | Table     |
| prerequisite_default   | SELECT                         | Table     |
| offerings              | SELECT, UPDATE, INSERT         | Table     |
| prerequisite           | SELECT, UPDATE, INSERT         | Table     |
| course_category        | SELECT, UPDATE, INSERT         | Table     |
| enrollments            | SELECT, UPDATE                 | Table     |
| teaching_team          | SELECT, UPDATE, INSERT         | Table     |
| academic_calender      | SELECT                         | Table     |
| curriculum             | SELECT                         | Table     |
| calculate_cgpa         | EXECUTE                        | Function  |
| update_cgpa            | EXECUTE                        | Procedure |
| update_credit_limit    | EXECUTE                        | Procedure |
| login_logs             | SELECT, INSERT, UPDATE         | Table     |
| log_activity           | EXECUTE                        | Procedure |

### Academics Office
| Name                   | Privilege                      | Type      |
|------------------------|--------------------------------|-----------|
| catalog                | SELECT, UPDATE, INSERT, DELETE | Table     |
| departments            | SELECT, UPDATE, INSERT, DELETE | Table     |
| students               | SELECT, UPDATE, INSERT, DELETE | Table     |
| instructor             | SELECT, UPDATE, INSERT, DELETE | Table     |
| prerequisite_default   | SELECT, UPDATE, INSERT, DELETE | Table     |
| offerings              | SELECT, UPDATE, INSERT, DELETE | Table     |
| prerequisite           | SELECT, UPDATE, INSERT, DELETE | Table     |
| course_category        | SELECT, UPDATE, INSERT, DELETE | Table     |
| enrollments            | SELECT, UPDATE, INSERT, DELETE | Table     |
| teaching_team          | SELECT, UPDATE, INSERT, DELETE | Table     |
| academic_calender      | SELECT, UPDATE, INSERT, DELETE | Table     |
| curriculum             | SELECT, UPDATE, INSERT, DELETE | Table     |
| calculate_cgpa         | EXECUTE                        | Function  |
| update_cgpa            | EXECUTE                        | Procedure |
| update_credit_limit    | EXECUTE                        | Procedure |
| login_logs             | SELECT, INSERT, UPDATE, DELETE | Table     |
| log_activity           | EXECUTE                        | Procedure |

## Policy
| Policy Name    | On Table        | To Role          | Using Clause                                                                     | Remarks                                                            |
|----------------|-----------------|------------------|----------------------------------------------------------------------------------|--------------------------------------------------------------------|
| admin_all      | offerings       | academics_office | true                                                                             | Allow admin everthing                                              |
| student_sel    | offerings       | student          | true                                                                             | Allow student to select everything                                 |
| instructor_sel | offerings       | instructor       | true                                                                             | Allow instructor to select everything                              |
| instructor_upd | offerings       | instructor       | current_user = coordinator                                                       | Allow instructor to update only if he is the coordinator           |
| instructor_ins | offerings       | instructor       | current_user = coordinator                                                       | Allow instructor to insert only if he is the coordinator           |
| instructor_del | offerings       | instructor       | current_user = coordinator                                                       | Allow instructor to delete only if he is the coordinator           |
| admin_all      | course_category | academics_office | true                                                                             | Allow admin everthing                                              |
| student_sel    | course_category | student          | true                                                                             | Allow student to select everything                                 |
| instructor_sel | course_category | instructor       | true                                                                             | Allow instructor to select everything                              |
| instructor_upd | course_category | instructor       | current_user IN (SELECT coordinator FROM offerings WHERE id=course_category.id)  | Allow instructor to update only if he is the coordinator           |
| instructor_ins | course_category | instructor       | current_user IN (SELECT coordinator FROM offerings WHERE id=course_category.id)  | Allow instructor to insert only if he is the coordinator           |
| instructor_del | course_category | instructor       | current_user IN (SELECT coordinator FROM offerings WHERE id=course_category.id)  | Allow instructor to delete only if he is the coordinator           |
| admin_all      | enrollments     | academics_office | true                                                                             | Allow admin everthing                                              |
| student_sel    | enrollments     | student          | current_user = entry_no                                                          | Allow student to select only his own enrollments                   |
| instructor_sel | enrollments     | instructor       | true                                                                             | Allow instructor to select everything                              |
| student_ins    | enrollments     | student          | current_user = entry_no AND grade='NA'                                           | Allow student to insert only if he is the entry_no and grade is NA |
| student_upd    | enrollments     | student          | current_user = entry_no                                                          | Allow student to update only if he is the entry_no                 |
| instructor_upd | enrollments     | instructor       | current_user IN (SELECT instructor FROM teaching_team WHERE id = enrollments.id) | Allow instructor to update only if he is the instructor            |
| admin_all      | teaching_team   | academics_office | true                                                                             | Allow admin everthing                                              |
| student_sel    | teaching_team   | student          | true                                                                             | Allow student to select everything                                 |
| instructor_sel | teaching_team   | instructor       | true                                                                             | Allow instructor to select everything                              |
| instructor_upd | teaching_team   | instructor       | current_user = (SELECT coordinator FROM offerings WHERE id=teaching_team.id)     | Allow instructor to update only if he is the coordinator           |
| instructor_ins | teaching_team   | instructor       | current_user = (SELECT coordinator FROM offerings WHERE id=teaching_team.id)     | Allow instructor to insert only if he is the coordinator           |
| instructor_del | teaching_team   | instructor       | current_user = (SELECT coordinator FROM offerings WHERE id=teaching_team.id)     | Allow instructor to delete only if he is the coordinator           |
## Functions
| Function Name       | Function Type | Function Definition                                                 | Remarks                                             |
|---------------------|---------------|---------------------------------------------------------------------|-----------------------------------------------------|
| calculate_cgpa      | Function      | calculate_cgpa(entry_no CHAR(11)) RETURNS float                     | Function to calculate the CGPA of a student         |
| update_cgpa         | Procedure     | update_cgpa(entry_no CHAR(11))                                      | Procedure to update the CGPA to the student profile |
| update_credit_limit | Procedure     | update_credit_limit(entry_no CHAR(11), year INT, semester semester) | Procedure to update credit_limit of a student       |


## Triggers


| Trigger Name                    | Trigger Event          | Trigger Function                  | Trigger Table   | Remarks                                                                                                   |
|---------------------------------|------------------------|-----------------------------------|-----------------|-----------------------------------------------------------------------------------------------------------|
| check_course_event              | INSERT, UPDATE, DELETE | check_course_event()              | course          | Trigger to check if course is updated/deleted/inserted within the Course Add/Drop timeframe.              |
| check_course_prerequisite_event | INSERT, UPDATE, DELETE | check_course_prerequisite_event() | prerequisite    | Trigger to check if course prerequisite is updated/deleted/inserted within the Course Add/Drop timeframe. |
| check_course_category_event     | INSERT, UPDATE, DELETE | check_course_category_event()     | course_category | Trigger to check if course category is updated/deleted/inserted within the Course Add/Drop timeframe.     |
| check_course_enrollment_event   | INSERT                 | check_course_enrollment_event()   | enrollments     | Trigger to check if course enrollment is updated/deleted/inserted within the Course Add/Drop timeframe.   |
| check_enrollment_update_event   | UPDATE                 | check_enrollment_update_event()   | enrollments     | Trigger to check if the enrollment is updated within the timeframe                                        |
| check_prereq                    | INSERT, UPDATE         | check_prereq()                    | enrollments     | Trigger to check if the student has done all the prerequisites for the course                             |

