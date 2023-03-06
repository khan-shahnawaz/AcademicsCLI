package org.acad;

import database.access.DBConnectionSingleton;
import picocli.CommandLine;

import java.util.concurrent.Callable;

import static database.access.Exception.SUCCESS;
import static database.access.Exception.UNAUTHORISED;

@CommandLine.Command(name = "profile", mixinStandardHelpOptions = true, version = "acad 0.1",
        description = "Update profile information.")
public class Profile implements Callable<Integer> {
    @CommandLine.Option(names = {"-a", "--address"}, description = "Address", interactive = true, echo = true, arity = "0..1")
    private String address;
    @CommandLine.Option(names = {"-p", "--phone"}, description = "Phone", interactive = true, echo = true, arity = "0..1")
    private String phone;

    @Override
    public Integer call() throws Exception {
        String userName = DBConnectionSingleton.getUserName();
        models.Student student = models.Student.retrieve(userName);
        if (student == null) {
            models.Instructor instructor = models.Instructor.retrieve(userName);
            if (instructor == null) {
                System.err.println("Only Instructors and Students can update their profile.");
                return UNAUTHORISED;
            }
            if (address != null) {
                instructor.setAddress(address);
            }
            if (phone != null) {
                instructor.setPhone(phone);
            }
            instructor.updateProfile();
        } else {
            if (address != null) {
                student.setAddress(address);
            }
            if (phone != null) {
                student.setPhone(phone);
            }
            student.updateProfile();
        }
        System.out.println("Profile updated successfully.");
        return SUCCESS;
    }
}
