package org.acad;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import picocli.CommandLine;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


@TestMethodOrder(OrderAnnotation.class)
class CatalogTest {
    @Test
    @Order(1)
    void addCatalog() throws IOException {

        int exitCode = new CommandLine(new Catalog()).execute("add", "-C", "CS101", "-n", "Data Structures", "-d", "des", "-c", "1", "-s", "1-1-1-1-1");
        assertEquals(Catalog.SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("add", "-C", "CS101", "-n", "Data Structures", "-d", "des", "-c", "2", "-s", "1-1-1-1-1");
        assertEquals(2, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("add", "-C", "CS101", "-n", "Data Structures", "-d", "des", "-c", "1", "-s", "1-1-1-1-1");
        assertEquals(Catalog.ALREADY_EXISTS, exitCode);
    }

    @Test
    @Order(2)
    void listCatalog() {
        int exitCode = new CommandLine(new Catalog()).execute("-l");
        assertEquals(Catalog.SUCCESS, exitCode);
    }

    @Test
    @Order(3)
    void updateCatalog() throws IOException {
        int exitCode = new CommandLine(new Catalog()).execute("update", "-C", "CS101", "-n", "Structures", "-d", "des", "-c", "1", "-s", "1-1-1-1-1");
        assertEquals(Catalog.SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("update", "-C", "CS102", "-n", "Structures", "-d", "des", "-c", "2", "-s", "1-1-1-1-1");
        assertEquals(Catalog.NOT_EXISTS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("update", "-C", "CS101", "-n", "Structures", "-d", "des", "-c", "2", "-s", "1-1-1-1-1");
        assertEquals(3, exitCode);
    }

    @Test
    @Order(4)
    void displayCatalog() {
        int exitCode = new CommandLine(new Catalog()).execute("--display", "CS101");
        assertEquals(Catalog.SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("--display", "CS102");
        assertEquals(Catalog.NOT_EXISTS, exitCode);
    }

    @Test
    @Order(5)
    void removeCatalog() throws IOException {
        int exitCode = new CommandLine(new Catalog()).execute("remove", "CS101");
        assertEquals(Catalog.SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("remove", "CS102");
        assertEquals(Catalog.NOT_EXISTS, exitCode);
    }

}