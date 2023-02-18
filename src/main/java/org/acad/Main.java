package org.acad;

import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name = "acad", mixinStandardHelpOptions = true, subcommands = {Catalog.class, Configuration.class}, version = "acad 0.1",
        description = "A command line interface for the Academic database.")
public class Main implements Callable<Integer> {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        if (exitCode == 0) {
            System.exit(0);
        }
        System.exit(1);
    }

    @Override
    public Integer call() {
        System.out.println("Use --help for more information.");
        return 0;
    }

}