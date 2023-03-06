package org.acad;

import models.Enrollment;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.File;
import java.util.ArrayList;

import static database.access.Exception.SUCCESS;
import static org.junit.jupiter.api.Assertions.*;

class ReportTest {
    static models.Offering offering;
    @BeforeAll
    static void setUp() throws Exception {
        int exitCode = new CommandLine(new Department()).execute("add", "-n", "Computer Science", "-c", "CS");
        assertEquals(0, exitCode);
        exitCode = new CommandLine(new Instructor()).execute("add", "-n", "John Doe", "-d", "CS", "-a", "India", "-p", "1234567890", "-e", "email","-w","iitrpr");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Student()).execute("add", "-n", "John Doe", "-d", "CS", "-a", "India", "-p", "1234567890", "-E", "email",
                "-c", "18", "-e", "2020CSB5000", "-P", "B.Tech", "-A", "email", "-y", "2020", "-w", "1234");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Student()).execute("add", "-n", "John Doe", "-d", "CS", "-a", "India", "-p", "1234567890", "-E", "email",
                "-c", "18", "-e", "2020CSB4999", "-P", "B.Tech", "-A", "email", "-y", "2020", "-w", "1234");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("add", "-C", "CS101", "-n", "Data Structures", "-d", "des", "-c", "1", "-s", "1-1-1-1-1");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("add", "-C", "CS110", "-n", "Data Structures", "-d", "des", "-c", "1", "-s", "1-1-1-1-1");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("add", "-C", "CS111", "-n", "Data Structures", "-d", "des", "-c", "1", "-s", "1-1-1-1-1");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("add", "-C", "CS112", "-n", "Data Structures", "-d", "des", "-c", "1", "-s", "1-1-1-1-1");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("add", "-C", "CS113", "-n", "Data Structures", "-d", "des", "-c", "1", "-s", "1-1-1-1-1");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("add", "-C", "CS114", "-n", "Data Structures", "-d", "des", "-c", "1", "-s", "1-1-1-1-1");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("add", "-C", "CS115", "-n", "Data Structures", "-d", "des", "-c", "1", "-s", "1-1-1-1-1");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("add", "-C", "CS116", "-n", "Data Structures", "-d", "des", "-c", "1", "-s", "1-1-1-1-1");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("add", "-C", "CS117", "-n", "Data Structures", "-d", "des", "-c", "1", "-s", "1-1-1-1-1");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("add", "-C", "CS118", "-n", "Data Structures", "-d", "des", "-c", "1", "-s", "1-1-1-1-1");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Calender()).execute("add", "-e","Academic Session", "-s", "2020-I", "-S", "2021-05-31", "-E", "9999-06-30");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Calender()).execute("add", "-e","Course Add/Drop", "-s", "2020-I", "-S", "2021-05-31", "-E", "9999-06-30");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Calender()).execute("add", "-e","Course Withdrawal/Audit", "-s", "2020-I", "-S", "2021-05-31", "-E", "9999-06-30");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Calender()).execute("add", "-e","Grade Submission", "-s", "2020-I", "-S", "2021-05-31", "-E", "9999-06-30");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Configuration()).execute("-u","email", "-w", "iitrpr");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Offering()).execute("add","-c","CS101","-s","2020-I","-S","1","-g","0");
        assertEquals(SUCCESS, exitCode);

        offering = models.Offering.retrieve("CS101","I",2020,1);
        exitCode = new CommandLine(new Offering()).execute("update","-i",String.valueOf(offering.getId()),"-s","Enrolling");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Offering()).execute("category","-i",String.valueOf(offering.getId()),"-c","PC","-d","CS","-p","B.Tech","-y","2020");
        assertEquals(SUCCESS, exitCode);

        exitCode = new CommandLine(new Configuration()).execute("-u","postgres", "-w", "1234");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new org.acad.Enrollment()).execute("-e", "2020CSB5000", "-i", String.valueOf(offering.getId()));
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new org.acad.Enrollment()).execute("-e", "2020CSB4999", "-i", String.valueOf(offering.getId()));
        assertEquals(SUCCESS, exitCode);
        String[] grade = {"A", "A-", "B", "B-", "C", "C-", "D", "E","F"};
        for (int i=0;i<8;i++) {
            models.Offering offering = new models.Offering();
            offering.setStatus("Enrolling");
            offering.setSemester("I");
            offering.setYear(2020);
            offering.setSection(1);
            offering.setCode("CS11"+i);
            offering.setMinCGPA(0);
            offering.setCoordinator("email");
            offering.setDepartment("CS");
            offering.save();
            models.Enrollment enrollment = new models.Enrollment();
            enrollment.setId(offering.getId());
            enrollment.setEntryNo("2020CSB5000");
            enrollment.setGrade(grade[i]);
            enrollment.setStatus("Enrolled");
            enrollment.setCourseType("OE");
            enrollment.save();
        }
    }
    @AfterAll
    static void tearDown() throws Exception {
        int exitCode = new CommandLine(new Configuration()).execute("-u","postgres", "-w", "1234");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Offering()).execute("category","-r","-i",String.valueOf(offering.getId()),"-c","PC","-d","CS","-p","B.Tech","-y","2020");
        assertEquals(SUCCESS, exitCode);
        ArrayList<Enrollment> enrollments = models.Enrollment.retrieveAll();
        for (models.Enrollment enrollment : enrollments) {
            assertEquals("00000", enrollment.delete());
        }
        models.Offering offering = models.Offering.retrieve("CS101","I",2020,1);
        assertNotNull(offering);
        models.TeachingTeam instruc = models.TeachingTeam.retrieve(offering.getId(), "email");
        instruc.delete();
        String sqlCode = offering.delete();
        for (models.Offering extraOffering : models.Offering.retrieveAll()) {
            extraOffering.delete();
        }
        System.out.println(offering.getCoordinator()+offering.getCode());
        assertEquals("00000",sqlCode);
        exitCode = new CommandLine(new Calender()).execute("remove", "-e","Academic Session", "-s", "2020-I");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Calender()).execute("remove", "-e","Course Add/Drop", "-s", "2020-I");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Calender()).execute("remove", "-e","Course Withdrawal/Audit", "-s", "2020-I");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Calender()).execute("remove", "-e","Grade Submission", "-s", "2020-I");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("remove", "CS101");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("remove", "CS110");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("remove", "CS111");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("remove", "CS112");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("remove", "CS113");
        assertEquals(SUCCESS, exitCode);

        exitCode = new CommandLine(new Catalog()).execute("remove", "CS114");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("remove", "CS115");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("remove", "CS116");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("remove", "CS117");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("remove", "CS118");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Student()).execute("remove", "-e", "2020CSB5000");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Student()).execute("remove", "-e", "2020CSB4999");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Instructor()).execute("remove", "-e", "email");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Department()).execute("remove", "-c", "CS");
        assertEquals(SUCCESS, exitCode);

    }
    @Test
    void generateReport() {
        int exitCode = new CommandLine(new Report()).execute("-F","./src/test/resources/student.csv");
        assertEquals(SUCCESS, exitCode);
        File file = new File("report.csv");
        assertTrue(file.exists());
        file.delete();
        file = new File("./Transcripts");
        assertTrue(file.exists());
        for (File f : file.listFiles()) {
            f.delete();
        }
        file.delete();
    }
}