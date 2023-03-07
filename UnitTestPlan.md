# Introduction
The testing for the project AcademicsCLI is done using JUnit5. This document contains information about the unit tests like approach, dependencies, requirements etc.

## Approach
Since, this application interacts with the database, the tests are written to test the functionality of the application as well as the part of the application where the query is sent to the database. For each class in models package, the entry is first inserted into the database by .save() command. After all the entries are inserted, some functionality is test and at the end, the entries are deleted from the database using .delete() command. This approach is followed for all the classes in models package.
<br>
For the classes which uses business logic, the commands are run by picoCLI execute which returns some exit code. This allows us to assert some conditions based on the exit code. For example, if the exit code is 0, then the command was successful and if the exit code is 1, then the command was unsuccessful. This approach is followed for all the classes in subcommands and org.acad package.

## Requirements
The database has to exists and the tables have to be created before running the tests. The database has to be empty and postgresql server should be running before the tests are executed.

## Results
The above approach gives a code coverage of 93% for the project. The results can be seen in the report generated in */docs/reports* folder.
Some of the lines are not covered because of the following reasons:
- The code is not reachable because of the way the code is written. Some completely unexpected exceptions are handled which are not possible to occur. It was added in the code to make sure that the application does not crash if any unexpected exception occurs.
- Some exceptions like Filenotfound exception are not handled because the file is always present in the resources folder. That exception is very well handled for the user and the user is notified if the file is not present. But testing these exceptions will require us to delete the file and then re-add it which is not a good practice.
- Some sqlExceptions are handled but are not possible under any circumstances