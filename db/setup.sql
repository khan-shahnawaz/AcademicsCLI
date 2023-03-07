CREATE DATABASE academics;
\c academics

--Loading all the tables,functions and triggers

\i 'database/schema.sql'
\i 'database/triggers/cgpa.sql'
\i 'database/triggers/calender.sql'
\i 'database/triggers/prerequisites.sql'
\i 'database/privileges.sql'