package models;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
class StudentTest {
    static Student student;
    static Student retrievedStudent;
    static Department department;
    static Instructor instructor;

    @BeforeAll
    static void init() {
        student = new Student();
        department = new Department();
        department.setId("CS");
        department.setName("Computer Science");
        department.save();
        instructor = new Instructor();
        instructor.setName("Anil Shukla");
        instructor.setEmail("shukla@iitrpr.ac.in");
        instructor.setPhone("1234567890");
        instructor.setDepartmentCode("CS");
        instructor.setAddress("Ropar");
        instructor.save();
        student.setEntryNumber("2020CSB1123");
        student.setName("Shahnawaz Khan");
        student.setEmail("shahnawazkhan5172@gmail.com");
        student.setPhone("1234567890");
        student.setDepartmentCode("CS");
        student.setEntryYear(2020);
        student.setAddress("Muzaffarpur");
        student.setProgram("B.Tech");
        student.setCgpa(0);
        student.setCreditsLimit(0);
        student.setAdvisor("shukla@iitrpr.ac.in");
    }

    @Test
    @Order(1)
    void getEntryNumber() {
        String entryNumber = "2020CSB1123";
        assertEquals(entryNumber, student.getEntryNumber());
    }

    @Test
    @Order(2)
    void setEntryNumber() {
        String newEntryNumber = "2020CSB1124";
        student.setEntryNumber(newEntryNumber);
        assertEquals(newEntryNumber, student.getEntryNumber());
        student.setEntryNumber("2020CSB1123");
    }

    @Test
    @Order(3)
    void getName() {
        String name = "Shahnawaz Khan";
        assertEquals(name, student.getName());
    }

    @Test
    @Order(4)
    void setName() {
        String newName = "S Khan";
        student.setName(newName);
        assertEquals(newName, student.getName());
        student.setName("Shahnawaz Khan");
    }

    @Test
    @Order(5)
    void getEmail() {
        String email = "shahnawazkhan5172@gmail.com";
        assertEquals(email, student.getEmail());
    }

    @Test
    @Order(6)
    void setEmail() {
        String newEmail = "a@b.com";
        student.setEmail(newEmail);
        assertEquals(newEmail, student.getEmail());
        student.setEmail("shahnawazkhan5172@gmail.com");
    }

    @Test
    @Order(7)
    void getPhone() {
        String phone = "1234567890";
        assertEquals(phone, student.getPhone());
    }

    @Test
    @Order(8)
    void setPhone() {
        String newPhone = "1234567891";
        student.setPhone(newPhone);
        assertEquals(newPhone, student.getPhone());
        student.setPhone("1234567890");
    }

    @Test
    @Order(9)
    void getDepartmentCode() {
        String departmentCode = "CS";
        assertEquals(departmentCode, student.getDepartmentCode());
    }

    @Test
    @Order(10)
    void setDepartmentCode() {
        String newDepartmentCode = "ME";
        student.setDepartmentCode(newDepartmentCode);
        assertEquals(newDepartmentCode, student.getDepartmentCode());
        student.setDepartmentCode("CS");
    }

    @Test
    @Order(11)
    void getEntryYear() {
        int entryYear = 2020;
        assertEquals(entryYear, student.getEntryYear());
    }

    @Test
    @Order(12)
    void setEntryYear() {
        int newEntryYear = 2021;
        student.setEntryYear(newEntryYear);
        assertEquals(newEntryYear, student.getEntryYear());
        student.setEntryYear(2020);
    }

    @Test
    @Order(13)
    void getAddress() {
        String address = "Muzaffarpur";
        assertEquals(address, student.getAddress());
    }

    @Test
    @Order(14)
    void setAddress() {
        String newAddress = "Muzaffarpur";
        student.setAddress(newAddress);
        assertEquals(newAddress, student.getAddress());
        student.setAddress("Muzaffarpur");
    }

    @Test
    @Order(15)
    void getProgram() {
        String program = "B.Tech";
        assertEquals(program, student.getProgram());
    }

    @Test
    @Order(16)
    void setProgram() {
        String newProgram = "M.Tech";
        student.setProgram(newProgram);
        assertEquals(newProgram, student.getProgram());
        student.setProgram("B.Tech");
    }

    @Test
    @Order(17)
    void getCgpa() {
        double cgpa = 0;
        assertEquals(cgpa, student.getCgpa());
    }

    @Test
    @Order(18)
    void setCgpa() {
        float newCgpa = 1;
        student.setCgpa(newCgpa);
        assertEquals(newCgpa, student.getCgpa());
        student.setCgpa(0);
    }

    @Test
    @Order(19)
    void getCreditsLimit() {
        int creditsLimit = 0;
        assertEquals(creditsLimit, student.getCreditsLimit());
    }

    @Test
    @Order(20)
    void setCreditsLimit() {
        int newCreditsLimit = 1;
        student.setCreditsLimit(newCreditsLimit);
        assertEquals(newCreditsLimit, student.getCreditsLimit());
        student.setCreditsLimit(0);
    }

    @Test
    @Order(21)
    void getAdvisor() {
        String advisor = "shukla@iitrpr.ac.in";
        assertEquals(advisor, student.getAdvisor());
    }

    @Test
    @Order(22)
    void setAdvisor() {
        String newAdvisor = "new@a.com";
        student.setAdvisor(newAdvisor);
        assertEquals(newAdvisor, student.getAdvisor());
        student.setAdvisor("shukla@iitrpr.ac.in");
    }

    @Test
    @Order(23)
    void getIsSaved() {
        assertFalse(student.getIsSaved());
    }

    @Test
    @Order(24)
    void setIsSaved() {
        student.setIsSaved(true);
        assertTrue(student.getIsSaved());
        student.setIsSaved(false);
    }

    @Test
    @Order(25)
    void save() {
        Assertions.assertEquals("00000", student.save());
        student.setAddress("Bihar");
        Assertions.assertEquals("00000", student.save());
        student.setAddress("Muzaffarpur");
        Assertions.assertEquals("00000", student.save());
    }

    @Test
    @Order(26)
    void retrieve() {
        retrievedStudent = Student.retrieve(student.getEntryNumber());
        assertEquals(student.getEntryNumber(), retrievedStudent.getEntryNumber());
        assertEquals(student.getName(), retrievedStudent.getName());
        assertEquals(student.getEmail(), retrievedStudent.getEmail());
        assertEquals(student.getPhone(), retrievedStudent.getPhone());
        assertEquals(student.getDepartmentCode(), retrievedStudent.getDepartmentCode());
        assertEquals(student.getEntryYear(), retrievedStudent.getEntryYear());
        assertEquals(student.getAddress(), retrievedStudent.getAddress());
        assertEquals(student.getProgram(), retrievedStudent.getProgram());
        assertEquals(student.getCgpa(), retrievedStudent.getCgpa());
        assertEquals(student.getCreditsLimit(), retrievedStudent.getCreditsLimit());
        assertEquals(student.getAdvisor(), retrievedStudent.getAdvisor());
        retrievedStudent.setIsSaved(false);
        retrievedStudent.setEntryNumber("2019CSB0301");
        retrievedStudent.save();
    }

    @Test
    @Order(27)
    void retrieveAll() {
        ArrayList<String> entryNumbers = new ArrayList<>();
        ArrayList<Student> students = Student.retrieveAll();
        Assertions.assertNotNull(students);
        for (Student thisStudent : students) {
            entryNumbers.add(thisStudent.getEntryNumber());
        }
        Assertions.assertTrue(entryNumbers.contains(student.getEntryNumber()));
        Assertions.assertTrue(entryNumbers.contains(retrievedStudent.getEntryNumber()));
    }

    @Test
    @Order(28)
    void delete() {

        Assertions.assertEquals("00000", student.delete());
        Assertions.assertEquals("00000", retrievedStudent.delete());
        Assertions.assertEquals("00000", instructor.delete());
        Assertions.assertEquals("00000", department.delete());
    }


}