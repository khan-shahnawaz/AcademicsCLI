package org.acad;

import static org.junit.jupiter.api.Assertions.*;
import database.access.DBConnectionSingleton;
import models.BaseModel;
import org.junit.jupiter.api.*;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import static database.access.Exception.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OfferingTest {
    static models.Offering offering;
    @BeforeAll
    static void setUp() throws Exception {
        int exitCode = new CommandLine(new Department()).execute("add", "-n", "Computer Science", "-c", "CS");
        assertEquals(0, exitCode);
        exitCode = new CommandLine(new Instructor()).execute("add", "-n", "John Doe", "-d", "CS", "-a", "India", "-p", "1234567890", "-e", "email","-w","iitrpr");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Instructor()).execute("add", "-n", "John Doe", "-d", "CS", "-a", "India", "-p", "1234567890", "-e", "email2","-w","iitrpr");
        exitCode = new CommandLine(new Student()).execute("add", "-n", "John Doe", "-d", "CS", "-a", "India", "-p", "1234567890", "-E", "email",
                "-c", "18", "-e", "2020CSB5000", "-P", "B.Tech", "-A", "email", "-y", "2020", "-w", "1234");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Student()).execute("add", "-n", "John Doe", "-d", "CS", "-a", "India", "-p", "1234567890", "-E", "email",
                "-c", "18", "-e", "2020CSB4999", "-P", "B.Tech", "-A", "email", "-y", "2020", "-w", "1234");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("add", "-C", "CS101", "-n", "Data Structures", "-d", "des", "-c", "1", "-s", "1-1-1-1-1");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("add", "-C", "CS102", "-n", "Data Structures", "-d", "des", "-c", "1", "-s", "1-1-1-1-1");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("prereq","CS101","CS102");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Calender()).execute("add", "-e","Academic Session", "-s", "2020-I", "-S", "2021-05-31", "-E", "9999-06-30");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Calender()).execute("add", "-e","Course Add/Drop", "-s", "2020-I", "-S", "2021-05-31", "-E", "9999-06-30");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Calender()).execute("add", "-e","Course Withdrawal/Audit", "-s", "2020-I", "-S", "2021-05-31", "-E", "9999-06-30");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Calender()).execute("add", "-e","Grade Submission", "-s", "2020-I", "-S", "2021-05-31", "-E", "9999-06-30");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Offering()).execute("add","-c","CS101","-s","2020-I","-S","1","-g","0");
        assertEquals(UNAUTHORISED, exitCode);
        exitCode = new CommandLine(new Configuration()).execute("-u","email", "-w", "iitrpr");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Offering()).execute("add","-c","CS101","-s","2020-I","-S","1","-g","0");
        assertEquals(SUCCESS, exitCode);
        offering = models.Offering.retrieve("CS101","I",2020,1);
        exitCode = new CommandLine(new Offering()).execute("update","-i",String.valueOf(offering.getId()),"-s","Enrolling");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Offering()).execute("category","-i",String.valueOf(offering.getId()),"-c","PC","-d","CS","-p","B.Tech","-y","2020");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Offering()).execute("prereq",String.valueOf(offering.getId()),"CS101","NA");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Offering()).execute("instructor",String.valueOf(offering.getId()),"email2");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Offering()).execute("update","-i",String.valueOf(offering.getId()),"-g","0.1");
        assertEquals(SUCCESS, exitCode);
    }
    @AfterAll
    static void tearDown() throws Exception {
        int exitCode = new CommandLine(new Configuration()).execute("-u","postgres", "-w", "1234");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Offering()).execute("prereq","-r",String.valueOf(offering.getId()),"CS101", "NA");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Offering()).execute("prereq","-r",String.valueOf(offering.getId()),"CS102", "E");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Offering()).execute("instructor","-r", String.valueOf(offering.getId()), "email2");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Offering()).execute("category","-r","-i",String.valueOf(offering.getId()),"-c","PC","-d","CS","-p","B.Tech","-y","2020");
        assertEquals(SUCCESS, exitCode);
        ArrayList<models.Enrollment> enrollments = models.Enrollment.retrieveAll();
        for (models.Enrollment enrollment : enrollments) {
            assertEquals("00000", enrollment.delete());
        }
        models.Offering offering = models.Offering.retrieve("CS101","I",2020,1);
        assertNotNull(offering);
        models.TeachingTeam instruc = models.TeachingTeam.retrieve(offering.getId(), "email");
        instruc.delete();
        String sqlCode = offering.delete();
        assertEquals("00000",sqlCode);
        exitCode = new CommandLine(new Offering()).execute("-l");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Calender()).execute("remove", "-e","Academic Session", "-s", "2020-I");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Calender()).execute("remove", "-e","Course Add/Drop", "-s", "2020-I");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Calender()).execute("remove", "-e","Course Withdrawal/Audit", "-s", "2020-I");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Calender()).execute("remove", "-e","Grade Submission", "-s", "2020-I");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("prereq", "-r", "CS101", "CS102");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("remove", "CS101");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Catalog()).execute("remove", "CS102");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Student()).execute("remove", "-e", "2020CSB5000");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Student()).execute("remove", "-e", "2020CSB4999");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Instructor()).execute("remove", "-e", "email");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Instructor()).execute("remove", "-e", "email2");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Department()).execute("remove", "-c", "CS");
        assertEquals(SUCCESS, exitCode);


    }
    @Test
    @Order(1)
    void listTets() {
        int exitCode = new CommandLine(new Offering()).execute("-l");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Offering()).execute("-l","-s","2020-I","-d","CS");
        assertEquals(SUCCESS, exitCode);
    }
    @Test
    @Order(2)
    void detailsTest() {
        int exitCode = new CommandLine(new Offering()).execute("-D",String.valueOf(offering.getId()));
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Offering()).execute("-D","0");
        assertEquals(NOT_EXISTS, exitCode);
    }
}