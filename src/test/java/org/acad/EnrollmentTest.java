package org.acad;

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
class EnrollmentTest {
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
    }
    @AfterAll
    static void tearDown() throws Exception {
        int exitCode = new CommandLine(new Configuration()).execute("-u","postgres", "-w", "1234");
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
    @Order(1)
    void addEnrollmentTest() {
        int exitCode = new CommandLine(new Enrollment()).execute("-l");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Configuration()).execute("-u","2020CSB5000", "-w", "1234");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Enrollment()).execute("-e", "2020CSB5000", "-i", String.valueOf(offering.getId()));
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Enrollment()).execute("-e", "2020CSB4999", "-i", String.valueOf(offering.getId()));
        assertEquals(NOT_EXISTS, exitCode);
        exitCode = new CommandLine(new Configuration()).execute("-u","2020CSB4999");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Enrollment()).execute("-e", "2020CSB4999", "-i", String.valueOf(offering.getId()));
        assertEquals(SUCCESS, exitCode);
    }

    @Test
    @Order(2)
    void listTest() {
        int exitCode = new CommandLine(new Enrollment()).execute("-l");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Enrollment()).execute("-l", "-i", String.valueOf(offering.getId()));
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Calender()).execute("-l","-s","2020-I");
        exitCode = new CommandLine(new Enrollment()).execute("-l", "-i", "0","-e", "2020CSB5001");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Configuration()).execute("-u","postgres", "-w", "1234");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Enrollment()).execute("-l", "-i","0","-e", "2020CSB5000");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Enrollment()).execute("-l", "-i",String.valueOf(offering.getId()),"-e", "2020CSB5001");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Enrollment()).execute("-l");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Enrollment()).execute();
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Enrollment()).execute("-i","0");
        assertEquals(NOT_EXISTS, exitCode);
    }
    @Order(3)
    @Test
    void dropTest() {
        int exitCode = new CommandLine(new Enrollment()).execute("-d", "-i","0","-e", "2020CSB5000");
        assertEquals(NOT_EXISTS, exitCode);
        exitCode = new CommandLine(new Enrollment()).execute("-d", "-i",String.valueOf(offering.getId()),"-e", "2020CSB5000");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Configuration()).execute("-u","2020CSB4999","-w","1234");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Enrollment()).execute("-d", "-i",String.valueOf(offering.getId()),"-e", "2020CSB4999");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Enrollment()).execute("-d", "-i",String.valueOf(offering.getId()),"-e", "2020CSB5000");
        assertEquals(NOT_EXISTS, exitCode);
        exitCode = new CommandLine(new Configuration()).execute("-u","postgres", "-w", "1234");
        assertEquals(SUCCESS, exitCode);
    }
    @Order(4)
    @Test
    void auditTest() {
        new CommandLine(new Calender()).execute("-l");
        int exitCode = new CommandLine(new Enrollment()).execute("-a", "-i","0","-e", "2020CSB5000");
        assertEquals(NOT_EXISTS, exitCode);
        exitCode = new CommandLine(new Enrollment()).execute("-a", "-i",String.valueOf(offering.getId()),"-e", "2020CSB5000");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Configuration()).execute("-u","2020CSB4999","-w","1234");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Enrollment()).execute("-a", "-i",String.valueOf(offering.getId()),"-e", "2020CSB4999");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Enrollment()).execute("-a", "-i",String.valueOf(offering.getId()),"-e", "2020CSB5000");
        assertEquals(NOT_EXISTS, exitCode);
        exitCode = new CommandLine(new Configuration()).execute("-u","postgres", "-w", "1234");
        assertEquals(SUCCESS, exitCode);
    }
    @Order(5)
    @Test
    void withdrawlTest() {
        new CommandLine(new Calender()).execute("-l");
        int exitCode = new CommandLine(new Enrollment()).execute("-w", "-i","0","-e", "2020CSB5000");
        assertEquals(NOT_EXISTS, exitCode);
        exitCode = new CommandLine(new Enrollment()).execute("-w", "-i",String.valueOf(offering.getId()),"-e", "2020CSB5000");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Configuration()).execute("-u","2020CSB4999","-w","1234");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Enrollment()).execute("-w", "-i",String.valueOf(offering.getId()),"-e", "2020CSB4999");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Enrollment()).execute("-w", "-i",String.valueOf(offering.getId()),"-e", "2020CSB5000");
        assertEquals(NOT_EXISTS, exitCode);
        exitCode = new CommandLine(new Configuration()).execute("-u","postgres", "-w", "1234");
        assertEquals(SUCCESS, exitCode);
    }
    @Order(6)
    @Test
    void importTest() throws IOException {
        int exitCode = new CommandLine(new Enrollment()).execute("-D", "-i",String.valueOf(offering.getId()));
        assertEquals(SUCCESS, exitCode);
        Files.move(Paths.get("./enrollment.csv"), Paths.get("./src/test/resources/enrollment.csv"), StandardCopyOption.REPLACE_EXISTING);
        exitCode = new CommandLine(new Enrollment()).execute("-D", "-i","0");
        assertEquals(NOT_EXISTS, exitCode);
    }
    @Order(7)
    @Test
    void exportTest() {
        int exitCode = new CommandLine(new Configuration()).execute("-u","2020CSB4999","-w","1234");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Enrollment()).execute("-u","./src/test/resources/enrollment.csv","-i",String.valueOf(offering.getId()));
        assertEquals(UNAUTHORISED, exitCode);
        exitCode = new CommandLine(new Configuration()).execute("-u","postgres", "-w", "1234");
        assertEquals(SUCCESS, exitCode);
        new CommandLine(new Enrollment()).execute("-l");
        exitCode = new CommandLine(new Configuration()).execute("-u","email","-w","iitrpr");
        assertEquals(SUCCESS, exitCode);
        exitCode = new CommandLine(new Enrollment()).execute("-u","./src/test/resources/enrollment.csv","-i",String.valueOf(offering.getId()));
        assertEquals(SUCCESS, exitCode);
        new CommandLine(new Enrollment()).execute("-l");
    }
}