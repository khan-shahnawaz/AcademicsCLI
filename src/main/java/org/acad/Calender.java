package org.acad;

import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;
import subcommands.calender.Add;
import subcommands.calender.Remove;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import static database.access.Exception.SUCCESS;
import static database.access.Exception.UNKNOWN;

/**
 * Class to deal with subcommand catalog.
 *
 * @author Shahnawaz Khan
 * @version 1.0
 * @since 2023-02-17
 */
@Command(name = "calender", mixinStandardHelpOptions = true, version = "calender 0.1",
        description = "Contains the functionality for displaying and changing the academic Calender.", subcommands = {Add.class, Remove.class})
public class Calender implements Callable<Integer> {
    @Option(names = {"-l", "--list"}, description = "List all courses in the catalog.")
    private boolean list;
    @Option(names = {"-s", "--session"}, description = "Calender for the session. e.g 2020-II", defaultValue = "")
    private String session;

    @Override
    public Integer call() {
        try {
            if (list) {
                ArrayList<models.AcademicCalender> events = models.AcademicCalender.retrieveAll();
                if (!session.equals("")) {
                    int year = Integer.parseInt(session.split("-")[0]);
                    String semester = session.split("-")[1];
                    ArrayList<models.AcademicCalender> eventsForSession = new ArrayList<>();
                    for (models.AcademicCalender event : events) {
                        if (event.getYear() == year && event.getSemester().equals(semester)) {
                            eventsForSession.add(event);
                        }
                    }
                    events = eventsForSession;
                }
                if (events.size() == 0) {
                    System.out.println("No events found.");
                    return SUCCESS;
                }
                System.out.println("Events:");
                // Print the table header Semester, Year, Event, Start Date, End Date
                System.out.printf("%-10s %-10s %-20s %-20s %-20s%n", "Semester", "Year", "Event", "Start Date", "End Date");
                for (models.AcademicCalender event : events) {
                    System.out.printf("%-10s %-10s %-20s %-20s %-20s%n", event.getSemester(), event.getYear(), event.getEvent(), event.getStartDate(), event.getEndDate());
                }
                return SUCCESS;
            }
            System.out.println("Use -l or --list to list all the events.");
            return SUCCESS;
        } catch (Exception e) {
            System.err.println("An error occurred while retrieving the academic calender.");
            return UNKNOWN;
        }
    }
}
