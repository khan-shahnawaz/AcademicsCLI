package models;

import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InstructorTest {
    static Instructor instructor;
    static Instructor retrievedInstructor;
    static ArrayList<Instructor> instructors;
    static Department department;

    @BeforeAll
    static void init() {
        instructor = new Instructor();
        department = new Department();
        department.setId("CS");
        department.setName("Computer Science");
        department.save();
        instructor.setName("Anil Shukla");
        instructor.setEmail("shukla@iitrpr.ac.in");
        instructor.setPhone("1234567890");
        instructor.setDepartmentCode("CS");
        instructor.setAddress("Ropar");
    }

    @Test
    @Order(1)
    void getName() {
        String name = "Anil Shukla";
        assertEquals(name, instructor.getName());
    }

    @Test
    @Order(2)
    void setName() {
        String newName = "Anil Kumar Shukla";
        instructor.setName(newName);
        assertEquals(newName, instructor.getName());
        instructor.setName("Anil Shukla");
    }

    @Test
    @Order(3)
    void getEmail() {
        String email = "shukla@iitrpr.ac.in";
        assertEquals(email, instructor.getEmail());
    }

    @Test
    @Order(4)
    void setEmail() {
        String newEmail = "a@b.com";
        instructor.setEmail(newEmail);
        assertEquals(newEmail, instructor.getEmail());
        instructor.setEmail("shukla@iitrpr.ac.in");

    }

    @Test
    @Order(5)
    void getPhone() {
        String phone = "1234567890";
        assertEquals(phone, instructor.getPhone());
    }

    @Test
    @Order(6)
    void setPhone() {
        String newPhone = "0987654321";
        instructor.setPhone(newPhone);
        assertEquals(newPhone, instructor.getPhone());
        instructor.setPhone("1234567890");
    }

    @Test
    @Order(7)
    void getDepartmentCode() {
        String departmentCode = "CS";
        assertEquals(departmentCode, instructor.getDepartmentCode());
    }

    @Test
    @Order(8)
    void setDepartmentCode() {
        String newDepartmentCode = "EE";
        instructor.setDepartmentCode(newDepartmentCode);
        assertEquals(newDepartmentCode, instructor.getDepartmentCode());
        instructor.setDepartmentCode("CS");
    }

    @Test
    @Order(9)
    void getAddress() {
        String address = "Ropar";
        assertEquals(address, instructor.getAddress());
    }

    @Test
    @Order(10)
    void setAddress() {
        String newAddress = "Patiala";
        instructor.setAddress(newAddress);
        assertEquals(newAddress, instructor.getAddress());
        instructor.setAddress("Ropar");
    }

    @Test
    @Order(11)
    void getIsSaved() {
        boolean isSaved = false;
        assertEquals(isSaved, instructor.getIsSaved());
    }

    @Test
    @Order(12)
    void setIsSaved() {
        boolean newIsSaved = true;
        instructor.setIsSaved(newIsSaved);
        assertEquals(newIsSaved, instructor.getIsSaved());
        instructor.setIsSaved(false);
    }

    @Test
    @Order(13)
    void save() {
        Assertions.assertEquals("00000", instructor.save());
        instructor.setEmail("a@b.com");
        Assertions.assertEquals("00000", instructor.save());
        instructor.setEmail("shukla@iitrpr.ac.in");
        Assertions.assertEquals("00000", instructor.save());
    }

    @Test
    @Order(14)
    void retrieve() {
        retrievedInstructor = Instructor.retrieve("shukla@iitrpr.ac.in");
        assertEquals(instructor.getName(), retrievedInstructor.getName());
        assertEquals(instructor.getEmail(), retrievedInstructor.getEmail());
        assertEquals(instructor.getPhone(), retrievedInstructor.getPhone());
        assertEquals(instructor.getDepartmentCode(), retrievedInstructor.getDepartmentCode());
        assertEquals(instructor.getAddress(), retrievedInstructor.getAddress());
        assertEquals(instructor.getIsSaved(), retrievedInstructor.getIsSaved());
        retrievedInstructor.setEmail("a@b.com");
        retrievedInstructor.setIsSaved(false);
        Assertions.assertEquals("00000", retrievedInstructor.save());
    }

    @Test
    @Order(15)
    void retrieveAll() {
        instructors = Instructor.retrieveAll();
        Assertions.assertNotNull(instructors);
        ArrayList<String> emails = new ArrayList<>();
        for (Instructor instructor : instructors) {
            emails.add(instructor.getEmail());
        }
        Assertions.assertTrue(emails.contains(instructor.getEmail()));
        Assertions.assertTrue(emails.contains(retrievedInstructor.getEmail()));
    }

    @Test
    @Order(16)
    void delete() {
        Assertions.assertEquals("00000", instructor.delete());
        Assertions.assertEquals("00000", retrievedInstructor.delete());
        Assertions.assertEquals("00000", department.delete());
    }
}