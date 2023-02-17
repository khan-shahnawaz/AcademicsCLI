package models;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(OrderAnnotation.class)
class TeachingTeamTest {
    static TeachingTeam teachingTeam;
    static TeachingTeam retrievedTeachingTeam;
    static Instructor instructor1;
    static Instructor instructor2;
    static Department department;
    static Catalog catalog;
    static Offering offering;
    static AcademicCalender academicCalender;
    static AcademicCalender academicCalender2;

    @BeforeAll
    static void init() {
        department = new Department();
        department.setId("CS");
        department.setName("Computer Science");
        department.save();
        instructor1 = new Instructor();
        instructor1.setName("A");
        instructor1.setEmail("A");
        instructor1.setPhone("1234567890");
        instructor1.setDepartmentCode("CS");
        instructor1.setAddress("Ropar");
        instructor1.save();
        instructor2 = new Instructor();
        instructor2.setName("B");
        instructor2.setEmail("B");
        instructor2.setPhone("1234567890");
        instructor2.setDepartmentCode("CS");
        instructor2.setAddress("Ropar");
        instructor2.save();
        catalog = new Catalog();
        catalog.setCode("CS101");
        catalog.setName("Intro to Computer Science");
        catalog.setCredits(3);
        catalog.save();
        academicCalender = new AcademicCalender();
        academicCalender.setSemester("I");
        academicCalender.setYear(2020);
        academicCalender.setStartDate(String.valueOf(java.time.LocalDate.now()));
        academicCalender.setEndDate(String.valueOf(java.time.LocalDate.now()));
        academicCalender.setEvent("Course Add/Drop");
        academicCalender.save();
        academicCalender2 = new AcademicCalender();
        academicCalender2.setEvent("Academic Session");
        academicCalender2.setSemester("I");
        academicCalender2.setYear(2020);
        academicCalender2.setStartDate(String.valueOf(java.time.LocalDate.now()));
        academicCalender2.setEndDate(String.valueOf(java.time.LocalDate.now()));
        academicCalender2.save();
        offering = new Offering();
        offering.setCode("CS101");
        offering.setSemester("I");
        offering.setYear(2020);
        offering.setSection(1);
        offering.setStatus("Proposed");
        offering.setDepartment("CS");
        offering.setCoordinator("A");
        offering.setMinCGPA(2.0f);
        offering.save();
        teachingTeam = new TeachingTeam();
        teachingTeam.setId(offering.getId());
        teachingTeam.setInstructor("A");
        teachingTeam.setCoordinator(true);
    }

    @Order(1)
    @Test
    void getId() {
        assertEquals(offering.getId(), teachingTeam.getId());
    }

    @Order(2)
    @Test
    void setId() {
        int newId = 2;
        teachingTeam.setId(newId);
        assertEquals(newId, teachingTeam.getId());
        teachingTeam.setId(offering.getId());
    }

    @Order(3)
    @Test
    void getInstructor() {
        assertEquals("A", teachingTeam.getInstructor());
    }

    @Order(4)
    @Test
    void setInstructor() {
        String newInstructor = "B";
        teachingTeam.setInstructor(newInstructor);
        assertEquals(newInstructor, teachingTeam.getInstructor());
        teachingTeam.setInstructor("A");
    }

    @Order(5)
    @Test
    void getCoordinator() {
        assertTrue(teachingTeam.isCoordinator());
    }

    @Order(6)
    @Test
    void setCoordinator() {
        teachingTeam.setCoordinator(false);
        assertFalse(teachingTeam.isCoordinator());
        teachingTeam.setCoordinator(true);
    }

    @Order(7)
    @Test
    void getIsSaved() {
        assertFalse(teachingTeam.getIsSaved());
    }

    @Order(8)
    @Test
    void setIsSaved() {
        teachingTeam.setIsSaved(true);
        assertTrue(teachingTeam.getIsSaved());
        teachingTeam.setIsSaved(false);
    }

    @Order(9)
    @Test
    void save() {
        assertEquals("00000", teachingTeam.save());
        teachingTeam.setInstructor("B");
        assertEquals("00000", teachingTeam.save());
        teachingTeam.setInstructor("A");
        assertEquals("00000", teachingTeam.save());
    }

    @Order(10)
    @Test
    void retrieve() {
        retrievedTeachingTeam = TeachingTeam.retrieve(teachingTeam.getId(), teachingTeam.getInstructor());
        assertEquals(teachingTeam.getId(), retrievedTeachingTeam.getId());
        assertEquals(teachingTeam.getInstructor(), retrievedTeachingTeam.getInstructor());
        assertEquals(teachingTeam.isCoordinator(), retrievedTeachingTeam.isCoordinator());
        retrievedTeachingTeam.setInstructor("B");
        retrievedTeachingTeam.setCoordinator(false);
        retrievedTeachingTeam.setIsSaved(false);
        retrievedTeachingTeam.save();
    }

    @Order(11)
    @Test
    void retrieveAll() {
        ArrayList<TeachingTeam> teachingTeams = TeachingTeam.retrieveAll();
        Assertions.assertNotNull(teachingTeams);
        ArrayList<String> instructors = new ArrayList<>();
        for (TeachingTeam t : teachingTeams) {
            instructors.add(t.getInstructor());
        }
        Assertions.assertTrue(instructors.contains("A"));
        Assertions.assertTrue(instructors.contains("B"));
    }

    @Order(12)
    @Test
    void delete() {
        Assertions.assertEquals("00000", teachingTeam.delete());
        Assertions.assertEquals("00000", retrievedTeachingTeam.delete());
        Assertions.assertEquals("00000", offering.delete());
        Assertions.assertEquals("00000", catalog.delete());
        Assertions.assertEquals("00000", instructor1.delete());
        Assertions.assertEquals("00000", instructor2.delete());
        Assertions.assertEquals("00000", department.delete());
        Assertions.assertEquals("00000", academicCalender.delete());
        Assertions.assertEquals("00000", academicCalender2.delete());
    }
}