package models;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
class PrerequisiteTest {
    static Prerequisite prerequisite;
    static Prerequisite retrievedPrerequisite;
    static Instructor instructor1;
    static Department department;
    static Catalog catalog;
    static Offering offering;
    static Offering offering2;
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
        offering2 = new Offering();
        offering2.setCode("CS101");
        offering2.setSemester("I");
        offering2.setYear(2020);
        offering2.setSection(2);
        offering2.setStatus("Proposed");
        offering2.setDepartment("CS");
        offering2.setCoordinator("A");
        offering2.setMinCGPA(2.0f);
        offering2.save();
        prerequisite = new Prerequisite();
        prerequisite.setId(offering.getId());
        prerequisite.setPrerequisiteCode("CS101");
        prerequisite.setMinGrade("A");
    }

    @Test
    @Order(1)
    void getProperties() {
        assertNotNull(prerequisite.getProperties());
    }

    @Test
    @Order(2)
    void getId() {
        assertEquals(offering.getId(), prerequisite.getId());
    }

    @Test
    @Order(3)
    void setId() {
        prerequisite.setId(1);
        assertEquals(1, prerequisite.getId());
        prerequisite.setId(offering.getId());
    }

    @Test
    @Order(4)
    void getPrerequisiteCode() {
        assertEquals("CS101", prerequisite.getPrerequisiteCode());
    }

    @Test
    @Order(5)
    void setPrerequisiteCode() {
        prerequisite.setPrerequisiteCode("CS102");
        assertEquals("CS102", prerequisite.getPrerequisiteCode());
        prerequisite.setPrerequisiteCode("CS101");
    }

    @Test
    @Order(6)
    void getMinGrade() {
        assertEquals("A", prerequisite.getMinGrade());
    }

    @Test
    @Order(7)
    void setMinGrade() {
        prerequisite.setMinGrade("B");
        assertEquals("B", prerequisite.getMinGrade());
        prerequisite.setMinGrade("A");
    }

    @Test
    @Order(8)
    void getIsSaved() {
        assertFalse(prerequisite.getIsSaved());
    }

    @Test
    @Order(9)
    void setIsSaved() {
        prerequisite.setIsSaved(true);
        assertTrue(prerequisite.getIsSaved());
        prerequisite.setIsSaved(false);
    }

    @Test
    @Order(10)
    void save() {
        assertEquals("00000", prerequisite.save());
        prerequisite.setMinGrade("C");
        assertEquals("00000", prerequisite.save());
        prerequisite.setMinGrade("A");
        assertEquals("00000", prerequisite.save());
    }

    @Test
    @Order(11)
    void retrieve() throws Exception {
        retrievedPrerequisite = Prerequisite.retrieve(offering.getId(), "CS101");
        assertEquals(prerequisite.getId(), retrievedPrerequisite.getId());
        assertEquals(prerequisite.getPrerequisiteCode(), retrievedPrerequisite.getPrerequisiteCode());
        assertEquals(prerequisite.getMinGrade(), retrievedPrerequisite.getMinGrade());
        retrievedPrerequisite.setIsSaved(false);
        retrievedPrerequisite.setId(offering2.getId());
        assertEquals("00000", retrievedPrerequisite.save());
    }

    @Test
    @Order(12)
    void retrieveAll() throws Exception {
        ArrayList<Prerequisite> prerequisites = Prerequisite.retrieveAll();
        ArrayList<Integer> ids = new ArrayList<>();
        Assertions.assertNotNull(prerequisites);
        for (Prerequisite prerequisite : prerequisites) {
            ids.add(prerequisite.getId());
        }
        assertTrue(ids.contains(offering.getId()));
        assertTrue(ids.contains(offering2.getId()));
    }

    @Test
    @Order(13)
    void delete() {
        assertEquals("00000", prerequisite.delete());
        assertEquals("00000", retrievedPrerequisite.delete());
        assertEquals("00000", offering.delete());
        assertEquals("00000", offering2.delete());
        assertEquals("00000", academicCalender.delete());
        assertEquals("00000", academicCalender2.delete());
        assertEquals("00000", catalog.delete());
        assertEquals("00000", instructor1.delete());
        assertEquals("00000", department.delete());
    }
}