package subcommands.instructors;
import com.opencsv.CSVReaderHeaderAware;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import database.access.DBConnectionSingleton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.concurrent.Callable;
import static database.access.Exception.SUCCESS;
import static database.access.Exception.*;
import database.access.Transaction;

@Command(name = "add", mixinStandardHelpOptions = true, version = "add 0.1",
        description = "Add instructor to the database.")
public class Add implements Callable<Integer>{
    @Option(names = {"-F","--file"}, description = "CSV file containing instructor details.(Name,Department,Address,Phone,Email,Password)", defaultValue = "", interactive = true, echo = true, prompt = "CSV file Location: ", arity = "0..1")
    private String file;
    @Option(names = {"-n","--name"}, description = "Name of the instructor.", defaultValue = "", interactive = true, echo = true, prompt = "Name: ", arity = "0..1")
    private String name;
    @Option(names = {"-d","--department"}, description = "Department of the instructor.", defaultValue = "", interactive = true, echo = true, prompt = "Department: ", arity = "0..1")
    private String department;
    @Option(names = {"-a","--address"}, description = "Address of the instructor.", defaultValue = "", interactive = true, echo = true, prompt = "Address: ", arity = "0..1")
    private String address;
    @Option(names = {"-p","--phone"}, description = "Phone number of the instructor.", defaultValue = "", interactive = true, echo = true, prompt = "Phone: ", arity = "0..1")
    private String phone;
    @Option(names = {"-e","--email"}, description = "Email of the instructor.", defaultValue = "", interactive = true, echo = true, prompt = "Email: ", arity = "0..1")
    private String email;
    @Option(names = {"-w","--password"}, description = "Password of the instructor.", defaultValue = "", interactive = true, prompt = "Password: ", arity = "0..1")
    private String password;
    Connection connection = DBConnectionSingleton.getConnection();
    private Integer addInstructor() throws Exception {
        models.Instructor instructor = new models.Instructor();
        instructor.setName(name);
        instructor.setDepartmentCode(department);
        instructor.setAddress(address);
        instructor.setPhone(phone);
        instructor.setEmail(email);
        String exitCode = instructor.save().toLowerCase();
        if (!exitCode.equals("00000")) {
            Transaction.rollback();
            System.err.println("An error occurred while adding the instructor to the database.");
            return handleSQLException(exitCode, "Insertion failed.");
        }
        String sql = String.format("CREATE USER \"%s\" WITH PASSWORD '%s';",email,password);
        Statement statement = connection.createStatement();
        statement.execute(sql);
        sql = String.format("GRANT instructor TO \"%s\";",email);
        statement = connection.createStatement();
        statement.execute(sql);
        return SUCCESS;
    }

    @Override
    public Integer call() {
        try {
            if (file.equals("")) {
                Transaction.start();
                Integer exitCode = addInstructor();
                if (exitCode != SUCCESS) {
                    return exitCode;
                }
                Transaction.commit();
                System.out.print("Instructor added successfully.");
                return SUCCESS;
            }
            System.out.println("Adding instructors from file: " + file);
            CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new java.io.FileReader(file));
            Transaction.start();
            java.util.Map<String, String> record;
            while ((record = reader.readMap()) != null) {
                name = record.get("Name");
                department = record.get("Department");
                address = record.get("Address");
                phone = record.get("Phone");
                email = record.get("Email");
                password = record.get("Password");
                Integer exitCode = addInstructor();
                if (exitCode != SUCCESS) {
                    return exitCode;
                }
            }
            Transaction.commit();
            System.out.print("Instructors added successfully.");
            return SUCCESS;
        } catch (Exception e) {
            return UNKNOWN;
        }
    }
}
