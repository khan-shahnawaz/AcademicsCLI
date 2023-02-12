package Models;
import DatabaseAccess.DBConnectionSingleton;
import DatabaseAccess.DAOInterface;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.sql.Connection;
import java.util.Properties;

/**
 * This class represents a student.
 * @author Shahnawaz Khan
 * @version 1.0
 * @since 2023-02-12
 */

public class Student implements DAOInterface {
    private static final String PROPERTIES_FILE = "student.properties";
    private String entryNumber;
    private String name;
    private String email;
    private String phone;
    private String departmentCode;
    private int entryYear;
    private String address;
    private String program;
    private float cgpa;
    private float creditsLimit;
    private String advisor;
    private final Connection connection;
    private final HashMap<String, String> lastSavedValues;
    private boolean isSaved;
    private static Properties properties;

    public Student() {
        this.connection = DBConnectionSingleton.getConnection();
        this.lastSavedValues = new HashMap<>();
        this.isSaved = false;
    }

    static {
        try {
            properties = new Properties();
            ClassLoader classLoader = Student.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(PROPERTIES_FILE);
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getEntryNumber() {
        return entryNumber;
    }

    public void setEntryNumber(String entryNumber) {
        this.lastSavedValues.putIfAbsent("entry_no", entryNumber);
        this.entryNumber = entryNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.lastSavedValues.putIfAbsent("name", name);
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.lastSavedValues.putIfAbsent("email", email);
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.lastSavedValues.putIfAbsent("phone", phone);
        this.phone = phone;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.lastSavedValues.putIfAbsent("department", departmentCode);
        this.departmentCode = departmentCode;
    }

    public int getEntryYear() {
        return entryYear;
    }

    public void setEntryYear(int entryYear) {
        this.lastSavedValues.putIfAbsent("entry_year", String.valueOf(entryYear));
        this.entryYear = entryYear;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.lastSavedValues.putIfAbsent("address", address);
        this.address = address;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.lastSavedValues.putIfAbsent("program", program);
        this.program = program;
    }

    public float getCgpa() {
        return cgpa;
    }

    public void setCgpa(float cgpa) {
        this.lastSavedValues.putIfAbsent("cgpa", String.valueOf(cgpa));
        this.cgpa = cgpa;
    }

    public float getCreditsLimit() {
        return creditsLimit;
    }

    public void setCreditsLimit(float creditsLimit) {
        this.lastSavedValues.putIfAbsent("credit_limit", String.valueOf(this.creditsLimit));
        this.creditsLimit = creditsLimit;
    }

    public String getAdvisor() {
        return advisor;
    }

    public void setAdvisor(String advisor) {
        this.lastSavedValues.putIfAbsent("advisor", this.advisor);
        this.advisor = advisor;
    }

    public boolean getIsSaved() {
        return this.isSaved;
    }
    public void setIsSaved(boolean isSaved) {
        this.isSaved = isSaved;
    }

    @Override
    public boolean save() {
        if (!this.isSaved) {
            try {
                int numRowInserted;
                PreparedStatement preparedStatement = this.connection.prepareStatement(properties.getProperty("insert"));
                preparedStatement.setString(1, this.entryNumber);
                preparedStatement.setString(2, this.name);
                preparedStatement.setString(3, this.email);
                preparedStatement.setString(4, this.phone);
                preparedStatement.setString(5, this.departmentCode);
                preparedStatement.setInt(6, this.entryYear);
                preparedStatement.setString(7, this.address);
                preparedStatement.setString(8, this.program);
                preparedStatement.setFloat(9, this.cgpa);
                preparedStatement.setFloat(10, this.creditsLimit);
                preparedStatement.setString(11, this.advisor);
                numRowInserted = preparedStatement.executeUpdate();
                if (numRowInserted == 0) {
                    throw new Exception("Insertion failed");
                }
                this.isSaved = true;
                return true;

            } catch (Exception e) {
                return false;
            }
        } else {
            try {
                int numRowUpdated;
                PreparedStatement preparedStatement = this.connection.prepareStatement(properties.getProperty("update"));
                preparedStatement.setString(1, this.entryNumber);
                preparedStatement.setString(2, this.name);
                preparedStatement.setString(3, this.email);
                preparedStatement.setString(4, this.phone);
                preparedStatement.setString(5, this.departmentCode);
                preparedStatement.setInt(6, this.entryYear);
                preparedStatement.setString(7, this.address);
                preparedStatement.setString(8, this.program);
                preparedStatement.setFloat(9, this.cgpa);
                preparedStatement.setFloat(10, this.creditsLimit);
                preparedStatement.setString(11, this.advisor);
                preparedStatement.setString(12, this.lastSavedValues.get("entry_no"));
                numRowUpdated = preparedStatement.executeUpdate();
                if (numRowUpdated == 0) {
                    throw new Exception("No rows affected");
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
    @Override
    public boolean delete() {
        try {
            int numAffectedRows;
            PreparedStatement preparedStatement = this.connection.prepareStatement(properties.getProperty("delete"));
            preparedStatement.setString(1, this.entryNumber);
            numAffectedRows = preparedStatement.executeUpdate();
            if (numAffectedRows == 0) {
                throw new Exception("No rows affected");
            }
            this.isSaved = false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
