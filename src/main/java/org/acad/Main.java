package org.acad;

import Models.Student;

public class Main {
    public static void main(String[] args) {
        Student student = new Student();
        student.setEntryNumber("2020CSB1123");
        student.setName("Shahnawaz Khan");
        student.setEmail("shahnawazkhan5172@gmail.com");
        student.setPhone("1234567890");
        student.setDepartmentCode("CS");
        student.setEntryYear(2020);
        student.setAddress("Bengaluru");
        student.setProgram("B.Tech");
        student.setCgpa(0);
        student.setCreditsLimit(0);
        student.setAdvisor("shukla@iitrpr.ac.in");
        System.out.println(student.save());
        student.setIsSaved(true);
        student.setCreditsLimit(24);
        System.out.println(student.getCreditsLimit());
        System.out.println(student.save());
        System.out.println(student.delete());
    }
}