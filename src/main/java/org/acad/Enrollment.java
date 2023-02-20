package org.acad;


import database.access.DBConnectionSingleton;
import models.CourseCategory;
import picocli.CommandLine;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import static database.access.Exception.*;


@CommandLine.Command(name = "enrollment", description = "Enrol/Drop a course enrollment for an offering", mixinStandardHelpOptions = true, version = "enrollment 0.1")
public class Enrollment implements Callable<Integer> {
    @CommandLine.Option(names = {"-l", "--list"}, description = "List all enrollments for a student")
    boolean list;
    @CommandLine.Option(names = {"-e", "--entry"}, description = "Enroll a student in an offering(Leave blank to use your own)", defaultValue = "", echo = true, prompt = "Entry Number(Leave blank to use your own):")
    String entry;
    @CommandLine.Option(names = {"-i", "--id"}, description = "Offering Id", paramLabel = "Offering Id", descriptionKey = "Offering Id")
    int offeringId;
    @CommandLine.Option(names = {"-d", "--drop"}, description = "Drop a student from an offering")
    boolean drop;

    @Override
    public Integer call() {
        try {
            if (list) {
                if (entry.equals("")) {
                    entry = DBConnectionSingleton.getUserName();
                }
                ArrayList<models.Enrollment> enrollments = models.Enrollment.retrieveAll();
                ArrayList<models.Enrollment> studentEnrollments = new ArrayList<>();
                for (models.Enrollment e : enrollments) {
                    if (e.getEntryNo().equals(entry)) {
                        studentEnrollments.add(e);
                    }
                }
                if (studentEnrollments.size() == 0) {
                    System.out.println("No enrollments found for the student.");
                    return SUCCESS;
                }

                // Enrollments in the following format:
                //Course Name, Offering Id, Entry Number, Status, Grade, Type

                System.out.println("Enrollments:");
                String heading1 = "Course Code";
                String heading2 = "Offering Id";
                String heading3 = "Entry Number";
                String heading4 = "Status";
                String heading5 = "Grade";
                String heading6 = "Type";

                int columnWidth1 = heading1.length() + 2;
                int columnWidth2 = heading2.length() + 2;
                int columnWidth3 = heading3.length() + 2;
                int columnWidth4 = 3 * heading4.length() + 2;
                int columnWidth5 = heading5.length() + 2;
                int columnWidth6 = heading6.length() + 2;

                System.out.printf("%-" + columnWidth1 + "s %-" + columnWidth2 + "s %-" + columnWidth3 + "s %-" + columnWidth4 + "s %-" + columnWidth5 + "s %-" + columnWidth6 + "s%n", heading1, heading2, heading3, heading4, heading5, heading6);
                for (models.Enrollment e : studentEnrollments) {
                    ArrayList<models.Offering> offerings = models.Offering.retrieveAll();
                    models.Offering offering = offerings.stream().filter(o -> o.getId() == offeringId).findFirst().orElse(null);
                    System.out.printf("%-" + columnWidth1 + "s %-" + columnWidth2 + "s %-" + columnWidth3 + "s %-" + columnWidth4 + "s %-" + columnWidth5 + "s %-" + columnWidth6 + "s%n", offering.getCode(), offering.getId(), e.getEntryNo(), e.getStatus(), e.getGrade(), e.getCourseType());

                }
                return SUCCESS;
            }
            if (drop) {
                if (entry.equals("")) {
                    entry = DBConnectionSingleton.getUserName();
                }
                models.Enrollment enrollment = models.Enrollment.retrieve(offeringId, entry);
                if (enrollment == null) {
                    System.out.println("No enrollment found for the student.");
                    return SUCCESS;
                }
                if (entry.equals(DBConnectionSingleton.getUserName())) {
                    enrollment.setStatus("Dropped by Student");
                } else {
                    enrollment.setStatus("Instructor Rejected");
                }
                String exitCode = enrollment.save();
                if (!exitCode.equals("00000")) {
                    System.out.println("An error occurred while dropping the enrollment.");
                    return handleSQLException(exitCode, "An error occurred while dropping the enrollment.");
                }
                System.out.println("Enrollment dropped successfully.");
                return SUCCESS;
            }
            if (entry.equals("")) {
                entry = DBConnectionSingleton.getUserName();
            }
            models.Enrollment enrollment = new models.Enrollment();
            enrollment.setEntryNo(entry);
            enrollment.setId(offeringId);
            enrollment.setStatus("Enrolled");
            enrollment.setGrade("NA");
            enrollment.setCourseType("OE");
            models.Student student = models.Student.retrieve(entry);
            if (student == null) {
                System.out.println("No student found with the given entry number.");
                return SUCCESS;
            }
            ArrayList<CourseCategory> courseCategory = CourseCategory.retrieveAll();
            for (CourseCategory c : courseCategory) {
                if (c.getDepartment() == student.getDepartmentCode()
                        && c.getEntryYear() == student.getEntryYear()
                        && c.getProgram() == student.getProgram()
                        && c.getId() == offeringId
                ) {
                    enrollment.setCourseType(c.getType());
                }
            }

            String exitCode = enrollment.save();
            if (!exitCode.equals("00000")) {
                System.out.println("An error occurred while enrolling the student.");
                return handleSQLException(exitCode, "An error occurred while enrolling the student.");
            }
            System.out.println("Student enrolled successfully.");
            return SUCCESS;

        } catch (SQLException e) {
            return handleSQLException(e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("An error occurred while retrieving the catalog.");
            return UNKNOWN;
        }
    }
}
