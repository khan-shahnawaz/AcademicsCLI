package org.acad;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import subcommands.departments.*;
import static database.access.Exception.*;

@Command(name = "department", mixinStandardHelpOptions = true, version = "department 0.1",
        description = "Contains the functionality for displaying and managing department.", subcommands = {Add.class, Remove.class})
public class Department implements Callable<Integer> {
    @Option(names = {"-l", "--list"}, description = "List all departments.")
    boolean list;

    @Override
    public Integer call() {
        try {
            if (list) {
                ArrayList<models.Department> departments = models.Department.retrieveAll();
                if (departments.size() == 0) {
                    System.out.println("No Such entries found.");
                    return SUCCESS;
                }
                System.out.println("Departments:");
                // Print the table header Department Code, Deparment name
                System.out.println("Department Code\t\tDepartment Name");
                for (models.Department department : departments) {
                    System.out.printf("%-20s %-20s%n", department.getId(), department.getName());
                }
            }
            return SUCCESS;
        } catch (SQLException e) {
            System.out.println("Failed to fetch department.");
            return handleSQLException(e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            return UNKNOWN;
        }
    }
}
