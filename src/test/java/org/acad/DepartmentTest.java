package org.acad;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import picocli.CommandLine;

import static database.access.Exception.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DepartmentTest {
    @Order(1)
    @Test
    void addDepartment() {
        int exitCode = new CommandLine(new Department()).execute("add", "-n", "Computer Science", "-c", "CS");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Department()).execute("add", "-n", "Computer Science", "-c", "CS");
        assertEquals(ALREADY_EXISTS, exitCode);
        exitCode = new CommandLine(new Department()).execute("add", "-n", "Computer Science", "-c", "CSS");
        assertEquals(UNKNOWN, exitCode);
    }

    @Order(2)
    @Test
    void listDepartment() {
        int exitCode = new CommandLine(new Department()).execute("-l");
        assertEquals(SUCCESS, exitCode);
    }

    @Order(3)
    @Test
    void removeDepartment() {
        int exitCode = new CommandLine(new Department()).execute("remove", "-c", "CS");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Department()).execute("remove", "-c", "CS");
        assertEquals(NOT_EXISTS, exitCode);
    }
}