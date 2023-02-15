package Models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
class OfferingTest {
    static Offering offering;
    static Offering retrievedoffering;
    static Catalog catalog;
    static Instructor instructor;
    static Department department;
    static AcademicCalender academicCalender;

    @BeforeAll
    static void init() {
        catalog = new Catalog();
        catalog.setCode("CS100");
        catalog.setName("Intro to Computer Science");
        catalog.setCredits(3);
        catalog.save();
        department = new Department();
        department.setId("CS");
        department.setName("Computer Science");
        assertTrue(department.save());
        instructor = new Instructor();
        instructor.setEmail("a@b.com");
        instructor.setName("A B");
        instructor.setDepartmentCode("CS");
        instructor.setPhone("1234567890");
        instructor.setAddress("Ropar");
        assertTrue(instructor.save());
        offering = new Offering();
        offering.setCode("CS100");
        offering.setSemester("I");
        offering.setYear(2020);
        offering.setSection(1);
        offering.setStatus("Proposed");
        offering.setDepartment("CS");
        offering.setCoordinator("a@b.com");
        offering.setMinCGPA(0);
        academicCalender = new AcademicCalender();
        academicCalender.setSemester("I");
        academicCalender.setYear(2020);
        academicCalender.setStartDate(String.valueOf(java.time.LocalDate.now()));
        academicCalender.setEndDate(String.valueOf(java.time.LocalDate.now()));
        academicCalender.setEvent("Course Add/Drop");
        academicCalender.save();
    }

    @Test
    @Order(1)
    void getCode() {
        String code = "CS100";
        assertEquals(code, offering.getCode());
    }

    @Order(2)
    @Test
    void setCode() {
        String newCode = "CS101";
        offering.setCode(newCode);
        assertEquals(newCode, offering.getCode());
        offering.setCode("CS100");
    }

    @Order(3)
    @Test
    void getSemester() {
        String semester = "I";
        assertEquals(semester, offering.getSemester());
    }

    @Order(4)
    @Test
    void setSemester() {
        String newSemester = "II";
        offering.setSemester(newSemester);
        assertEquals(newSemester, offering.getSemester());
        offering.setSemester("I");
    }

    @Order(5)
    @Test
    void getYear() {
        int year = 2020;
        assertEquals(year, offering.getYear());
    }

    @Order(6)
    @Test
    void setYear() {
        int newYear = 2021;
        offering.setYear(newYear);
        assertEquals(newYear, offering.getYear());
        offering.setYear(2020);
    }

    @Order(7)
    @Test
    void getSection() {
        int section = 1;
        assertEquals(section, offering.getSection());
    }

    @Order(8)
    @Test
    void setSection() {
        int newSection = 2;
        offering.setSection(newSection);
        assertEquals(newSection, offering.getSection());
        offering.setSection(1);
    }

    @Order(9)
    @Test
    void getStatus() {
        String status = "Proposed";
        assertEquals(status, offering.getStatus());
    }

    @Order(10)
    @Test
    void setStatus() {
        String newStatus = "Enrolling";
        offering.setStatus(newStatus);
        assertEquals(newStatus, offering.getStatus());
        offering.setStatus("Proposed");
    }

    @Order(11)
    @Test
    void getDepartment() {
        String department = "CS";
        assertEquals(department, offering.getDepartment());
    }

    @Order(12)
    @Test
    void setDepartment() {
        String newDepartment = "EE";
        offering.setDepartment(newDepartment);
        assertEquals(newDepartment, offering.getDepartment());
        offering.setDepartment("CS");
    }

    @Order(13)
    @Test
    void getCoordinator() {
        String coordinator = "a@b.com";
        assertEquals(coordinator, offering.getCoordinator());
    }

    @Order(14)
    @Test
    void setCoordinator() {
        String newCoordinator = "a@bc.com";
        offering.setCoordinator(newCoordinator);
        assertEquals(newCoordinator, offering.getCoordinator());
        offering.setCoordinator("a@b.com");
    }

    @Order(15)
    @Test
    void getMinCGPA() {
        float minCGPA = 0;
        assertEquals(minCGPA, offering.getMinCGPA());
    }

    @Order(16)
    @Test
    void setMinCGPA() {
        float newMinCGPA = 1;
        offering.setMinCGPA(newMinCGPA);
        assertEquals(newMinCGPA, offering.getMinCGPA());
        offering.setMinCGPA(0);
    }

    @Order(17)
    @Test
    void getIsSaved() {
        assertFalse(offering.getIsSaved());
    }

    @Order(18)
    @Test
    void setIsSaved() {
        offering.setIsSaved(true);
        assertTrue(offering.getIsSaved());
        offering.setIsSaved(false);
    }

    @Order(19)
    @Test
    void save() {
        assertTrue(offering.save());
        offering.setStatus("Enrolling");
        assertTrue(offering.save());
        offering.setStatus("Proposed");
        assertTrue(offering.save());
    }

    @Order(20)
    @Test
    void retrieve() {
        retrievedoffering = Offering.retrieve("CS100", "I", 2020, 1);
        assertNotNull(retrievedoffering);
        assertEquals("CS100", retrievedoffering.getCode());
        assertEquals("I", retrievedoffering.getSemester());
        assertEquals(2020, retrievedoffering.getYear());
        assertEquals(1, retrievedoffering.getSection());
        assertEquals("Proposed", retrievedoffering.getStatus());
        assertEquals("CS", retrievedoffering.getDepartment());
        assertEquals("a@b.com", retrievedoffering.getCoordinator());
        assertEquals(0, retrievedoffering.getMinCGPA());
        retrievedoffering.setIsSaved(false);
        retrievedoffering.setSection(2);
        assertTrue(retrievedoffering.save());
    }

    @Order(21)
    @Test
    void retrieveAll() {
        ArrayList<Offering> offerings = Offering.retrieveAll();
        ArrayList<Integer> ids = new ArrayList<>();
        assertNotNull(offerings);
        for (Offering o : offerings) {
            ids.add(o.getId());
        }
        assertTrue(ids.contains(offering.getId()));
        assertTrue(ids.contains(retrievedoffering.getId()));
    }

    @Order(22)
    @Test
    void getId() {
        int id = offering.getId();
        assertEquals(id, offering.getId());
    }

    @Order(23)
    @Test
    void setId() {
        int newId = 2;
        int oldId = offering.getId();
        offering.setId(newId);
        assertEquals(newId, offering.getId());
        offering.setId(oldId);
    }

    @Order(22)
    @Test
    void delete() {
        assertTrue(offering.delete());
        assertTrue(retrievedoffering.delete());
        assertTrue(instructor.delete());
        assertTrue(catalog.delete());
        assertTrue(department.delete());
        assertTrue(academicCalender.delete());
    }
}