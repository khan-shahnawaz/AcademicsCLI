package subcommands.students;

import com.opencsv.CSVReaderHeaderAware;
import database.access.DBConnectionSingleton;
import database.access.Transaction;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.sql.Connection;
import java.sql.Statement;
import java.util.concurrent.Callable;

import static database.access.Exception.*;

@Command(name = "remove", mixinStandardHelpOptions = true, version = "add 0.1",
        description = "Remove student from the database.")
public class Remove implements Callable<Integer> {
    Connection connection = DBConnectionSingleton.getConnection();
    @Option(names = {"-F", "--file"}, description = "CSV file containing student details.(Entry Number,Name,Department,Address,Phone,Email,Password,Advisor,Credit Limit, Program, Entry Year)", defaultValue = "", interactive = true, echo = true, prompt = "CSV file Location: ", arity = "0..1")
    private String file;
    @Option(names = {"-e", "--entry"}, description = "Entry of the student.", defaultValue = "", interactive = true, echo = true, prompt = "Entry Number: ", arity = "0..1")
    private String entry;

    private Integer removeStudent() throws Exception {
        models.Student student = models.Student.retrieve(entry);
        if (student == null) {
            System.out.println("Student not found.");
            return NOT_EXISTS;
        }
        String exitCode = student.delete().toLowerCase();
        if (!exitCode.equals("00000")) {
            Transaction.rollback();
            System.err.println("An error occurred while removing the student from the database.");
            return handleSQLException(exitCode, "Deletion failed.");
        }
        Statement statement = connection.createStatement();
        String sql = String.format("DROP USER \"%s\"", entry);
        statement.execute(sql);
        System.out.println("Student removed successfully.");
        return SUCCESS;
    }

    @Override
    public Integer call() {
        try {
            if (file.equals("")) {
                Transaction.start();
                int exitCode = removeStudent();
                if (exitCode == SUCCESS) {
                    Transaction.commit();
                } else {
                    Transaction.rollback();
                }
                return exitCode;
            }
            Transaction.start();
            CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new java.io.FileReader(file));
            java.util.Map<String, String> record;
            while ((record = reader.readMap()) != null) {
                entry = record.get("Entry Number");
                int exitCode = removeStudent();
                if (exitCode != SUCCESS) {
                    Transaction.rollback();
                    return exitCode;
                }
            }
            Transaction.commit();
            return SUCCESS;
        } catch (Exception e) {
            return UNKNOWN;
        }
    }
}
