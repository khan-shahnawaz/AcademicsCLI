package subcommands.curriculum;

import picocli.CommandLine;

import java.sql.SQLException;
import java.util.concurrent.Callable;

import static database.access.Exception.*;

@CommandLine.Command(name = "remove", mixinStandardHelpOptions = true, description = "Remove an entry from the curriculum.")
public class Remove implements Callable<Integer> {

    @CommandLine.Option(names = {"-p", "--program"}, description = "Program Code", required = true)
    private String program;
    @CommandLine.Option(names = {"-t", "--type"}, description = "Type of curriculum entry", required = true)
    private String type;

    @Override
    public Integer call() {
        models.Curriculum curriculum;
        try {
            curriculum = models.Curriculum.retrieve(program, type);
            if (curriculum == null) {
                System.err.println("Curriculum entry does not exist.");
                return NOT_EXISTS;
            }
            curriculum.delete();
            System.out.println("Curriculum entry removed successfully.");
        } catch (SQLException e) {
            System.err.println("An error occurred while removing the curriculum entry.");
            handleSQLException(e.getSQLState(), e.getMessage());
            return UNKNOWN;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return SUCCESS;
    }
}
