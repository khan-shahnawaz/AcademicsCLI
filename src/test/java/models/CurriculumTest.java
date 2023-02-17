package models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
class CurriculumTest {
    static Curriculum curriculum;
    static Curriculum retrievedCurriculum;

    @BeforeAll
    static void init() {
        curriculum = new Curriculum();
        curriculum.setProgram("B.Tech");
        curriculum.setMinCredits(60);
        curriculum.setCourseType("PC");
    }

    @Test
    @Order(1)
    void getProgram() {
        String program = "B.Tech";
        assertEquals(program, curriculum.getProgram());
    }

    @Order(2)
    @Test
    void setProgram() {
        String newProgram = "M.Tech";
        curriculum.setProgram(newProgram);
        assertEquals(newProgram, curriculum.getProgram());
        curriculum.setProgram("B.Tech");
    }

    @Order(3)
    @Test
    void getMinCredits() {
        int minCredits = 60;
        assertEquals(minCredits, curriculum.getMinCredits());
    }

    @Order(4)
    @Test
    void setMinCredits() {
        int newMinCredits = 70;
        curriculum.setMinCredits(newMinCredits);
        assertEquals(newMinCredits, curriculum.getMinCredits());
        curriculum.setMinCredits(60);
    }

    @Order(5)
    @Test
    void getCourseType() {
        String courseType = "PC";
        assertEquals(courseType, curriculum.getCourseType());
    }

    @Order(6)
    @Test
    void setCourseType() {
        String newCourseType = "PE";
        curriculum.setCourseType(newCourseType);
        assertEquals(newCourseType, curriculum.getCourseType());
        curriculum.setCourseType("PC");
    }

    @Order(7)
    @Test
    void getIsSaved() {
        assertFalse(curriculum.getIsSaved());
    }

    @Order(8)
    @Test
    void setIsSaved() {
        curriculum.setIsSaved(true);
        assertTrue(curriculum.getIsSaved());
        curriculum.setIsSaved(false);
    }

    @Order(9)
    @Test
    void save() {
        assertEquals("00000", curriculum.save());
        curriculum.setMinCredits(70);
        assertEquals("00000", curriculum.save());
        curriculum.setMinCredits(60);
        assertEquals("00000", curriculum.save());
    }

    @Order(10)
    @Test
    void retrieve() {
        retrievedCurriculum = Curriculum.retrieve("B.Tech", "PC");
        assertNotNull(retrievedCurriculum);
        assertEquals(curriculum.getProgram(), retrievedCurriculum.getProgram());
        assertEquals(curriculum.getMinCredits(), retrievedCurriculum.getMinCredits());
        assertEquals(curriculum.getCourseType(), retrievedCurriculum.getCourseType());
        retrievedCurriculum.setIsSaved(false);
        retrievedCurriculum.setProgram("M.Tech");
        assertEquals("00000", retrievedCurriculum.save());
    }

    @Order(11)
    @Test
    void retrieveAll() {
        ArrayList<Curriculum> curriculums = Curriculum.retrieveAll();
        ArrayList<String> programs = new ArrayList<>();
        assertNotNull(curriculums);
        for (Curriculum curriculum : curriculums) {
            programs.add(curriculum.getProgram());
        }
        assertTrue(programs.contains("B.Tech"));
        assertTrue(programs.contains("M.Tech"));
    }

    @Order(12)
    @Test
    void delete() {
        assertEquals("00000", curriculum.delete());
        assertEquals("00000", retrievedCurriculum.delete());
    }
}