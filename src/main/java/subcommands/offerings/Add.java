package subcommands.offerings;

import database.access.Transaction;
import picocli.CommandLine;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import static database.access.DBConnectionSingleton.getUserName;
import static database.access.Exception.*;

@CommandLine.Command(name = "add", description = "Add an offering to the database")
public class Add implements Callable<Integer> {
    @CommandLine.Option(names = {"-c", "--course"}, description = "Course Code", required = true, interactive = true, arity = "0..1", echo = true, prompt = "Course Code: ")
    private String courseCode;
    @CommandLine.Option(names = {"-s", "--session"}, description = "Session", required = true, interactive = true, arity = "0..1", echo = true, prompt = "Academic Session: ")
    private String session;
    @CommandLine.Option(names = {"-S", "--section"}, description = "Section", required = true, interactive = true, arity = "0..1", echo = true, prompt = "Section: ")
    private int section;
    @CommandLine.Option(names = {"-g", "--CGPA"}, description = "CGPA Cuttoff", required = true, interactive = true, arity = "0..1", echo = true, prompt = "CGPA Cuttoff: ")
    private float cgpaCutoff;
    @CommandLine.Option(names = {"-i", "--instructor"}, description = "Instructor", interactive = true, arity = "0..1", echo = true, prompt = "Coordinator Email(Leave Blank to set your own): ", defaultValue = "")
    private String instructor;
    @CommandLine.Option(names = {"-d", "--department"}, description = "Department", interactive = true, arity = "0..1", echo = true, prompt = "Department(leave blank to set your own): ", defaultValue = "")
    private String department;

    @Override
    public Integer call() {
        try {
            models.Offering offering = new models.Offering();
            offering.setCode(courseCode);
            // Session: 2020-II
            int year = Integer.parseInt(session.split("-")[0]);
            String semester = session.split("-")[1];
            offering.setYear(year);
            offering.setSemester(semester);
            offering.setSection(section);
            offering.setMinCGPA(cgpaCutoff);
            offering.setStatus("Proposed");
            if (instructor.equals("")) {
                instructor = getUserName();
                System.out.println(instructor);
            }
            System.out.println(instructor);
            models.Instructor instructorObject = models.Instructor.retrieve(instructor);
            if (instructorObject == null) {
                System.out.println("You are not an instructor.");
                return UNAUTHORISED;
            }
            offering.setCoordinator(instructor);
            if (department.equals("")) {
                department = instructorObject.getDepartmentCode();
            }
            offering.setDepartment(department);
            Transaction.start();
            String exitCode1 = offering.save();
            models.TeachingTeam teachingTeam = new models.TeachingTeam();
            teachingTeam.setId(offering.getId());
            teachingTeam.setInstructor(offering.getCoordinator());
            teachingTeam.setCoordinator(true);
            String exitCode2 = teachingTeam.save();
            if (!exitCode1.equals("00000") || !exitCode2.equals("00000")) {
                System.out.println("An error occurred while adding the offering.");
                Transaction.rollback();
                return handleSQLException(exitCode1, "Insertion failed");
            }
            ArrayList<models.DefaultPrerequisite> defaultPrerequisites = models.DefaultPrerequisite.retrieveAll();
            for (models.DefaultPrerequisite defaultPrerequisite : defaultPrerequisites) {
                if (defaultPrerequisite.getCatalogCode().equals(offering.getCode())) {
                    models.Prerequisite prerequisite = new models.Prerequisite();
                    prerequisite.setId(offering.getId());
                    prerequisite.setPrerequisiteCode(defaultPrerequisite.getPrerequisiteCode());
                    prerequisite.setMinGrade(defaultPrerequisite.getMinGrade());
                    exitCode1 = prerequisite.save();
                    if (!exitCode1.equals("00000")) {
                        System.out.println("An error occurred while adding the offering.");
                        Transaction.rollback();
                        return handleSQLException(exitCode1, "Insertion failed");
                    }
                }
            }
            Transaction.commit();
            System.out.println("Offering added successfully.");
            return SUCCESS;
        } catch (SQLException e) {
            Transaction.rollback();
            e.printStackTrace();
            return handleSQLException(e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
            System.err.println("An error occurred while retrieving the catalog.");
            return UNKNOWN;
        }
    }
}
