package models;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

/**
 * This class represents a student.
 *
 * @author Shahnawaz Khan
 * @version 1.0
 * @since 2023-02-12
 */

public class Student extends BaseModel {
    private static final String PROPERTIES_FILE;
    private static Properties properties;

    static {
        PROPERTIES_FILE = "student.properties";
        try {
            properties = new Properties();
            ClassLoader classLoader = Student.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(PROPERTIES_FILE);
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    private static void fillDetails(Student student, ResultSet resultSet) throws Exception {
        student.setEntryNumber(resultSet.getString("entry_no"));
        student.setName(resultSet.getString("name"));
        student.setEmail(resultSet.getString("email"));
        student.setPhone(resultSet.getString("phone"));
        student.setDepartmentCode(resultSet.getString("department"));
        student.setEntryYear(resultSet.getInt("entry_year"));
        student.setAddress(resultSet.getString("address"));
        student.setProgram(resultSet.getString("program"));
        student.setCgpa(resultSet.getFloat("cgpa"));
        student.setCreditsLimit(resultSet.getFloat("credit_limit"));
        student.setAdvisor(resultSet.getString("advisor"));
    }

    public static Student retrieve(String entryNumber) throws Exception {
        Student student = new Student();
        PreparedStatement preparedStatement = Student.connection.prepareStatement(properties.getProperty("select"));
        preparedStatement.setString(1, entryNumber);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            fillDetails(student, resultSet);
            student.setIsSaved(true);
            return student;
        }
        return null;
    }

    public static ArrayList<Student> retrieveAll() throws Exception {
        ArrayList<Student> students = new ArrayList<>();
        Student student;
        PreparedStatement preparedStatement = Student.connection.prepareStatement(properties.getProperty("selectAll"));
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            student = new Student();
            fillDetails(student, resultSet);
            student.setIsSaved(true);
            students.add(student);
        }
        return students;
    }

    public Properties getProperties() {
        return properties;
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

    protected void putValues(PreparedStatement preparedStatement) throws Exception {
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
    }

    protected void putConditions(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setString(12, this.lastSavedValues.get("entry_no"));
    }

    protected void updateLastSavedValues() {
        this.lastSavedValues.put("entry_no", this.entryNumber);
        this.lastSavedValues.put("name", this.name);
        this.lastSavedValues.put("email", this.email);
        this.lastSavedValues.put("phone", this.phone);
        this.lastSavedValues.put("department", this.departmentCode);
        this.lastSavedValues.put("entry_year", String.valueOf(this.entryYear));
        this.lastSavedValues.put("address", this.address);
        this.lastSavedValues.put("program", this.program);
        this.lastSavedValues.put("cgpa", String.valueOf(this.cgpa));
        this.lastSavedValues.put("credit_limit", String.valueOf(this.creditsLimit));
        this.lastSavedValues.put("advisor", this.advisor);
    }

    protected void prepareDeleteStatement(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setString(1, this.entryNumber);
    }
}
