package subcommands.calender;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

import static database.access.Exception.SUCCESS;
import static database.access.Exception.handleSQLException;

@Command(name = "add", mixinStandardHelpOptions = true, version = "calender 0.1",
        description = "Adds an Event to the calender.")
public class Add implements Callable<Integer> {
    @Option(names = {"-s", "--session"}, description = "Session of the event.", required = true, interactive = true, echo = true, arity = "0..1", prompt = "Session(YYYY-I/YYYY-II/YYYY-S): ")
    private String session;
    @Option(names = {"-e", "--event"}, description = "Academic Event('Academic Session', 'Course Add/Drop', 'Grade Submission', 'Course Withdrawal/Audit')", required = true, interactive = true, echo = true, prompt = "Academic Event: ", arity = "0..1")
    private String event;
    @Option(names = {"-S", "--start"}, description = "Start date of the event.", required = true, interactive = true, echo = true, prompt = "Start Date: ", arity = "0..1")
    private String startDate;
    @Option(names = {"-E", "--end"}, description = "End date of the event.", required = true, interactive = true, echo = true, prompt = "End Date: ", arity = "0..1")
    private String endDate;

    @Override
    public Integer call() {
        models.AcademicCalender calender = new models.AcademicCalender();
        String[] sessionArray = session.split("-");
        int year = Integer.parseInt(sessionArray[0]);
        String semester = sessionArray[1];
        calender.setYear(year);
        calender.setSemester(semester);
        calender.setEvent(event);
        calender.setStartDate(startDate);
        calender.setEndDate(endDate);
        String exitCode = calender.save().toLowerCase();
        if (!exitCode.equals("00000")) {
            System.err.println("An error occurred while adding the event to the calender.");
            return handleSQLException(exitCode, "Insertion failed.");
        }
        System.out.print("Event added successfully.");
        return SUCCESS;
    }
}