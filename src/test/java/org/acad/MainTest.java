package org.acad;

import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.IOException;

import static database.access.Exception.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {
    @Test
    void main() {

        int exitCode = new CommandLine(new Main()).execute("--help");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Main()).execute();
        assertEquals(SUCCESS, exitCode);
        Main.checkConfigFile();
    }
}