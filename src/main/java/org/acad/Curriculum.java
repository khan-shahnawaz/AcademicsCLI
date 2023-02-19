package org.acad;

import picocli.CommandLine;
import subcommands.curriculum.Add;
import subcommands.curriculum.Remove;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import static database.access.Exception.SUCCESS;
import static database.access.Exception.UNKNOWN;

@CommandLine.Command(name = "curriculum", mixinStandardHelpOptions = true, version = "calender 0.1",
        description = "Contains the functionality for displaying and change the academic Curriculum.", subcommands = {Add.class, Remove.class})
public class Curriculum implements Callable<Integer> {
    @CommandLine.Option(names = {"-l", "--list"}, description = "List all courses in the catalog.")
    boolean list;
    @CommandLine.Option(names = {"-p", "--program"}, description = "Program for which the curriculum is to be displayed.", defaultValue = "")
    String program;

    @Override
    public Integer call() {
        try {
            if (list) {
                ArrayList<models.Curriculum> curriculums = models.Curriculum.retrieveAll();
                if (!program.equals("")) {
                    ArrayList<models.Curriculum> curriculumsForProgram = new ArrayList<>();
                    for (models.Curriculum curriculum : curriculums) {
                        if (curriculum.getProgram().equals(program)) {
                            curriculumsForProgram.add(curriculum);
                        }
                    }
                    curriculums = curriculumsForProgram;
                }
                if (curriculums.size() == 0) {
                    System.out.println("No Such entries" +
                            " found.");
                    return SUCCESS;
                }
                System.out.println("Curriculums:");
                // Print the table header Program Type Minimum Credits
                System.out.printf("%-20s %-20s %-20s%n", "Program", "Type", "Minimum Credits");
                for (models.Curriculum curriculum : curriculums) {
                    System.out.printf("%-20s %-20s %-20s%n", curriculum.getProgram(), curriculum.getCourseType(), curriculum.getMinCredits());
                }
                return SUCCESS;
            }
            return SUCCESS;
        } catch (Exception e) {
            return UNKNOWN;
        }
    }
}
