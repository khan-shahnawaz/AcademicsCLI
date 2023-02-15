package Models;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
class DepartmentTest {
    static Department department;
    static Department retrievedDepartment;

    @BeforeAll
    static void init() {
        department = new Department();
        department.setId("CC");
        department.setName("Computer Science");
    }

    @Test
    @Order(1)
    void getId() {
        String id = "CC";
        assertEquals(id, department.getId());
    }

    @Order(2)
    @Test
    void setId() {
        String newId = "EE";
        department.setId(newId);
        assertEquals(newId, department.getId());
        department.setId("CC");
    }

    @Order(3)
    @Test
    void getName() {
        String name = "Computer Science";
        assertEquals(name, department.getName());
    }

    @Order(4)
    @Test
    void getIsSaved() {
        assertFalse(department.getIsSaved());
    }

    @Order(5)
    @Test
    void setIsSaved() {
        department.setIsSaved(true);
        assertTrue(department.getIsSaved());
        department.setIsSaved(false);
    }

    @Order(6)
    @Test
    void save() {
        Assertions.assertTrue(department.save());
        department.setId("EE");
        Assertions.assertTrue(department.save());
        department.setId("CC");
        Assertions.assertTrue(department.save());
    }

    @Order(7)
    @Test
    void retrieve() {
        retrievedDepartment = Department.retrieve("CC");
        assertEquals(department.getId(), retrievedDepartment.getId());
        assertEquals(department.getName(), retrievedDepartment.getName());
        retrievedDepartment.setId("EE");
        retrievedDepartment.setIsSaved(false);
        retrievedDepartment.save();
    }

    @Order(8)
    @Test
    void retrieveAll() {
        ArrayList<Department> departments = Department.retrieveAll();
        Assertions.assertNotNull(departments);
        ArrayList<String> ids = new ArrayList<>();
        for (Department d : departments) {
            ids.add(d.getId());
        }
        Assertions.assertTrue(ids.contains("CC"));
        Assertions.assertTrue(ids.contains("EE"));
    }

    @Order(9)
    @Test
    void delete() {
        Assertions.assertTrue(department.delete());
        Assertions.assertTrue(retrievedDepartment.delete());
    }
}