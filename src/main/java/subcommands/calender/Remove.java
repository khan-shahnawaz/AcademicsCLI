package subcommands.calender;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.sql.SQLException;
import java.util.concurrent.Callable;

import static database.access.Exception.*;

@Command(name = "remove", mixinStandardHelpOptions = true, version = "calender 0.1",
        description = "Removes an Event to the calender.")
public class Remove implements Callable<Integer> {
    @Option(names = {"-s", "--session"}, description = "Session of the event.", required = true)
    private String session;
    @Option(names = {"-e", "--event"}, description = "Academic Event('Academic Session', 'Course Add/Drop', 'Grade Submission', 'Course Withdrawal/Audit')", required = true, interactive = true, echo = true, prompt = "Academic Event: ", arity = "0..1")
    private String event;

    @Override
    public Integer call() throws Exception {
        try {
            String[] sessionArray = session.split("-");
            int year = Integer.parseInt(sessionArray[0]);
            String semester = sessionArray[1];
            models.AcademicCalender calender = models.AcademicCalender.retrieve(year, semester, event);
            if (calender == null) {
                System.err.println("Event does not exist in the calender.");
                return NOT_EXISTS;
            }
            String exitCode = calender.delete();
            if (!exitCode.equals("00000")) {
                System.err.println("An error occurred while removing the event from the calender.");
                return handleSQLException(exitCode, "Deletion failed.");
            }
            System.out.print("Event removed successfully.");
            return SUCCESS;
        } catch (SQLException e) {
            return database.access.Exception.handleSQLException(e.getSQLState(), e.getMessage());
        }
    }
}