package org.acad;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import picocli.CommandLine;

import java.io.IOException;

import static database.access.Exception.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@TestMethodOrder(OrderAnnotation.class)
class CatalogTest {
    @Test
    @Order(1)
    void addCatalog() {

        int exitCode = new CommandLine(new Catalog()).execute("add", "-C", "CS101", "-n", "Data Structures", "-d", "des", "-c", "1", "-s", "1-1-1-1-1");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("add", "-C", "CS101", "-n", "Data Structures", "-d", "des", "-c", "2", "-s", "1-1-1-1-1");
        assertEquals(2, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("add", "-C", "CS101", "-n", "Data Structures", "-d", "des", "-c", "1", "-s", "1-1-1-1-1");
        assertEquals(ALREADY_EXISTS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("add", "-C", "CS102", "-n", "New Data Structures", "-d", "des", "-c", "3", "-s", "1-1-1-1-3");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("prereq", "CS102", "CS101", "E");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("prereq", "CS102", "CS101", "E");
        assertEquals(ALREADY_EXISTS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("prereq", "CS102", "CS105", "A-");
        assertEquals(NOT_EXISTS, exitCode);

    }

    @Test
    @Order(2)
    void listCatalog() {
        int exitCode = new CommandLine(new Catalog()).execute("-l");
        assertEquals(SUCCESS, exitCode);
    }

    @Test
    @Order(3)
    void updateCatalog() {
        int exitCode = new CommandLine(new Catalog()).execute("update", "-C", "CS101", "-n", "Structures", "-d", "des", "-c", "1", "-s", "1-1-1-1-1");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("update", "-C", "CS105", "-n", "Structures", "-d", "des", "-c", "2", "-s", "1-1-1-1-1");
        assertEquals(NOT_EXISTS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("update", "-C", "CS101", "-n", "Structures", "-d", "des", "-c", "2", "-s", "1-1-1-1-1");
        assertEquals(3, exitCode);
    }

    @Test
    @Order(4)
    void displayCatalog() {
        int exitCode = new CommandLine(new Catalog()).execute("--display", "CS101");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("--display", "CS102");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("--display", "CS105");
        assertEquals(NOT_EXISTS, exitCode);
    }

    @Test
    @Order(5)
    void removeCatalog() {
        int exitCode = new CommandLine(new Catalog()).execute("prereq", "-r", "CS102", "CS105");
        assertEquals(NOT_EXISTS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("prereq", "-r", "CS102", "CS101");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("remove", "CS101");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("remove", "CS105");
        assertEquals(NOT_EXISTS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("remove", "CS102");
        assertEquals(SUCCESS, exitCode);
    }
}