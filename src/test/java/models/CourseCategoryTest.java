package models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(OrderAnnotation.class)
class CourseCategoryTest {
    static CourseCategory courseCategory;
    static CourseCategory retrievedCourseCategory;
    static Instructor instructor1;
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
        courseCategory = new CourseCategory();
        courseCategory.setId(offering.getId());
        courseCategory.setEntryYear(2020);
        courseCategory.setType("PC");
        courseCategory.setDepartment("CS");
        courseCategory.setProgram("B.Tech");
    }

    @Order(1)
    @Test
    void getId() {
        assertEquals(offering.getId(), courseCategory.getId());
    }

    @Order(2)
    @Test
    void setId() {
        int newId = 2;
        courseCategory.setId(newId);
        assertEquals(newId, courseCategory.getId());
        courseCategory.setId(offering.getId());
    }

    @Order(3)
    @Test
    void getEntryYear() {
        int entryYear = 2020;
        assertEquals(entryYear, courseCategory.getEntryYear());
    }

    @Order(4)
    @Test
    void setEntryYear() {
        int newEntryYear = 2021;
        courseCategory.setEntryYear(newEntryYear);
        assertEquals(newEntryYear, courseCategory.getEntryYear());
        courseCategory.setEntryYear(2020);
    }

    @Order(5)
    @Test
    void getType() {
        String type = "PC";
        assertEquals(type, courseCategory.getType());
    }

    @Order(6)
    @Test
    void setType() {
        String newType = "PE";
        courseCategory.setType(newType);
        assertEquals(newType, courseCategory.getType());
        courseCategory.setType("PC");
    }

    @Order(7)
    @Test
    void getDepartment() {
        String department = "CS";
        assertEquals(department, courseCategory.getDepartment());
    }

    @Order(8)
    @Test
    void setDepartment() {
        String newDepartment = "ME";
        courseCategory.setDepartment(newDepartment);
        assertEquals(newDepartment, courseCategory.getDepartment());
        courseCategory.setDepartment("CS");
    }

    @Order(9)
    @Test
    void getProgram() {
        String program = "B.Tech";
        assertEquals(program, courseCategory.getProgram());
    }

    @Order(10)
    @Test
    void setProgram() {
        String newProgram = "M.Tech";
        courseCategory.setProgram(newProgram);
        assertEquals(newProgram, courseCategory.getProgram());
        courseCategory.setProgram("B.Tech");
    }

    @Order(11)
    @Test
    void getIsSaved() {
        boolean isSaved = false;
        assertEquals(isSaved, courseCategory.getIsSaved());
    }

    @Order(12)
    @Test
    void save() {
        assertEquals("00000", courseCategory.save());
        courseCategory.setProgram("M.Tech");
        assertEquals("00000", courseCategory.save());
        courseCategory.setProgram("B.Tech");
        assertEquals("00000", courseCategory.save());
    }

    @Order(13)
    @Test
    void retrieve() throws Exception {
        retrievedCourseCategory = CourseCategory.retrieve(courseCategory.getId(),
                courseCategory.getType(),
                courseCategory.getEntryYear(),
                courseCategory.getDepartment(),
                courseCategory.getProgram());
        assertEquals(courseCategory.getId(), retrievedCourseCategory.getId());
        assertEquals(courseCategory.getEntryYear(), retrievedCourseCategory.getEntryYear());
        assertEquals(courseCategory.getType(), retrievedCourseCategory.getType());
        assertEquals(courseCategory.getDepartment(), retrievedCourseCategory.getDepartment());
        assertEquals(courseCategory.getProgram(), retrievedCourseCategory.getProgram());
        retrievedCourseCategory.setIsSaved(false);
        retrievedCourseCategory.setProgram("M.Tech");
        retrievedCourseCategory.save();
    }

    @Order(14)
    @Test
    void retrieveAll() throws Exception {
        ArrayList<CourseCategory> courseCategories = CourseCategory.retrieveAll();
        ArrayList<String> programs = new ArrayList<>();
        assertNotNull(courseCategories);
        for (CourseCategory courseCategory : courseCategories) {
            programs.add(courseCategory.getProgram());
        }
        assertTrue(programs.contains("B.Tech"));
        assertTrue(programs.contains("M.Tech"));
    }

    @Order(15)
    @Test
    void delete() {
        assertEquals("00000", courseCategory.delete());
        assertEquals("00000", retrievedCourseCategory.delete());
        assertEquals("00000", offering.delete());
        assertEquals("00000", catalog.delete());
        assertEquals("00000", instructor1.delete());
        assertEquals("00000", department.delete());
        assertEquals("00000", academicCalender.delete());
        assertEquals("00000", academicCalender2.delete());
    }
}