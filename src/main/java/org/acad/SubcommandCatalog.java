package org.acad;

import models.Catalog;
import picocli.CommandLine.*;
import picocli.CommandLine.Model.CommandSpec;

/**
 * Class to deal with subcommand catalog.
 *
 * @author Shahnawaz Khan
 * @version 1.0
 * @since 2023-02-17
 */
@Command(name = "catalog", mixinStandardHelpOptions = true, version = "catalog 0.1",
        description = "Contains the functionality for displaying and changing the course catalog.")
public class SubcommandCatalog implements Runnable {
    @Spec
    CommandSpec spec;
    @Option(names = {"-l", "--list"}, description = "List all courses in the catalog.")
    private boolean list;

    @Command(name = "add", mixinStandardHelpOptions = true, version = "add 0.1",
            description = "Add a course to the catalog.")
    public void add(
            @Parameters(index = "0", description = "Course Name", prompt = "Enter Course Name: ", interactive = true)
            String courseName,
            @Parameters(index = "1", description = "Course Description", prompt = "Course Description: ", interactive = true)
            String courseDescription,
            @Parameters(index = "2", description = "Course Credits", prompt = "Enter number of credits for the course: ", interactive = true)
            float courseCredits,
            @Parameters(index = "3", description = "Course Structure", prompt = "Enter the L-T-P-S-C course Structure: ", interactive = true)
            String courseStructure,
            @Parameters(index = "4", description = "Course Code")
            String courseCode
    ) throws Exception {
        Catalog catalog = new Catalog();
        catalog.setName(courseName);
        catalog.setDescription(courseDescription);
        catalog.setCredits(courseCredits);
        String[] courseStructureArray = courseStructure.split("-");
        float L = Float.parseFloat(courseStructureArray[0]);
        float T = Float.parseFloat(courseStructureArray[1]);
        float P = Float.parseFloat(courseStructureArray[2]);
        float S = Float.parseFloat(courseStructureArray[3]);
        float C = Float.parseFloat(courseStructureArray[4]);
        if (C != courseCredits) {
            throw new ParameterException(spec.commandLine(), "Course credits and course structure do not match.");
        }
        catalog.setL(L);
        catalog.setT(T);
        catalog.setP(P);
        catalog.setS(S);
        catalog.setC(C);
        catalog.setCode(courseCode);
        String exitCode = catalog.save().toLowerCase();
        if (exitCode.equals("23505")) {
            System.err.println("Course already exists in the catalog");
            System.exit(1);
        }
        if (exitCode.equals("42501")) {
            System.err.println("You do not have the required permissions to add a course to the catalog.");
            System.exit(1);
        }
        if (!exitCode.equals("00000")) {
            System.err.println("An error occurred while adding the course to the catalog.");
            System.exit(1);
        }
        System.out.print("Course added successfully.");
    }

    @Command(name = "remove", mixinStandardHelpOptions = true, version = "remove 0.1",
            description = "Remove a course from the catalog.")
    public void remove(
            @Parameters(index = "0", description = "Course Code", paramLabel = "courseCode", descriptionKey = "courseCode")
            String courseCode
    ) throws Exception {
        Catalog catalog = Catalog.retrieve(courseCode);
        if (catalog == null) {
            throw new Exception("Course does not exist.");
        }
        catalog.delete();
        System.out.println("Course removed successfully.");
    }

    @Override
    public void run() {
        System.out.println("Use --help for more information.");
    }
}
