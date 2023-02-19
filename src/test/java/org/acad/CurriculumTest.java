package org.acad;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import picocli.CommandLine;

import static database.access.Exception.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CurriculumTest {
    @Order(1)
    @Test
    void addCurriculum() {
        int exitCode = new CommandLine(new Curriculum()).execute("add", "-p", "B.Tech", "-t", "PE", "-c", "20");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Curriculum()).execute("add", "-p", "B.Tech", "-t", "P", "-c", "20");
        assertEquals(INVALID_VALUES, exitCode);
        exitCode = new CommandLine(new Curriculum()).execute("add", "-p", "B.Tech", "-t", "PC", "-c", "20");
        assertEquals(SUCCESS, exitCode);
    }

    @Order(2)
    @Test
    void listCurriculum() {
        int exitCode = new CommandLine(new Curriculum()).execute("-l");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Curriculum()).execute("-l", "-p", "B.Tech");
        assertEquals(SUCCESS, exitCode);
    }

    @Order(3)
    @Test
    void removeCurriculum() {
        int exitCode = new CommandLine(new Curriculum()).execute("remove", "-p", "B.Tech", "-t", "PE");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Curriculum()).execute("remove", "-p", "B.Tech", "-t", "PE");
        assertEquals(NOT_EXISTS, exitCode);
        exitCode = new CommandLine(new Curriculum()).execute("remove", "-p", "B.Tech", "-t", "PC");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Curriculum()).execute("-l");
        assertEquals(SUCCESS, exitCode);
    }

}