package models;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

/**
 * This class represents an Instructor.
 *
 * @author Shahnawaz Khan
 * @version 1.0
 * @since 2023-02-14
 */
public class Instructor extends BaseModel {
    private static final String PROPERTIES_FILE;
    private static Properties properties;

    static {
        PROPERTIES_FILE = "instructor.properties";
        try {
            properties = new Properties();
            ClassLoader classLoader = Instructor.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(PROPERTIES_FILE);
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String name;
    private String email;
    private String phone;
    private String address;
    private String departmentCode;

    private static void fillDetails(Instructor instructor, ResultSet resultSet) throws Exception {
        instructor.setName(resultSet.getString("name"));
        instructor.setEmail(resultSet.getString("email"));
        instructor.setPhone(resultSet.getString("phone"));
        instructor.setAddress(resultSet.getString("address"));
        instructor.setDepartmentCode(resultSet.getString("department"));
    }

    public static Instructor retrieve(String email) throws Exception {
        Instructor instructor = new Instructor();
        PreparedStatement preparedStatement = Instructor.connection.prepareStatement(properties.getProperty("select"));
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            fillDetails(instructor, resultSet);
            instructor.setIsSaved(true);
            return instructor;
        }
        return null;
    }

    public static ArrayList<Instructor> retrieveAll() throws Exception {
        ArrayList<Instructor> instructors = new ArrayList<>();
        PreparedStatement preparedStatement = Instructor.connection.prepareStatement(properties.getProperty("selectAll"));
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Instructor instructor = new Instructor();
            fillDetails(instructor, resultSet);
            instructor.setIsSaved(true);
            instructors.add(instructor);
        }
        return instructors;
    }

    public Properties getProperties() {
        return properties;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.lastSavedValues.putIfAbsent("address", address);
        this.address = address;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.lastSavedValues.putIfAbsent("department", departmentCode);
        this.departmentCode = departmentCode;
    }

    protected void putValues(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setString(1, this.name);
        preparedStatement.setString(2, this.email);
        preparedStatement.setString(3, this.phone);
        preparedStatement.setString(4, this.address);
        preparedStatement.setString(5, this.departmentCode);
    }

    protected void putConditions(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setString(6, this.lastSavedValues.get("email"));
    }

    protected void updateLastSavedValues() {
        this.lastSavedValues.put("name", this.name);
        this.lastSavedValues.put("email", this.email);
        this.lastSavedValues.put("phone", this.phone);
        this.lastSavedValues.put("address", this.address);
        this.lastSavedValues.put("department", this.departmentCode);
    }

    protected void prepareDeleteStatement(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setString(1, this.lastSavedValues.get("email"));
    }
}