package subcommands.students;

import com.opencsv.CSVReaderHeaderAware;
import database.access.DBConnectionSingleton;
import database.access.Transaction;
import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Callable;

import static database.access.Exception.*;

@CommandLine.Command(name = "add", mixinStandardHelpOptions = true, version = "add 0.1",
        description = "Add student to the database.")
public class Add implements Callable<Integer> {
    Connection connection = DBConnectionSingleton.getConnection();
    @Option(names = {"-F", "--file"}, description = "CSV file containing student details.(CSV file containing student details.(Entry Number,Name,Department,Address,Phone,Email,Password,Advisor,Credit Limit, Program, Entry Year))", defaultValue = "", interactive = true, echo = true, prompt = "CSV file Location: ", arity = "0..1")
    private String file;
    @Option(names = {"-n", "--name"}, description = "Name of the student.", defaultValue = "", interactive = true, echo = true, prompt = "Name: ", arity = "0..1")
    private String name;
    @Option(names = {"-d", "--department"}, description = "Department of the student.", defaultValue = "", interactive = true, echo = true, prompt = "Department: ", arity = "0..1")
    private String department;
    @Option(names = {"-a", "--address"}, description = "Address of the student.", defaultValue = "", interactive = true, echo = true, prompt = "Address: ", arity = "0..1")
    private String address;
    @Option(names = {"-p", "--phone"}, description = "Phone number of the student.", defaultValue = "", interactive = true, echo = true, prompt = "Phone: ", arity = "0..1")
    private String phone;
    @Option(names = {"-E", "--email"}, description = "Email of the student.", defaultValue = "", interactive = true, echo = true, prompt = "Email: ", arity = "0..1")
    private String email;
    @Option(names = {"-y", "--year"}, description = "Entry Year of the student.", interactive = true, echo = true, prompt = "Year: ", arity = "0..1")
    private int year;
    @Option(names = {"-w", "--password"}, description = "Password of the student.", defaultValue = "", interactive = true, prompt = "Password: ", arity = "0..1")
    private String password;
    @Option(names = {"-A", "--advisor"}, description = "Advisor of the student.", defaultValue = "", interactive = true, echo = true, prompt = "Advisor: ", arity = "0..1")
    private String advisor;
    @Option(names = {"-P", "--program"}, description = "Program of the student.", defaultValue = "", interactive = true, echo = true, prompt = "Program: ", arity = "0..1")
    private String program;
    @Option(names = {"-e", "--entry"}, description = "Entry number of the student.", defaultValue = "", interactive = true, echo = true, prompt = "Entry Number: ", arity = "0..1")
    private String entry;
    @Option(names = {"-c", "--credits"}, description = "Credit Limits for the student.", interactive = true, echo = true, prompt = "Credit Limits: ", arity = "0..1")
    private float credits;

    private Integer addStudent() throws Exception {
        models.Student student = new models.Student();
        System.out.println("Adding student to the database...");
        student.setName(name);
        student.setDepartmentCode(department);
        student.setAddress(address);
        student.setProgram(program);
        student.setPhone(phone);
        student.setEmail(email);
        student.setEntryYear(year);
        student.setAdvisor(advisor);
        student.setEntryNumber(entry);
        student.setCgpa(0.0f);
        student.setCreditsLimit(credits);
        String exitCode = student.save();
        if (!exitCode.equals("00000")) {
            Transaction.rollback();
            System.err.println("An error occurred while adding student to the database.");
            return handleSQLException(exitCode, "Insertion of student failed.");
        }
        String sql = String.format("CREATE USER \"%s\" WITH PASSWORD '%s';", entry, password);
        Statement statement = connection.createStatement();
        statement.execute(sql);
        sql = String.format("GRANT student TO \"%s\";", entry);
        statement.execute(sql);
        return SUCCESS;
    }

    @Override
    public Integer call() {
        try {
            if (file.equals("")) {
                Transaction.start();
                Integer exitCode = addStudent();
                if (exitCode != SUCCESS) {
                    return exitCode;
                }
                Transaction.commit();
                System.out.println("Student added successfully.");
                return SUCCESS;
            }
            System.out.println("Reading from file...");
            CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new java.io.FileReader(file));
            Transaction.start();
            System.out.println("Adding students to the database...");
            java.util.Map<String, String> record;
            while ((record = reader.readMap()) != null) {
                name = record.get("Name");
                department = record.get("Department");
                address = record.get("Address");
                phone = record.get("Phone");
                email = record.get("Email");
                year = Integer.parseInt(record.get("Entry Year"));
                password = record.get("Password");
                advisor = record.get("Advisor");
                program = record.get("Program");
                entry = record.get("Entry Number");
                credits = Float.parseFloat(record.get("Credit Limit"));

                Integer exitCode = addStudent();
                if (exitCode != SUCCESS) {
                    return exitCode;
                }

            }
            Transaction.commit();
            System.out.println("Students added successfully.");
            return SUCCESS;
        } catch (SQLException e) {
            return handleSQLException(e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            return UNKNOWN;
        }
    }
}
