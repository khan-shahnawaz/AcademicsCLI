# AcademicsCLI
Command Line Interface to interact with the database on Academics Management System

## Dependencies
- *Java 18.1.0.2*
- *Gradle 7.6.1*

## How to run
- Set up the database server by following the instructions from */db/README.md*
- Modify the *db.properties* file in */src/main/resources* to match your database server
- Run the following command in the root directory of the project
```
gradlew build
```
Note: Before running this command, make sure that the database server is running, has been initialised with tables and all tables are empty.
<br>
- This will unit run tests and generate report in */docs/reports*
- Generate the jar file using the following commands.
```agsl
gradlew shadowJar
```
This will generate the jar file in */build/libs*
- Run the jar file using the following command
```
java -jar build/libs/AcademicsCLI-1.0-SNAPSHOT-all.jar -h
```
This will display the help menu for the CLI. <br> <br>
**Note: If at any point, you encounter issue with database connection, just delete the .academic folder present in user home directory and re-run the program**
## Distributable
- The jar file is distributable and can be run on any machine with Java
- The jar file is self contained and does not require any external dependencies
- If you need to install the software and add it to path, you need to install distribution in */dist* folder for your OS if available

## How to use
- The CLI has a help menu for each subcommands which can be accessed using --help option.
- Setup the hostname and port of the database server by the following command
```
acad config -h <hostname> -p <port>
```

- To login to the system, use the following command(Considering that jar file is added to path from */dist* folder)
```
acad config -u <username> -w <password>
```
- You can also, just type
```agsl
acad config -u -w
```
and the CLI will prompt you for the username and password
- Other functionalities can be accessed using the --help option at each level
- Help option at the root level will display the help menu for the CLI like this
```agsl
Usage: acad [-hV] [COMMAND]
A command line interface for the Academic database.
  -h, --help      Show this help message and exit.
  -V, --version   Print version information and exit.
Commands:
  catalog     Contains the functionality for displaying and changing the course
                catalog.
  config      Configure the database connection and login credentials.
  calender    Contains the functionality for displaying and changing the
                academic Calender.
  curriculum  Contains the functionality for displaying and change the academic
                Curriculum.
  department  Contains the functionality for displaying and managing department.
  instructor  Contains the functionality for displaying and managing instructor.
  student     Contains the functionality for displaying and managing student.
  offerings   Contains the functionality for displaying and managing course
                offerings.
  enrol       Enrol/Drop a course enrollment for an offering
  report      Generates transcripts and graduation reports for students
  profile     Update profile information.
```

## Dummy Data
- The instructions to add dummy data is present in */data*.
- It also contains some transcripts and graduation reports for students.