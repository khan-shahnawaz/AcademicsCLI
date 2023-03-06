package org.acad;

import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static database.access.Exception.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfigurationTest {
    @Test
    void changeConfiguration() {
        InputStream in = new ByteArrayInputStream("4321\n".getBytes());
        System.setIn(in);
        int exitCode = new CommandLine(new Configuration()).execute("-d", "acad", "-u", "post", "-w");
        assertEquals(1, exitCode);
        in = new ByteArrayInputStream("1234\n".getBytes());
        System.setIn(in);
        exitCode = new CommandLine(new Configuration()).execute("-d", "academics", "-h", "localhost", "-w", "-p", "5432", "-u", "postgres");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Configuration()).execute("-o");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Configuration()).execute("-w", "1234");
        assertEquals(SUCCESS, exitCode);
    }
}