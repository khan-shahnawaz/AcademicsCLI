package org.acad;

import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import subcommands.instructors.*;
import static database.access.Exception.*;
@CommandLine.Command(name = "instructor", mixinStandardHelpOptions = true, version = "instructor 0.1",
        description = "Contains the functionality for displaying and managing instructor.", subcommands = {Add.class, Remove.class})
public class Instructor implements Callable<Integer> {
    @Option(names = {"-l", "--list"}, description = "List all instructors.")
    boolean list;
    @Override
    public Integer call() {
        try {
            if (list) {
                ArrayList<models.Instructor> instructors = models.Instructor.retrieveAll();
                if (instructors.size() == 0) {
                    System.out.println("No Such entries found.");
                    return SUCCESS;
                }
                System.out.println("Instructors:");
                // Name, Department, Address, Phone, Email
                int columnWidth1 = Math.max("Name".length() + 2, instructors.stream().mapToInt(i -> i.getName().length()+2).max().orElse("Name".length() + 2));
                int columnWidth2 = Math.max("Department".length() + 2, instructors.stream().mapToInt(i -> i.getDepartmentCode().length()+2).max().orElse("Department".length() + 2));
                int columnWidth3 = Math.max("Address".length() + 2, instructors.stream().mapToInt(i -> i.getAddress().length()+2).max().orElse("Address".length() + 2));
                int columnWidth4 = Math.max("Phone".length() + 2, instructors.stream().mapToInt(i -> i.getPhone().length()+2).max().orElse("Phone".length() + 2));
                int columnWidth5 = Math.max("Email".length() + 2, instructors.stream().mapToInt(i -> i.getEmail().length()+2).max().orElse("Email".length() + 2));
                System.out.printf("%-" + columnWidth1 + "s", "Name");
                System.out.printf("%-" + columnWidth2 + "s", "Department");
                System.out.printf("%-" + columnWidth3 + "s", "Address");
                System.out.printf("%-" + columnWidth4 + "s", "Phone");
                System.out.printf("%-" + columnWidth5 + "s", "Email");
                System.out.println();
                for (models.Instructor instructor : instructors) {
                    System.out.printf("%-" + columnWidth1 + "s", instructor.getName());
                    System.out.printf("%-" + columnWidth2 + "s", instructor.getDepartmentCode());
                    System.out.printf("%-" + columnWidth3 + "s", instructor.getAddress());
                    System.out.printf("%-" + columnWidth4 + "s", instructor.getPhone());
                    System.out.printf("%-" + columnWidth5 + "s", instructor.getEmail());
                    System.out.println();
                }
                return SUCCESS;
            }
            System.out.println("Use -l or --list to list all instructors or --help to see all options.");
            return SUCCESS;
        } catch (SQLException e) {
            System.out.println("Failed to fetch instructor.");
            return handleSQLException(e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            return UNKNOWN;
        }
    }
}
