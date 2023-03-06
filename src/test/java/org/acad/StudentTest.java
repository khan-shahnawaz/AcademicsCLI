package org.acad;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import picocli.CommandLine;

import static database.access.Exception.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudentTest {
    @Order(1)
    @Test
    void addStudent() {
        int exitCode = new CommandLine(new Student()).execute("--list");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Department()).execute("add", "-n", "Computer Science", "-c", "CS");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Instructor()).execute("add", "-F", "src/test/resources/instructors.csv");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Student()).execute("add", "-n", "John Doe", "-d", "CS", "-a", "India", "-p", "1234567890", "-E", "email",
                "-c", "18", "-e", "2020CSB5000", "-P", "B.Tech", "-A", "EQANYINXNY@gmail.com", "-y", "2020", "-w", "1234");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Student()).execute("add", "-n", "John Doe", "-d", "CS", "-a", "India", "-p", "1234567890", "-E", "email",
                "-c", "18", "-e", "2020CSB5000", "-P", "B.Tech", "-A", "abcd@a.com", "-y", "2020", "-w", "1234");
        assertEquals(ALREADY_EXISTS, exitCode);
        exitCode = new CommandLine(new Student()).execute("add", "-F", "src/test/resources/students.csv");
        assertEquals(SUCCESS, exitCode);
    }

    @Order(2)
    @Test
    void listStudent() {
        int exitCode = new CommandLine(new Student()).execute("--list");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Student()).execute("--list", "-e", "2020CSB5000");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Student()).execute("--list", "-d", "CS");
    }

    @Order(3)
    @Test
    void removeStudent() {
        int exitCode = new CommandLine(new Student()).execute("remove", "-e", "2020CSB5000");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Student()).execute("remove", "-e", "2020CSB5000");
        assertEquals(NOT_EXISTS, exitCode);
        exitCode = new CommandLine(new Student()).execute("remove", "-F", "src/test/resources/students.csv");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Instructor()).execute("remove", "-F", "src/test/resources/instructors.csv");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Department()).execute("remove", "-c", "CS");
        assertEquals(SUCCESS, exitCode);
    }
}