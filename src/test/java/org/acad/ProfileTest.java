package org.acad;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import static database.access.Exception.SUCCESS;
import static database.access.Exception.UNAUTHORISED;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProfileTest {
    @BeforeAll
    static void setUp() {
        int exitCode = new CommandLine(new Department()).execute("add", "-n", "Computer Science", "-c", "CS");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Instructor()).execute("add", "-n", "John Doe", "-d", "CS", "-a", "India", "-p", "1234567890", "-e", "email", "-w", "1234");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Student()).execute("add", "-n", "John Doe", "-d", "CS", "-a", "India", "-p", "1234567890", "-E", "email",
                "-c", "18", "-e", "2020CSB5000", "-P", "B.Tech", "-A", "email", "-y", "2020", "-w", "1234");
        assertEquals(SUCCESS, exitCode);
    }

    @AfterAll
    static void tearDown() {
        int exitCode = new CommandLine(new Student()).execute("remove", "-e", "2020CSB5000");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Instructor()).execute("remove", "-e", "email");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Department()).execute("remove", "-c", "CS");
        assertEquals(SUCCESS, exitCode);
    }

    @Test
    void ProfileTest() {
        int exitCode = new CommandLine(new Profile()).execute("-p", "23124");
        assertEquals(UNAUTHORISED, exitCode);
        exitCode = new CommandLine(new Configuration()).execute("-u", "email", "-w", "1234");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Profile()).execute("-p", "23124", "-a", "New Address");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Configuration()).execute("-u", "2020CSB5000", "-w", "1234");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Profile()).execute("-p", "23124", "-a", "New Address");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Configuration()).execute("-u", "postgres", "-w", "1234");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Instructor()).execute("-l");
        assertEquals(SUCCESS, exitCode);
    }

}