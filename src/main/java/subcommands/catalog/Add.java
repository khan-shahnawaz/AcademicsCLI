package subcommands.catalog;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

@Command(name = "add", mixinStandardHelpOptions = true, version = "catalog 0.1",
        description = "Adds a course to the catalog.")
public class Add implements Callable<Integer> {
    public static int SUCCESS = 0;
    public static int ALREADY_EXISTS = 1;
    public static int UNAUTHORISED = 2;
    public static int UNKNOWN = 3;
    public static int NOT_EXISTS = 4;
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;
    @Option(names = {"-C", "--code"}, description = "Code of the course.", interactive = true, required = true, arity = "0..1", echo = true, prompt = "Course Code: ")
    private String courseCode;
    @Option(names = {"-n", "--name"}, description = "Name of the course.", interactive = true, required = true, arity = "0..1", echo = true, prompt = "Course Name: ")
    private String courseName;
    @Option(names = {"-d", "--description"}, description = "Description of the course.", interactive = true, required = true, arity = "0..1", echo = true, prompt = "Course Description: ")
    private String courseDescription;
    @Option(names = {"-c", "--credits"}, description = "Credits of the course.", interactive = true, required = true, arity = "0..1", echo = true, prompt = "Course Credits: ")
    private float courseCredits;
    @Option(names = {"-s", "--structure"}, description = "Structure of the course.", interactive = true, required = true, arity = "0..1", echo = true, prompt = "Course Structure: ")
    private String courseStructure;

    @Override
    public Integer call() throws Exception {
        models.Catalog catalog = new models.Catalog();
        catalog.setCode(courseCode);
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
            throw new CommandLine.ParameterException(spec.commandLine(), "Course credits and course structure do not match.");
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
            return ALREADY_EXISTS;
        }
        if (exitCode.equals("42501")) {
            System.err.println("You do not have the required permissions to add a course to the catalog.");
            return UNAUTHORISED;
        }
        if (!exitCode.equals("00000")) {
            System.err.println("An error occurred while adding the course to the catalog.");
            return UNKNOWN;
        }
        System.out.print("Course added successfully.");
        return SUCCESS;
    }
}