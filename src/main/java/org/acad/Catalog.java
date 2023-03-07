package org.acad;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import subcommands.catalog.Add;
import subcommands.catalog.Prerequisite;
import subcommands.catalog.Remove;
import subcommands.catalog.Update;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import static database.access.Exception.*;


/**
 * Class to deal with subcommand catalog.
 *
 * @author Shahnawaz Khan
 * @version 1.0
 * @since 2023-02-17
 */
@Command(name = "catalog", mixinStandardHelpOptions = true, version = "catalog 0.1",
        description = "Contains the functionality for displaying and changing the course catalog.", subcommands = {Add.class, Remove.class, Update.class, Prerequisite.class})
public class Catalog implements Callable<Integer> {
    @Option(names = {"-l", "--list"}, description = "List all courses in the catalog.")
    private boolean list;
    @Option(names = {"-d", "--display"}, description = "Display a course in the catalog.")
    private String courseCode;

    @Override
    public Integer call() {
        if (list) {
            try {
                DecimalFormat df = new DecimalFormat("#.##");
                ArrayList<models.Catalog> catalogList = models.Catalog.retrieveAll();
                String header1 = "Course Code";
                int columnWidth1 = Math.max(header1.length() + 2
                        , catalogList.stream().mapToInt(c -> c.getCode().length()).max().orElse(header1.length() + 2));
                String header2 = "Course Name";
                int columnWidth2 = Math.max(header2.length() + 2
                        , catalogList.stream().mapToInt(c -> c.getName().length()).max().orElse(header2.length() + 2));
                String header3 = "Course Credits";
                int columnWidth3 = Math.max(header3.length() + 2
                        , catalogList.stream().mapToInt(c -> df.format(c.getCredits()).length()).max().orElse(header3.length() + 2));
                String header4 = "Course Structure(L-T-P-S-C)";
                int columnWidth4 = Math.max(header4.length() + 2
                        , catalogList.stream().mapToInt(c -> df.format(c.getL()).length() + df.format(c.getT()).length() + df.format(c.getP()).length() + df.format(c.getS()).length() + df.format(c.getC()).length() + 4).max().orElse(header4.length() + 2));
                System.out.printf("%-" + columnWidth1 + "s | %-" + columnWidth2 + "s | %-" + columnWidth3 + "s | %-" + columnWidth4 + "s%n", header1, header2, header3, header4);
                for (models.Catalog catalog : catalogList) {
                    System.out.printf("%-" + columnWidth1 + "s | %-" + columnWidth2 + "s | %-" + columnWidth3 + "s | %-" + columnWidth4 + "s%n", catalog.getCode(), catalog.getName(), df.format(catalog.getCredits()), df.format(catalog.getL()) + "-" + df.format(catalog.getT()) + "-" + df.format(catalog.getP()) + "-" + df.format(catalog.getS()) + "-" + df.format(catalog.getC()));
                }
                return SUCCESS;
            } catch (SQLException e) {
                return handleSQLException(e.getSQLState(), e.getMessage());
            } catch (Exception e) {
                System.err.println("An error occurred while retrieving the catalog.");
                return UNKNOWN;
            }
        }
        if (courseCode != null) {
            try {
                models.Catalog catalog = models.Catalog.retrieve(courseCode);
                if (catalog == null) {
                    System.err.println("Course does not exist in the catalog.");
                    return NOT_EXISTS;
                }
                DecimalFormat df = new DecimalFormat("#.##");
                System.out.println("Course Code: " + catalog.getCode());
                System.out.println("Course Name: " + catalog.getName());
                System.out.println("Course Description: " + catalog.getDescription());
                System.out.println("Course Credits: " + df.format(catalog.getCredits()));
                System.out.println("Course Structure(L-T-P-S-C): " + df.format(catalog.getL()) + "-" + df.format(catalog.getT()) + "-" + df.format(catalog.getP()) + "-" + df.format(catalog.getS()) + "-" + df.format(catalog.getC()));
                boolean hasPrerequisites = false;
                ArrayList<models.DefaultPrerequisite> prerequisites = models.DefaultPrerequisite.retrieveAll();
                for (models.DefaultPrerequisite prerequisite : prerequisites) {
                    if (prerequisite.getCatalogCode().equals(catalog.getCode())) {
                        if (!hasPrerequisites) {
                            System.out.println("List of prerequisites: ");
                            hasPrerequisites = true;
                        }
                        System.out.println("\t" + prerequisite.getPrerequisiteCode());
                        System.out.println("Minimum grade required: " + prerequisite.getMinGrade());
                    }
                }
                return SUCCESS;
            } catch (SQLException e) {
                return handleSQLException(e.getSQLState(), e.getMessage());
            } catch (Exception e) {
                System.err.println("An error occurred while retrieving the catalog.");
                return UNKNOWN;
            }
        }

        System.out.println("Use --help for more information.");
        return SUCCESS;
    }
}
