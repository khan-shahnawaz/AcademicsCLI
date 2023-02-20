package subcommands.offerings;

import picocli.CommandLine;

import java.sql.SQLException;
import java.util.concurrent.Callable;

import static database.access.Exception.*;

@CommandLine.Command(name = "category", description = "Update the course categorization information to the database")
public class Category implements Callable<Integer> {
    @CommandLine.Option(names = {"-r", "--remove"}, description = "Remove a prerequisite from a course in the Offering.")
    boolean remove;
    @CommandLine.Option(names = {"-i", "--id"}, description = "Offering Id", paramLabel = "courseCode", descriptionKey = "courseCode", interactive = true, echo = true, arity = "0..1", prompt = "Enter the offering id: ")
    int offeringId;
    @CommandLine.Option(names = {"-c", "--category"}, description = "Category(PC,PE,HC,HE,GE etc.)", paramLabel = "category", descriptionKey = "Category(PC,PE,HC,HE,GE etc.)", interactive = true, echo = true, arity = "0..1", prompt = "Enter the category: ")
    String category;
    @CommandLine.Option(names = {"-y", "--year"}, description = "Entry Year", paramLabel = "year", descriptionKey = "Year", interactive = true, echo = true, arity = "0..1", prompt = "Enter the entry year: ")
    int year;
    @CommandLine.Option(names = {"-p", "--program"}, description = "Program", paramLabel = "program", descriptionKey = "Program", interactive = true, echo = true, arity = "0..1", prompt = "Enter the program: ")
    String program;
    @CommandLine.Option(names = {"-d", "--department"}, description = "Department", paramLabel = "department", descriptionKey = "Department", interactive = true, echo = true, arity = "0..1", prompt = "Enter the department: ")
    String department;

    @Override
    public Integer call() {
        try {
            if (remove) {
                models.CourseCategory courseCategory = models.CourseCategory.retrieve(offeringId, category, year, department, program);
                if (courseCategory == null) {
                    System.err.println("Course Category does not exist in the offering.");
                    return NOT_EXISTS;
                }
                String exitCode = courseCategory.delete();
                if (!exitCode.equals("00000")) {
                    System.err.println("An error occurred while removing the entry from the offering.");
                    return handleSQLException(exitCode, "Operation failed.");
                }
                System.out.println("Entry removed from the offering.");
                return SUCCESS;
            }
            models.CourseCategory courseCategory = new models.CourseCategory();
            courseCategory.setType(category);
            courseCategory.setEntryYear(year);
            courseCategory.setDepartment(department);
            courseCategory.setProgram(program);
            courseCategory.setId(offeringId);
            String exitCode = courseCategory.save();
            if (!exitCode.equals("00000")) {
                System.err.println("An error occurred while adding the entry to the offering.");
                return handleSQLException(exitCode, "Operation failed.");
            }
            System.out.println("Entry added to the offering.");
            return SUCCESS;
        } catch (SQLException e) {
            return handleSQLException(e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred while removing a prerequisite to the Offering.");
            return UNKNOWN;
        }
    }
}
