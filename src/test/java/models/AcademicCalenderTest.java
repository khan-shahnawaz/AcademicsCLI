package models;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
class AcademicCalenderTest {
    static AcademicCalender academicCalender;
    static AcademicCalender retrievedAcademicCalender;

    @BeforeAll
    static void init() {
        academicCalender = new AcademicCalender();
        academicCalender.setYear(2019);
        academicCalender.setSemester("I");
        academicCalender.setEvent("Academic Session");
        academicCalender.setStartDate("2019-03-01");
        academicCalender.setEndDate("2019-03-15");
    }

    @Test
    @Order(1)
    void getYear() {
        int year = 2019;
        assertEquals(year, academicCalender.getYear());
    }

    @Order(2)
    @Test
    void setYear() {
        int newYear = 2020;
        academicCalender.setYear(newYear);
        assertEquals(newYear, academicCalender.getYear());
        academicCalender.setYear(2019);
    }

    @Order(3)
    @Test
    void getSemester() {
        String semester = "I";
        assertEquals(semester, academicCalender.getSemester());
    }

    @Order(4)
    @Test
    void setSemester() {
        String newSemester = "II";
        academicCalender.setSemester(newSemester);
        assertEquals(newSemester, academicCalender.getSemester());
        academicCalender.setSemester("I");
    }

    @Order(5)
    @Test
    void getEvent() {
        String event = "Academic Session";
        assertEquals(event, academicCalender.getEvent());
    }

    @Order(6)
    @Test
    void setEvent() {
        String newEvent = "Mid Semester";
        academicCalender.setEvent(newEvent);
        assertEquals(newEvent, academicCalender.getEvent());
        academicCalender.setEvent("Academic Session");
    }

    @Order(7)
    @Test
    void getStartDate() {
        String startDate = "2019-03-01";
        assertEquals(startDate, academicCalender.getStartDate());
    }

    @Order(8)
    @Test
    void setStartDate() {
        String newStartDate = "2019-03-02";
        academicCalender.setStartDate(newStartDate);
        assertEquals(newStartDate, academicCalender.getStartDate());
        academicCalender.setStartDate("2019-03-01");
    }

    @Order(9)
    @Test
    void getEndDate() {
        String endDate = "2019-03-15";
        assertEquals(endDate, academicCalender.getEndDate());
    }

    @Order(10)
    @Test
    void setEndDate() {
        String newEndDate = "2019-03-16";
        academicCalender.setEndDate(newEndDate);
        assertEquals(newEndDate, academicCalender.getEndDate());
        academicCalender.setEndDate("2019-03-15");
    }

    @Order(11)
    @Test
    void getIsSaved() {
        assertFalse(academicCalender.getIsSaved());
    }

    @Order(12)
    @Test
    void setIsSaved() {
        academicCalender.setIsSaved(true);
        assertTrue(academicCalender.getIsSaved());
        academicCalender.setIsSaved(false);
    }

    @Order(13)
    @Test
    void save() {
        Assertions.assertEquals("00000", academicCalender.save());
        academicCalender.setYear(2020);
        Assertions.assertEquals("00000", academicCalender.save());
        academicCalender.setYear(2019);
        Assertions.assertEquals("00000", academicCalender.save());
    }

    @Order(14)
    @Test
    void retrieve() {
        retrievedAcademicCalender = AcademicCalender.retrieve(2019, "I", "Academic Session");
        assertEquals(academicCalender.getYear(), retrievedAcademicCalender.getYear());
        assertEquals(academicCalender.getSemester(), retrievedAcademicCalender.getSemester());
        assertEquals(academicCalender.getEvent(), retrievedAcademicCalender.getEvent());
        assertEquals(academicCalender.getStartDate(), retrievedAcademicCalender.getStartDate());
        assertEquals(academicCalender.getEndDate(), retrievedAcademicCalender.getEndDate());
        retrievedAcademicCalender.setYear(2020);
        retrievedAcademicCalender.setIsSaved(false);
        retrievedAcademicCalender.save();
    }

    @Order(15)
    @Test
    void retrieveAll() {
        ArrayList<AcademicCalender> academicCalenders = AcademicCalender.retrieveAll();
        Assertions.assertNotNull(academicCalenders);
        ArrayList<Integer> years = new ArrayList<>();
        for (AcademicCalender a : academicCalenders) {
            years.add(a.getYear());
        }
        Assertions.assertTrue(years.contains(2019));
        Assertions.assertTrue(years.contains(2020));
    }

    @Order(16)
    @Test
    void delete() {
        Assertions.assertEquals("00000", academicCalender.delete());
        Assertions.assertEquals("00000", retrievedAcademicCalender.delete());
    }
}