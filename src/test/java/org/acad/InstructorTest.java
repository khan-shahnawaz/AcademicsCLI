package org.acad;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import picocli.CommandLine;

import static database.access.Exception.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.opencsv.CSVReaderHeaderAware;

import java.io.FileReader;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InstructorTest {
    @Order(1)
    @Test
    void addInstructor() throws Exception {
        int exitCode = new CommandLine(new Department()).execute("add", "-n", "Computer Science", "-c", "CS");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Instructor()).execute("--list");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Instructor()).execute("add", "-n", "John Doe", "-d", "CS", "-a", "India", "-p", "1234567890", "-e", "email");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Instructor()).execute("add", "-n", "John Doe", "-d", "CS", "-a", "India", "-p", "1234567890", "-e", "email");
        assertEquals(ALREADY_EXISTS, exitCode);
        exitCode = new CommandLine(new Instructor()).execute("add", "-F", "src/test/resources/instructors.csv");
        assertEquals(SUCCESS, exitCode);
    }

    @Order(2)
    @Test
    void listInstructor() throws Exception {
        int exitCode = new CommandLine(new Instructor()).execute("--list");
        assertEquals(SUCCESS, exitCode);
    }

    @Order(3)
    @Test
void removeInstructor() throws Exception {
        int exitCode = new CommandLine(new Instructor()).execute("remove", "-e", "email");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Instructor()).execute("remove", "-e", "email");
        assertEquals(NOT_EXISTS, exitCode);
        exitCode = new CommandLine(new Instructor()).execute("remove", "-F", "src/test/resources/instructors.csv");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Department()).execute("remove", "-c", "CS");
        assertEquals(SUCCESS, exitCode);
    }
}