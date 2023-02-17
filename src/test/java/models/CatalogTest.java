package models;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(OrderAnnotation.class)
class CatalogTest {
    static Catalog catalog;
    static Catalog retrievedCatalog;

    @BeforeAll
    static void init() {
        catalog = new Catalog();
        catalog.setCode("CS101");
        catalog.setName("Introduction to Computer Science");
        catalog.setDescription("Introduction to Computer Science");
        catalog.setCredits(4);
        catalog.setL(3);
        catalog.setT(1);
        catalog.setP(0);
        catalog.setS(6);
        catalog.setC(4);
    }

    @Test
    @Order(1)
    void getCode() {
        String code = "CS101";
        assertEquals(code, catalog.getCode());
    }

    @Order(2)
    @Test
    void setCode() {
        String newCode = "CS102";
        catalog.setCode(newCode);
        assertEquals(newCode, catalog.getCode());
        catalog.setCode("CS101");
    }

    @Order(3)
    @Test
    void getName() {
        String name = "Introduction to Computer Science";
        assertEquals(name, catalog.getName());
    }

    @Order(4)
    @Test
    void setName() {
        String newName = "Introduction to Computer Science and Programming";
        catalog.setName(newName);
        assertEquals(newName, catalog.getName());
        catalog.setName("Introduction to Computer Science");
    }

    @Order(5)
    @Test
    void getDescription() {
        String description = "Introduction to Computer Science";
        assertEquals(description, catalog.getDescription());
    }

    @Order(6)
    @Test
    void setDescription() {
        String newDescription = "Introduction to Computer Science and Programming";
        catalog.setDescription(newDescription);
        assertEquals(newDescription, catalog.getDescription());
        catalog.setDescription("Introduction to Computer Science");
    }

    @Order(7)
    @Test
    void getCredits() {
        int credits = 4;
        assertEquals(credits, catalog.getCredits());
    }

    @Order(8)
    @Test
    void setCredits() {
        int newCredits = 3;
        catalog.setCredits(newCredits);
        assertEquals(newCredits, catalog.getCredits());
        catalog.setCredits(4);
    }

    @Order(9)
    @Test
    void getL() {
        int l = 3;
        assertEquals(l, catalog.getL());
    }

    @Order(10)
    @Test
    void setL() {
        int newL = 2;
        catalog.setL(newL);
        assertEquals(newL, catalog.getL());
        catalog.setL(3);
    }

    @Order(11)
    @Test
    void getT() {
        int t = 1;
        assertEquals(t, catalog.getT());
    }

    @Order(12)
    @Test
    void setT() {
        int newT = 2;
        catalog.setT(newT);
        assertEquals(newT, catalog.getT());
        catalog.setT(1);
    }

    @Order(13)
    @Test
    void getP() {
        int p = 0;
        assertEquals(p, catalog.getP());
    }

    @Order(14)
    @Test
    void setP() {
        int newP = 1;
        catalog.setP(newP);
        assertEquals(newP, catalog.getP());
        catalog.setP(0);
    }

    @Order(15)
    @Test
    void getS() {
        int s = 6;
        assertEquals(s, catalog.getS());
    }

    @Order(16)
    @Test
    void setS() {
        int newS = 4;
        catalog.setS(newS);
        assertEquals(newS, catalog.getS());
        catalog.setS(6);
    }

    @Order(17)
    @Test
    void getC() {
        int c = 4;
        assertEquals(c, catalog.getC());
    }

    @Order(18)
    @Test
    void setC() {
        int newC = 3;
        catalog.setC(newC);
        assertEquals(newC, catalog.getC());
        catalog.setC(4);
    }

    @Order(19)
    @Test
    void getIsSaved() {
        boolean isSaved = false;
        assertEquals(isSaved, catalog.getIsSaved());
    }

    @Order(20)
    @Test
    void setIsSaved() {
        boolean newIsSaved = true;
        catalog.setIsSaved(newIsSaved);
        assertEquals(newIsSaved, catalog.getIsSaved());
        catalog.setIsSaved(false);
    }

    @Test
    @Order(21)
    void save() {
        Assertions.assertEquals("00000", catalog.save());
        catalog.setName("Introduction to Computer Science and Programming");
        Assertions.assertEquals("00000", catalog.save());
        catalog.setName("Introduction to Computer Science");
        Assertions.assertEquals("00000", catalog.save());
    }

    @Test
    @Order(22)
    void retrieve() {
        retrievedCatalog = Catalog.retrieve(catalog.getCode());
        assertEquals(catalog.getCode(), retrievedCatalog.getCode());
        assertEquals(catalog.getName(), retrievedCatalog.getName());
        assertEquals(catalog.getDescription(), retrievedCatalog.getDescription());
        assertEquals(catalog.getCredits(), retrievedCatalog.getCredits());
        assertEquals(catalog.getL(), retrievedCatalog.getL());
        assertEquals(catalog.getT(), retrievedCatalog.getT());
        assertEquals(catalog.getP(), retrievedCatalog.getP());
        assertEquals(catalog.getS(), retrievedCatalog.getS());
        assertEquals(catalog.getC(), retrievedCatalog.getC());
        assertEquals(catalog.getIsSaved(), retrievedCatalog.getIsSaved());
        retrievedCatalog.setIsSaved(false);
        retrievedCatalog.setCode("CS102");
        retrievedCatalog.save();
    }

    @Test
    @Order(23)
    void retrieveAll() {
        ArrayList<String> codes = new ArrayList<>();
        ArrayList<Catalog> catalogs = Catalog.retrieveAll();

        Assertions.assertNotNull(catalogs);
        for (Catalog thiscatalog : catalogs) {
            codes.add(thiscatalog.getCode());
        }
        Assertions.assertTrue(codes.contains(catalog.getCode()));
        Assertions.assertTrue(codes.contains(retrievedCatalog.getCode()));
    }

    @Test
    @Order(24)
    void delete() {

        Assertions.assertEquals("00000", catalog.delete());
        Assertions.assertEquals("00000", retrievedCatalog.delete());
    }
}