package models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(OrderAnnotation.class)
class EnrollmentTest {
    static Enrollment enrollment;
    static Enrollment retrievedEnrollment;
    static Student student;
    static Instructor instructor1;
    static Department department;
    static Catalog catalog;
    static Catalog catalog1;
    static Offering offering;
    static Offering offering2;
    static AcademicCalender academicCalender;
    static AcademicCalender academicCalender2;
    static AcademicCalender academicCalender3;

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
        catalog1 = new Catalog();
        catalog1.setCode("CS102");
        catalog1.setName("Intro to Computer Science");
        catalog1.setCredits(3);
        catalog1.save();
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
        academicCalender3 = new AcademicCalender();
        academicCalender3.setEvent("Grade Submission");
        academicCalender3.setSemester("I");
        academicCalender3.setYear(2020);
        academicCalender3.setStartDate(String.valueOf(java.time.LocalDate.now()));
        academicCalender3.setEndDate(String.valueOf(java.time.LocalDate.now()));
        academicCalender3.save();
        offering = new Offering();
        offering.setCode("CS101");
        offering.setSemester("I");
        offering.setYear(2020);
        offering.setSection(1);
        offering.setStatus("Enrolling");
        offering.setDepartment("CS");
        offering.setCoordinator("A");
        offering.setMinCGPA(0.0f);
        offering.save();
        offering2 = new Offering();
        offering2.setCode("CS102");
        offering2.setSemester("I");
        offering2.setYear(2020);
        offering2.setSection(1);
        offering2.setStatus("Enrolling");
        offering2.setDepartment("CS");
        offering2.setCoordinator("A");
        offering2.setMinCGPA(0.0f);
        offering2.save();
        student = new Student();
        student.setEntryNumber("2020CSB1123");
        student.setName("Shahnawaz Khan");
        student.setEmail("shahnawazkhan5172@gmail.com");
        student.setPhone("1234567890");
        student.setDepartmentCode("CS");
        student.setEntryYear(2020);
        student.setAddress("Muzaffarpur");
        student.setProgram("B.Tech");
        student.setCgpa(0);
        student.setCreditsLimit(24);
        student.setAdvisor("A");
        student.save();
        enrollment = new Enrollment();
        enrollment.setEntryNo("2020CSB1123");
        enrollment.setId(offering.getId());
        enrollment.setGrade("NA");
        enrollment.setStatus("Enrolled");
        enrollment.setCourseType("PC");
    }

    @Test
    @Order(1)
    void getId() {
        assertEquals(offering.getId(), enrollment.getId());
    }

    @Test
    @Order(2)
    void setId() {
        enrollment.setId(2);
        assertEquals(2, enrollment.getId());
        enrollment.setId(offering.getId());
    }

    @Test
    @Order(3)
    void getEntryNo() {
        assertEquals("2020CSB1123", enrollment.getEntryNo());
    }

    @Test
    @Order(4)
    void setEntryNo() {
        enrollment.setEntryNo("2020CSB1124");
        assertEquals("2020CSB1124", enrollment.getEntryNo());
        enrollment.setEntryNo("2020CSB1123");
    }

    @Test
    @Order(5)
    void getGrade() {
        assertEquals("NA", enrollment.getGrade());
    }

    @Test
    @Order(6)
    void setGrade() {
        enrollment.setGrade("A");
        assertEquals("A", enrollment.getGrade());
        enrollment.setGrade("NA");
    }

    @Test
    @Order(7)
    void getStatus() {
        assertEquals("Enrolled", enrollment.getStatus());
    }

    @Test
    @Order(8)
    void setStatus() {
        enrollment.setStatus("Dropped");
        assertEquals("Dropped", enrollment.getStatus());
        enrollment.setStatus("Enrolled");
    }

    @Test
    @Order(9)
    void getCourseType() {
        assertEquals("PC", enrollment.getCourseType());
    }

    @Test
    @Order(10)
    void setCourseType() {
        enrollment.setCourseType("PE");
        assertEquals("PE", enrollment.getCourseType());
        enrollment.setCourseType("PC");
    }

    @Test
    @Order(11)
    void getIsSaved() {
        assertFalse(enrollment.getIsSaved());
    }

    @Test
    @Order(12)
    void setIsSaved() {
        enrollment.setIsSaved(true);
        assertTrue(enrollment.getIsSaved());
        enrollment.setIsSaved(false);
    }

    @Test
    @Order(13)
    void save() {
        assertEquals("00000", enrollment.save());
        enrollment.setGrade("NF");
        enrollment.save();
        assertEquals("00000", enrollment.save());
        enrollment.setGrade("NA");
        assertEquals("00000", enrollment.save());
    }

    @Test
    @Order(14)
    void retrieve() throws Exception {
        retrievedEnrollment = Enrollment.retrieve(enrollment.getId(), enrollment.getEntryNo());
        assertEquals(enrollment.getId(), retrievedEnrollment.getId());
        assertEquals(enrollment.getEntryNo(), retrievedEnrollment.getEntryNo());
        assertEquals(enrollment.getGrade(), retrievedEnrollment.getGrade());
        assertEquals(enrollment.getStatus(), retrievedEnrollment.getStatus());
        assertEquals(enrollment.getCourseType(), retrievedEnrollment.getCourseType());
        retrievedEnrollment.setIsSaved(false);
        retrievedEnrollment.setId(offering2.getId());
        retrievedEnrollment.save();
    }

    @Test
    @Order(15)
    void retrieveAll() throws Exception {
        ArrayList<Enrollment> enrollments = Enrollment.retrieveAll();
        ArrayList<Integer> ids = new ArrayList<>();
        assertNotNull(enrollments);
        for (Enrollment enrollment : enrollments) {
            ids.add(enrollment.getId());
        }
        assertTrue(ids.contains(enrollment.getId()));
        assertTrue(ids.contains(retrievedEnrollment.getId()));
    }

    @Test
    @Order(16)
    void delete() {
        assertEquals("00000", enrollment.delete());
        assertEquals("00000", retrievedEnrollment.delete());
        assertEquals("00000", offering.delete());
        assertEquals("00000", offering2.delete());
        assertEquals("00000", catalog.delete());
        assertEquals("00000", catalog1.delete());
        assertEquals("00000", academicCalender.delete());
        assertEquals("00000", academicCalender2.delete());
        assertEquals("00000", academicCalender3.delete());
        assertEquals("00000", student.delete());
        assertEquals("00000", instructor1.delete());
        assertEquals("00000", department.delete());
    }

}