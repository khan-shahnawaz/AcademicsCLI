package org.acad;


import picocli.CommandLine;
import picocli.CommandLine.Option;
import subcommands.students.Add;
import subcommands.students.Remove;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import static database.access.Exception.*;

@CommandLine.Command(name = "student", mixinStandardHelpOptions = true, version = "student 0.1",
        description = "Contains the functionality for displaying and managing student.", subcommands = {Add.class, Remove.class})
public class Student implements Callable<Integer> {
    @Option(names = {"-l", "--list"}, description = "List all students.")
    boolean list;
    @Option(names = {"-e", "--entry"}, description = "Entry number of a student.", defaultValue = "")
    String entryNumber;
    @Option(names = {"-d", "--department"}, description = "Department code of a student.", defaultValue = "")
    String departmentCode;

    @Override
    public Integer call() {
        try {
            if (list) {
                ArrayList<models.Student> students = models.Student.retrieveAll();
                ArrayList<models.Student> filteredStudents = new ArrayList<>();
                if (!entryNumber.equals("")) {
                    for (models.Student student : students) {
                        if (student.getEntryNumber().equals(entryNumber)) {
                            filteredStudents.add(student);
                        }
                    }
                } else if (!departmentCode.equals("")) {
                    for (models.Student student : students) {
                        if (student.getDepartmentCode().equals(departmentCode)) {
                            filteredStudents.add(student);
                        }
                    }
                } else {
                    filteredStudents = students;
                }
                if (filteredStudents.size() == 0) {
                    System.out.println("No Such entries found.");
                    return SUCCESS;
                }

                System.out.println("Students:");
                // Entry Number, Name, E-mail, Phone, Department, Entry Year, Address, program, CGPA, Credit Limits, Advisor
                int columnWidth1 = Math.max("Entry Number".length() + 2, filteredStudents.stream().mapToInt(i -> i.getEntryNumber().length() + 2).max().orElse("Entry Number".length() + 2));
                int columnWidth2 = Math.max("Name".length() + 2, filteredStudents.stream().mapToInt(i -> i.getName().length() + 2).max().orElse("Name".length() + 2));
                int columnWidth3 = Math.max("E-mail".length() + 2, filteredStudents.stream().mapToInt(i -> i.getEmail().length() + 2).max().orElse("E-mail".length() + 2));
                int columnWidth4 = Math.max("Phone".length() + 2, filteredStudents.stream().mapToInt(i -> i.getPhone().length() + 2).max().orElse("Phone".length() + 2));
                int columnWidth5 = Math.max("Department".length() + 2, filteredStudents.stream().mapToInt(i -> i.getDepartmentCode().length() + 2).max().orElse("Department".length() + 2));
                int columnWidth6 = Math.max("Entry Year".length() + 2, filteredStudents.stream().mapToInt(i -> String.valueOf(i.getEntryYear()).length() + 2).max().orElse("Entry Year".length() + 2));
                int columnWidth7 = Math.max("Address".length() + 2, filteredStudents.stream().mapToInt(i -> i.getAddress().length() + 2).max().orElse("Address".length() + 2));
                int columnWidth8 = Math.max("Program".length() + 2, filteredStudents.stream().mapToInt(i -> i.getProgram().length() + 2).max().orElse("Program".length() + 2));
                int columnWidth9 = Math.max("CGPA".length() + 2, filteredStudents.stream().mapToInt(i -> String.valueOf(i.getCgpa()).length() + 2).max().orElse("CGPA".length() + 2));
                int columnWidth10 = Math.max("Credit Limits".length() + 2, filteredStudents.stream().mapToInt(i -> String.valueOf(i.getCreditsLimit()).length() + 2).max().orElse("Credit Limits".length() + 2));
                int columnWidth11 = Math.max("Advisor".length() + 2, filteredStudents.stream().mapToInt(i -> i.getAdvisor().length() + 2).max().orElse("Advisor".length() + 2));
                System.out.printf("%-" + columnWidth1 + "s", "Entry Number");
                System.out.printf("%-" + columnWidth2 + "s", "Name");
                System.out.printf("%-" + columnWidth3 + "s", "E-mail");
                System.out.printf("%-" + columnWidth4 + "s", "Phone");
                System.out.printf("%-" + columnWidth5 + "s", "Department");
                System.out.printf("%-" + columnWidth6 + "s", "Entry Year");
                System.out.printf("%-" + columnWidth7 + "s", "Address");
                System.out.printf("%-" + columnWidth8 + "s", "Program");
                System.out.printf("%-" + columnWidth9 + "s", "CGPA");
                System.out.printf("%-" + columnWidth10 + "s", "Credit Limits");
                System.out.printf("%-" + columnWidth11 + "s", "Advisor");
                System.out.println();
                for (models.Student student : filteredStudents) {
                    System.out.printf("%-" + columnWidth1 + "s", student.getEntryNumber());
                    System.out.printf("%-" + columnWidth2 + "s", student.getName());
                    System.out.printf("%-" + columnWidth3 + "s", student.getEmail());
                    System.out.printf("%-" + columnWidth4 + "s", student.getPhone());
                    System.out.printf("%-" + columnWidth5 + "s", student.getDepartmentCode());
                    System.out.printf("%-" + columnWidth6 + "s", student.getEntryYear());
                    System.out.printf("%-" + columnWidth7 + "s", student.getAddress());
                    System.out.printf("%-" + columnWidth8 + "s", student.getProgram());
                    System.out.printf("%-" + columnWidth9 + "s", student.getCgpa());
                    System.out.printf("%-" + columnWidth10 + "s", student.getCreditsLimit());
                    System.out.printf("%-" + columnWidth11 + "s", student.getAdvisor());
                    System.out.println();
                }
                return SUCCESS;
            }
            return SUCCESS;
        } catch (SQLException e) {
            System.out.println("Failed to fetch Student.");
            return handleSQLException(e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            return UNKNOWN;
        }
    }
}
