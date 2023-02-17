package models;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
class DefaultPrerequisiteTest {
    static DefaultPrerequisite defaultPrerequisite;
    static DefaultPrerequisite retrievedDefaultPrerequisite;
    static Catalog catalog1;
    static Catalog catalog2;
    static Catalog catalog3;

    @BeforeAll
    static void init() {
        catalog1 = new Catalog();
        catalog1.setCode("CS100");
        catalog1.setName("Intro to Computer Science");
        catalog1.setCredits(3);
        catalog2 = new Catalog();
        catalog2.setCode("CS101");
        catalog2.setName("Intro to Computer Science 2");
        catalog2.setCredits(3);
        catalog3 = new Catalog();
        catalog3.setCode("CS102");
        catalog3.setName("Intro to Computer Science 3");
        catalog3.setCredits(3);
        catalog1.save();
        catalog2.save();
        catalog3.save();
        defaultPrerequisite = new DefaultPrerequisite();
        defaultPrerequisite.setCatalogCode("CS101");
        defaultPrerequisite.setPrerequisiteCode("CS100");
        defaultPrerequisite.setMinGrade("C");
    }

    @Test
    @Order(1)
    void getCode() {
        String code = "CS101";
        assertEquals(code, defaultPrerequisite.getCatalogCode());
    }

    @Order(2)
    @Test
    void setCode() {
        String newCode = "CS102";
        defaultPrerequisite.setCatalogCode(newCode);
        assertEquals(newCode, defaultPrerequisite.getCatalogCode());
        defaultPrerequisite.setCatalogCode("CS101");
    }

    @Order(3)
    @Test
    void getPrerequisiteCode() {
        String prerequisiteCode = "CS100";
        assertEquals(prerequisiteCode, defaultPrerequisite.getPrerequisiteCode());
    }

    @Order(4)
    @Test
    void setPrerequisiteCode() {
        String newPrerequisiteCode = "CS102";
        defaultPrerequisite.setPrerequisiteCode(newPrerequisiteCode);
        assertEquals(newPrerequisiteCode, defaultPrerequisite.getPrerequisiteCode());
        defaultPrerequisite.setPrerequisiteCode("CS100");
    }

    @Order(5)
    @Test
    void getMinGrade() {
        String minGrade = "C";
        assertEquals(minGrade, defaultPrerequisite.getMinGrade());
    }

    @Order(6)
    @Test
    void setMinGrade() {
        String newMinGrade = "B";
        defaultPrerequisite.setMinGrade(newMinGrade);
        assertEquals(newMinGrade, defaultPrerequisite.getMinGrade());
        defaultPrerequisite.setMinGrade("C");
    }

    @Order(7)
    @Test
    void getIsSaved() {
        assertFalse(defaultPrerequisite.getIsSaved());
    }

    @Order(8)
    @Test
    void setIsSaved() {
        defaultPrerequisite.setIsSaved(true);
        assertTrue(defaultPrerequisite.getIsSaved());
        defaultPrerequisite.setIsSaved(false);
    }

    @Order(9)
    @Test
    void save() {
        Assertions.assertEquals("00000", defaultPrerequisite.save());
        defaultPrerequisite.setCatalogCode("CS102");
        Assertions.assertEquals("00000", defaultPrerequisite.save());
        defaultPrerequisite.setCatalogCode("CS101");
        Assertions.assertEquals("00000", defaultPrerequisite.save());
    }

    @Order(10)
    @Test
    void retrieve() {
        retrievedDefaultPrerequisite = DefaultPrerequisite.retrieve("CS101", "CS100");
        assertEquals(defaultPrerequisite.getCatalogCode(), retrievedDefaultPrerequisite.getCatalogCode());
        assertEquals(defaultPrerequisite.getPrerequisiteCode(), retrievedDefaultPrerequisite.getPrerequisiteCode());
        assertEquals(defaultPrerequisite.getMinGrade(), retrievedDefaultPrerequisite.getMinGrade());
        retrievedDefaultPrerequisite.setCatalogCode("CS102");
        retrievedDefaultPrerequisite.setIsSaved(false);
        retrievedDefaultPrerequisite.save();
    }

    @Order(11)
    @Test
    void retrieveAll() {
        ArrayList<DefaultPrerequisite> defaultPrerequisites = DefaultPrerequisite.retrieveAll();
        Assertions.assertNotNull(defaultPrerequisites);
        ArrayList<String> catalogCodes = new ArrayList<>();
        for (DefaultPrerequisite d : defaultPrerequisites) {
            catalogCodes.add(d.getCatalogCode());
        }
        Assertions.assertTrue(catalogCodes.contains("CS101"));
        Assertions.assertTrue(catalogCodes.contains("CS102"));
    }

    @Order(12)
    @Test
    void delete() {
        Assertions.assertEquals("00000", defaultPrerequisite.delete());
        Assertions.assertEquals("00000", retrievedDefaultPrerequisite.delete());
        Assertions.assertEquals("00000", catalog1.delete());
        Assertions.assertEquals("00000", catalog2.delete());
        Assertions.assertEquals("00000", catalog3.delete());
    }


}