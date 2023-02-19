package subcommands.curriculum;

import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

import static database.access.Exception.SUCCESS;
import static database.access.Exception.handleSQLException;

@CommandLine.Command(name = "add", mixinStandardHelpOptions = true, description = "Add an entry to the curriculum.")
public class Add implements Callable<Integer> {
    @Option(names = {"-p", "--program"}, description = "Program Code", required = true)
    private String program;
    @Option(names = {"-t", "--type"}, description = "Type of curriculum entry", required = true)
    private String type;
    @Option(names = {"-c", "--credits"}, description = "Minimum Credits", required = true)
    private float credits;

    @Override
    public Integer call() {
        models.Curriculum curriculum = new models.Curriculum();
        curriculum.setProgram(program);
        curriculum.setCourseType(type);
        curriculum.setMinCredits(credits);
        String exitCode = curriculum.save();
        if (!exitCode.equals("00000")) {
            System.err.println("An error occurred while adding the curriculum entry.");
            return handleSQLException(exitCode, "Insertion failed.");
        }
        System.out.print("Curriculum entry added successfully.");
        return SUCCESS;

    }
}
