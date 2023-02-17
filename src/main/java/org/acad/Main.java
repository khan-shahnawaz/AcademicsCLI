package org.acad;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "acad", mixinStandardHelpOptions = true, subcommands = {SubcommandCatalog.class}, version = "acad 0.1",
        description = "A command line interface for the Academic database.")
public class Main implements Runnable {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        System.out.println("Use --help for more information.");
    }
}