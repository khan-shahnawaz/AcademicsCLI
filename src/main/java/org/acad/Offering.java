package org.acad;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import subcommands.offerings.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import static database.access.Exception.*;

@Command(name = "offerings", mixinStandardHelpOptions = true, version = "offerings 0.1",
        description = "Contains the functionality for displaying and managing course offerings.", subcommands = {Add.class, Prerequisite.class, Update.class, Category.class, Teacher.class})
public class Offering implements Callable<Integer> {
    @Option(names = {"-l", "--list"}, description = "List all offerings.")
    private boolean list;
    @Option(names = {"-s", "--session"}, description = "List all offerings for a session.", defaultValue = "")
    private String session;
    @Option(names = {"-d", "--department"}, description = "List all offerings for a department.", defaultValue = "")
    private String department;
    @Option(names = {"-D", "--details"}, description = "View the details of the offering code provided.", defaultValue = "-1")
    private int offeringCode;

    @Override
    public Integer call() {
        try {
            if (list) {
                ArrayList<models.Offering> offerings = models.Offering.retrieveAll();
                ArrayList<models.Offering> filteredOfferings = new ArrayList<>();
                for (models.Offering offering : offerings) {
                    if (!session.equals("")) {
                        //Session of the form 2020-II
                        int year = Integer.parseInt(session.split("-")[0]);
                        String semester = session.split("-")[1];
                        if (offering.getYear() == year && offering.getSemester().equals(semester)) {
                            filteredOfferings.add(offering);
                        }
                    } else {
                        filteredOfferings.add(offering);
                    }
                }
                offerings = filteredOfferings;
                filteredOfferings = new ArrayList<>();
                for (models.Offering offering : offerings) {
                    if (!department.equals("")) {
                        if (offering.getDepartment().equals(department)) {
                            filteredOfferings.add(offering);
                        }
                    } else {
                        filteredOfferings.add(offering);
                    }
                }
                offerings = filteredOfferings;
                System.out.println("Offerings:");
                if (offerings.size() == 0) {
                    System.out.println("No offerings found.");
                    return SUCCESS;
                }
                ArrayList<models.Catalog> catalogs = new ArrayList<>();
                for (models.Offering offering : offerings) {
                    catalogs.add(models.Catalog.retrieve(offering.getCode()));
                }
                String header1 = "Id";
                int columnWidth1 = Math.max(
                        header1.length() + 2,
                        offerings.stream().mapToInt(o -> String.valueOf(o.getId()).length() + 2).max().orElse(header1.length() + 2)
                );
                String header2 = "Course Code";
                int columnWidth2 = Math.max(
                        header2.length() + 2,
                        offerings.stream().mapToInt(o -> o.getCode().length() + 2).max().orElse(header2.length() + 2)
                );
                String header3 = "Course Name";
                int columnWidth3 = Math.max(
                        header3.length() + 2,
                        catalogs.stream().mapToInt(o -> o.getName().length() + 2).max().orElse(header3.length() + 2)
                );
                String header4 = "Department";
                int columnWidth4 = Math.max(
                        header4.length() + 2,
                        offerings.stream().mapToInt(o -> o.getDepartment().length() + 2).max().orElse(header4.length() + 2)
                );
                String header5 = "Session";
                int columnWidth5 = Math.max(
                        header5.length() + 2,
                        offerings.stream().mapToInt(o -> (o.getYear() + "-" + o.getSemester()).length() + 2).max().orElse(header5.length() + 2)
                );
                String header6 = "Section";
                int columnWidth6 = Math.max(
                        header6.length() + 2,
                        offerings.stream().mapToInt(o -> String.valueOf(o.getSection()).length() + 2).max().orElse(header6.length() + 2)
                );
                String header7 = "Coordinator";
                int columnWidth7 = Math.max(
                        header7.length() + 2,
                        offerings.stream().mapToInt(o -> o.getCoordinator().length() + 2).max().orElse(header7.length() + 2)
                );
                String header8 = "Status";
                int columnWidth8 = Math.max(
                        header8.length() + 2,
                        offerings.stream().mapToInt(o -> o.getStatus().length() + 2).max().orElse(header8.length() + 2)
                );
                String header9 = "CGPA Req.";
                int columnWidth9 = Math.max(
                        header9.length() + 2,
                        offerings.stream().mapToInt(o -> String.valueOf(o.getMinCGPA()).length() + 2).max().orElse(header9.length() + 2)
                );
                String header10 = "L-T-P-S-C Structure";
                int columnWidth10 = Math.max(
                        header10.length() + 2,
                        catalogs.stream().mapToInt(o -> (o.getL() + "-" + o.getT() + "-" + o.getP() + "-" + o.getS() + "-" + o.getC()).length() + 2).max().orElse(header10.length() + 2)
                );
                String header = String.format("%-" + columnWidth1 + "s", header1) + String.format("%-" + columnWidth2 + "s", header2) + String.format("%-" + columnWidth3 + "s", header3) + String.format("%-" + columnWidth4 + "s", header4) + String.format("%-" + columnWidth5 + "s", header5) + String.format("%-" + columnWidth6 + "s", header6) + String.format("%-" + columnWidth7 + "s", header7) + String.format("%-" + columnWidth8 + "s", header8) + String.format("%-" + columnWidth9 + "s", header9) + String.format("%-" + columnWidth10 + "s", header10);
                System.out.println(header);
                for (models.Offering offering : offerings) {
                    models.Catalog catalog = models.Catalog.retrieve(offering.getCode());
                    String row = String.format("%-" + columnWidth1 + "s", offering.getId()) + String.format("%-" + columnWidth2 + "s", offering.getCode()) + String.format("%-" + columnWidth3 + "s", catalog.getName()) + String.format("%-" + columnWidth4 + "s", offering.getDepartment()) + String.format("%-" + columnWidth5 + "s", offering.getYear() + "-" + offering.getSemester()) + String.format("%-" + columnWidth6 + "s", offering.getSection()) + String.format("%-" + columnWidth7 + "s", offering.getCoordinator()) + String.format("%-" + columnWidth8 + "s", offering.getStatus()) + String.format("%-" + columnWidth9 + "s", offering.getMinCGPA()) + String.format("%-" + columnWidth10 + "s", catalog.getL() + "-" + catalog.getT() + "-" + catalog.getP() + "-" + catalog.getS() + "-" + catalog.getC());
                    System.out.println(row);
                }
                return SUCCESS;
                // Print the table header Id | Course Code | Course Name | Department | Session | Section | Coordinator | Status | CGPA Req. | L-T-P-S-C Structure

            }
            if (offeringCode != -1) {
                ArrayList<models.Offering> offerings = models.Offering.retrieveAll();
                models.Offering thisOffering = null;
                for (models.Offering offering : offerings) {
                    if (offering.getId() == offeringCode) {
                        thisOffering = offering;
                        break;
                    }
                }
                if (thisOffering == null) {
                    System.out.println("No offering found with the given code.");
                    return SUCCESS;
                }

                models.Catalog catalog = models.Catalog.retrieve(thisOffering.getCode());
                ArrayList<models.Prerequisite> prerequisites = models.Prerequisite.retrieveAll();
                ArrayList<models.Prerequisite> filteredPrerequisites = new ArrayList<>();
                for (models.Prerequisite prerequisite : prerequisites) {
                    if (prerequisite.getId() == thisOffering.getId()) {
                        filteredPrerequisites.add(prerequisite);
                    }
                }
                prerequisites = filteredPrerequisites;
                ArrayList<models.TeachingTeam> teachingTeams = models.TeachingTeam.retrieveAll();
                ArrayList<models.TeachingTeam> filteredTeachingTeams = new ArrayList<>();
                for (models.TeachingTeam teachingTeam : teachingTeams) {
                    if (teachingTeam.getId() == thisOffering.getId()) {
                        filteredTeachingTeams.add(teachingTeam);
                    }
                }
                teachingTeams = filteredTeachingTeams;
                ArrayList<models.CourseCategory> courseCategories = models.CourseCategory.retrieveAll();
                ArrayList<models.CourseCategory> filteredCourseCategories = new ArrayList<>();
                for (models.CourseCategory courseCategory : courseCategories) {
                    if (courseCategory.getId() == thisOffering.getId()) {
                        filteredCourseCategories.add(courseCategory);
                    }
                }
                courseCategories = filteredCourseCategories;

                System.out.println("Offering Id: " + thisOffering.getId());
                System.out.println("Course Code: " + thisOffering.getCode());
                System.out.println("Course Name: " + catalog.getName());
                System.out.println("Offering Department: " + thisOffering.getDepartment());
                System.out.println("Session: " + thisOffering.getYear() + "-" + thisOffering.getSemester());
                System.out.println("Section: " + thisOffering.getSection());
                models.Instructor coordinator = models.Instructor.retrieve(thisOffering.getCoordinator());
                System.out.println("Coordinator: " + coordinator.getName());
                System.out.println("Status: " + thisOffering.getStatus());
                System.out.println("CGPA Requirement: " + thisOffering.getMinCGPA());
                System.out.println("L-T-P-S-C Structure: " + catalog.getL() + "-" + catalog.getT() + "-" + catalog.getP() + "-" + catalog.getS() + "-" + catalog.getC());
                System.out.println("\nPrerequisites: ");
                System.out.println("Prerequisite Code | Minimum Grade");
                for (models.Prerequisite prerequisite : prerequisites) {
                    System.out.println(prerequisite.getPrerequisiteCode() + " \t\t\t " + prerequisite.getMinGrade());
                }
                System.out.println("\nTeaching Team: \n");
                // Instructor Name[Coordinator]
                ArrayList<String> instructorNames = new ArrayList<>();
                for (models.TeachingTeam teachingTeam : teachingTeams) {
                    models.Instructor instructor = models.Instructor.retrieve(teachingTeam.getInstructor());
                    instructorNames.add(instructor.getName());
                }
                for (int i = 0; i < instructorNames.size(); i++) {
                    if (teachingTeams.get(i).isCoordinator()) {
                        System.out.println(instructorNames.get(i) + "[Coordinator]");
                    } else {
                        System.out.println(instructorNames.get(i));
                    }
                }
                if (courseCategories.size() > 0) {
                    System.out.println("\nCourse Categorization: ");
                    // Entry Year | Department | Course Type | Program
                    System.out.println("Entry Year  Department  Course Type  Program");
                }
                for (models.CourseCategory courseCategory : courseCategories) {
                    System.out.println(courseCategory.getEntryYear() + " \t\t " + courseCategory.getDepartment() + " \t " + courseCategory.getType() + " \t " + courseCategory.getProgram());
                }
            }

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
