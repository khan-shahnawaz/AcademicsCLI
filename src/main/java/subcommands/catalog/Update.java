package subcommands.catalog;

import picocli.CommandLine;

import java.sql.SQLException;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "update", mixinStandardHelpOptions = true, version = "update 0.1",
        description = "Update a course in the catalog.")
public class Update implements Callable<Integer> {
    public static int SUCCESS = 0;
    public static int ALREADY_EXISTS = 1;
    public static int UNAUTHORISED = 2;
    public static int UNKNOWN = 3;
    public static int NOT_EXISTS = 4;
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;
    @CommandLine.Option(names = {"-C", "--code"}, description = "Code of the course.", interactive = true, required = true, arity = "0..1", echo = true, prompt = "Course Code: ")
    private String courseCode;
    @CommandLine.Option(names = {"-n", "--name"}, description = "Name of the course.", interactive = true, arity = "0..1", echo = true, prompt = "Course Name: ", defaultValue = "")
    private String courseName;
    @CommandLine.Option(names = {"-d", "--description"}, description = "Description of the course.", interactive = true, arity = "0..1", echo = true, prompt = "Course Description: ", defaultValue = "")
    private String courseDescription;
    @CommandLine.Option(names = {"-c", "--credits"}, description = "Credits of the course.", interactive = true, arity = "0..1", echo = true, prompt = "Course Credits: ", defaultValue = "")
    private String courseCreditsStr;
    @CommandLine.Option(names = {"-s", "--structure"}, description = "Structure of the course.", interactive = true, arity = "0..1", echo = true, prompt = "Course Structure: ", defaultValue = "")
    private String courseStructure;

    public Integer call() {
        try {
            models.Catalog catalog = models.Catalog.retrieve(courseCode);
            if (catalog == null) {
                System.err.println("Course with that code does not exist in the catalog.");
                return NOT_EXISTS;
            }
            if (!courseName.equals("")) {
                catalog.setName(courseName);
            }
            if (!courseDescription.equals("")) {
                catalog.setDescription(courseDescription);
            }
            if (!courseCreditsStr.equals("")) {
                catalog.setCredits(Float.parseFloat(courseCreditsStr));
            }
            if (!courseStructure.equals("")) {
                String[] courseStructureArray = courseStructure.split("-");
                float L = Float.parseFloat(courseStructureArray[0]);
                float T = Float.parseFloat(courseStructureArray[1]);
                float P = Float.parseFloat(courseStructureArray[2]);
                float S = Float.parseFloat(courseStructureArray[3]);
                float C = Float.parseFloat(courseStructureArray[4]);
                if (C != catalog.getCredits()) {
                    throw new CommandLine.ParameterException(spec.commandLine(), "Course credits and course structure do not match.");
                }
                catalog.setL(L);
                catalog.setT(T);
                catalog.setP(P);
                catalog.setS(S);
                catalog.setC(C);
            }
            catalog.save();
            System.out.println("Course updated successfully.");
            return SUCCESS;
        } catch (SQLException e) {
            if (e.getSQLState().equalsIgnoreCase("42501")) {
                System.err.println("You do not have the required permissions to update a course in the catalog.");
                return UNAUTHORISED;
            }
            System.err.println("An error occurred while updating the course in the catalog.");
            return UNKNOWN;
        } catch (Exception e) {
            System.err.println("An error occurred while updating the course in the catalog.");
            return UNKNOWN;
        }
    }
}