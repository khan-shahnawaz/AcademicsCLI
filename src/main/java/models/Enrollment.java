package models;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

/**
 * This class represents the model for enrollment.
 *
 * @author Shahnawaz Khan
 * @version 1.0
 * @since 2023-02-15
 */
public class Enrollment extends BaseModel {
    private static final String PROPERTIES_FILE;
    private static final Properties properties;

    static {
        PROPERTIES_FILE = "enrollment.properties";
        properties = new Properties();
        ClassLoader classLoader = Enrollment.class.getClassLoader();
        try {
            InputStream inputStream = classLoader.getResourceAsStream(PROPERTIES_FILE);
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int id;
    private String entryNo;
    private String grade;
    private String status;
    private String courseType;

    private static void fillDetails(Enrollment enrollment, ResultSet resultSet) throws Exception {
        enrollment.setId(resultSet.getInt("id"));
        enrollment.setEntryNo(resultSet.getString("entry_no"));
        enrollment.setGrade(resultSet.getString("grade"));
        enrollment.setStatus(resultSet.getString("status"));
        enrollment.setCourseType(resultSet.getString("course_type"));
    }

    public static Enrollment retrieve(int id, String entryNo) throws Exception {
        Enrollment enrollment = new Enrollment();
        PreparedStatement preparedStatement = Enrollment.connection.prepareStatement(properties.getProperty("select"));
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, entryNo);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            fillDetails(enrollment, resultSet);
            enrollment.setIsSaved(true);
            return enrollment;
        }
        return null;
    }

    public static ArrayList<Enrollment> retrieveAll() throws Exception {
        ArrayList<Enrollment> enrollments = new ArrayList<>();
        Enrollment enrollment;
        PreparedStatement preparedStatement = Enrollment.connection.prepareStatement(properties.getProperty("selectAll"));
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            enrollment = new Enrollment();
            fillDetails(enrollment, resultSet);
            enrollment.setIsSaved(true);
            enrollments.add(enrollment);
        }
        return enrollments;
    }

    public Properties getProperties() {
        return properties;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.lastSavedValues.putIfAbsent("id", String.valueOf(id));
        this.id = id;
    }

    public String getEntryNo() {
        return entryNo;
    }

    public void setEntryNo(String entryNo) {
        this.lastSavedValues.putIfAbsent("entry_no", entryNo);
        this.entryNo = entryNo;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.lastSavedValues.putIfAbsent("grade", grade);
        this.grade = grade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.lastSavedValues.putIfAbsent("status", status);
        this.status = status;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.lastSavedValues.putIfAbsent("course_type", courseType);
        this.courseType = courseType;
    }

    protected void putValues(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setInt(1, this.id);
        preparedStatement.setString(2, this.entryNo);
        preparedStatement.setString(3, this.grade);
        preparedStatement.setString(4, this.status);
        preparedStatement.setString(5, this.courseType);
    }

    protected void putConditions(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setInt(6, this.id);
        preparedStatement.setString(7, this.entryNo);
    }

    protected void updateLastSavedValues() {
        this.lastSavedValues.put("id", String.valueOf(this.id));
        this.lastSavedValues.put("entry_no", this.entryNo);
        this.lastSavedValues.put("grade", this.grade);
        this.lastSavedValues.put("status", this.status);
        this.lastSavedValues.put("course_type", this.courseType);
    }

    protected void prepareDeleteStatement(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setInt(1, this.id);
        preparedStatement.setString(2, this.entryNo);
    }
}
