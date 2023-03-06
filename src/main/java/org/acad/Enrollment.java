package org.acad;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.CSVWriter;
import database.access.DBConnectionSingleton;
import models.CourseCategory;
import picocli.CommandLine;

import java.io.FileReader;
import java.io.FileWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Callable;

import static database.access.Exception.*;


@CommandLine.Command(name = "enrol", description = "Enrol/Drop a course enrollment for an offering", mixinStandardHelpOptions = true, version = "enrollment 0.1")
public class Enrollment implements Callable<Integer> {
    @CommandLine.Option(names = {"-l", "--list"}, description = "List all enrollments for a student")
    boolean list;
    @CommandLine.Option(names = {"-e", "--entry"}, description = "Enroll a student in an offering(Leave blank to use your own)", defaultValue = "", echo = true, prompt = "Entry Number(Leave blank to use your own):")
    String entry;
    @CommandLine.Option(names = {"-i", "--id"}, description = "Offering Id", paramLabel = "Offering Id", descriptionKey = "Offering Id", defaultValue = "-1", interactive = true, arity = "0..1", echo = true, prompt = "Offering Id: ")
    int offeringId;
    @CommandLine.Option(names = {"-d", "--drop"}, description = "Drop a student from an offering")
    boolean drop;
    @CommandLine.Option(names = {"-a", "--audit"}, description = "Audit an offering")
    boolean audit;
    @CommandLine.Option(names = {"-w", "--withdraw"}, description = "Withdraw from an offering")
    boolean withdraw;
    @CommandLine.Option(names = {"-D", "--download"}, description = "Download the enrollment list for an offering in CSV format")
    boolean download;
    @CommandLine.Option(names = {"-u", "--upload"}, description = "Upload grades for an offering in CSV format", defaultValue = "", echo = true, prompt = "CSV File Name: ", interactive = true, arity = "0..1")
    String fileName;

    @Override
    public Integer call() {
        try {
            if (list) {
                ArrayList<models.Enrollment> enrollments = models.Enrollment.retrieveAll();
                ArrayList<models.Enrollment> studentEnrollments = new ArrayList<>();
                System.out.println(DBConnectionSingleton.getUserName());
                for (models.Enrollment e : enrollments) {
                    studentEnrollments.add(e);
                    System.out.println(offeringId);
                    boolean removed = false;
                    if (!entry.equals("") && !e.getEntryNo().equals(entry)) {
                        removed = true;
                        studentEnrollments.remove(studentEnrollments.size() - 1);
                    }
                    if (offeringId != -1 && e.getId() != offeringId && !removed) {
                        studentEnrollments.remove(studentEnrollments.size() - 1);
                    }
                }
                if (studentEnrollments.size() == 0) {
                    System.out.println("No such enrollments found.");
                    return SUCCESS;
                }

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
                    models.Offering offering = null;
                    for (models.Offering o : offerings) {
                        if (o.getId() == e.getId()) {
                            offering = o;
                        }
                    }
                    System.out.printf("%-" + columnWidth1 + "s %-" + columnWidth2 + "s %-" + columnWidth3 + "s %-" + columnWidth4 + "s %-" + columnWidth5 + "s %-" + columnWidth6 + "s%n", offering.getCode(), offering.getId(), e.getEntryNo(), e.getStatus(), e.getGrade(), e.getCourseType());

                }
                return SUCCESS;
            }
            if (offeringId == -1) {
                System.err.println("Offering Id is mandatory.");
                return SUCCESS;
            }
            if (entry.equals("")) {
                entry = DBConnectionSingleton.getUserName();
            }
            if (drop) {
                models.Enrollment enrollment = models.Enrollment.retrieve(offeringId, entry);
                if (enrollment == null) {
                    System.out.println("No enrollment found for the student.");
                    return NOT_EXISTS;
                }
                if (entry.equals(DBConnectionSingleton.getUserName())) {
                    enrollment.setStatus("Dropped by Student");
                } else {
                    enrollment.setStatus("Instructor Rejected");
                }
                String exitCode = enrollment.updateStatus();
                if (!exitCode.equals("00000")) {
                    System.out.println("An error occurred while dropping the enrollment.");
                    return handleSQLException(exitCode, "An error occurred while dropping the enrollment.");
                }
                System.out.println("Enrollment dropped successfully.");
                return SUCCESS;
            }
            if (audit) {
                models.Enrollment enrollment = models.Enrollment.retrieve(offeringId, entry);
                if (enrollment == null) {
                    System.out.println("No enrollment found for the student.");
                    return NOT_EXISTS;
                }
                enrollment.setStatus("Audit");
                String exitCode = enrollment.updateStatus();
                if (!exitCode.equals("00000")) {
                    System.out.println("An error occurred while auditing the enrollment.");
                    return handleSQLException(exitCode, "An error occurred while auditing the enrollment.");
                }
                System.out.println("Enrollment audited successfully.");
                return SUCCESS;
            }
            if (withdraw) {
                models.Enrollment enrollment = models.Enrollment.retrieve(offeringId, entry);
                if (enrollment == null) {
                    System.out.println("No enrollment found for the student.");
                    return NOT_EXISTS;
                }
                enrollment.setStatus("Withdrawn");
                String exitCode = enrollment.updateStatus();
                if (!exitCode.equals("00000")) {
                    System.out.println("An error occurred while withdrawing the enrollment.");
                    return handleSQLException(exitCode, "An error occurred while withdrawing the enrollment.");
                }
                System.out.println("Enrollment withdrawn successfully.");
                return SUCCESS;
            }
            if (download) {
                ArrayList<models.Enrollment> enrollments = models.Enrollment.retrieveAll();
                ArrayList<models.Enrollment> offeringEnrollments = new ArrayList<>();
                for (models.Enrollment e : enrollments) {
                    if (e.getId() == offeringId) {
                        offeringEnrollments.add(e);
                    }
                }
                if (offeringEnrollments.size() == 0) {
                    System.out.println("No enrollments found for the offering.");
                    return NOT_EXISTS;
                }
                CSVWriter writer = new CSVWriter(new FileWriter("enrollment.csv"));
                String[] heading = {"Offering Id", "Entry Number", "Status", "Grade", "Type"};
                writer.writeNext(heading);
                for (models.Enrollment e : offeringEnrollments) {
                    String[] record = {String.valueOf(e.getId()), e.getEntryNo(), e.getStatus(), e.getGrade(), e.getCourseType()};
                    writer.writeNext(record);
                }
                writer.close();
                System.out.println("Enrollment list downloaded and saved at " + System.getProperty("user.dir") + "/enrollment.csv");
                return SUCCESS;
            }
            if (!fileName.equals("")) {
                CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new FileReader(fileName));
                Map<String, String> record;
                while ((record = reader.readMap()) != null) {
                    models.Enrollment enrollment = models.Enrollment.retrieve(Integer.parseInt(record.get("Offering Id")), record.get("Entry Number"));
                    if (enrollment == null) {
                        System.err.println("No enrollment found for the student: " + record.get("Entry Number"));
                        continue;
                    }
                    enrollment.setGrade(record.get("Grade"));
                    System.out.println("Updating enrollment for the student: " + record.get("Entry Number"));
                    String exitCode = enrollment.save();
                    if (!exitCode.equals("00000")) {
                        System.err.println("An error occurred while updating the enrollment for the student: " + record.get("Entry Number"));
                        return handleSQLException(exitCode, "An error occurred while updating the enrollment for the student: " + record.get("Entry Number"));
                    }
                }
                System.out.println("Grades submitted Successfully");
                return SUCCESS;
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
                return NOT_EXISTS;
            }
            ArrayList<CourseCategory> courseCategory = CourseCategory.retrieveAll();
            for (CourseCategory c : courseCategory) {
                if (c.getDepartment().equals(student.getDepartmentCode())
                        && c.getEntryYear() == student.getEntryYear()
                        && c.getProgram().equals(student.getProgram())
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
