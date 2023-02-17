package models;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

/**
 * This class represents a course category.
 *
 * @author Shahnawaz Khan
 * @version 1.0
 * @since 2023-02-15
 */

public class CourseCategory extends BaseModel {
    private static final String PROPERTIES_FILE;
    private static Properties properties;

    static {
        PROPERTIES_FILE = "course_category.properties";
        try {
            properties = new Properties();
            ClassLoader classLoader = CourseCategory.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(PROPERTIES_FILE);
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int id;
    private String type;
    private int entryYear;
    private String department;
    private String program;

    private static void fillDetails(CourseCategory courseCategory, ResultSet resultSet) throws Exception {
        courseCategory.setId(resultSet.getInt("id"));
        courseCategory.setType(resultSet.getString("type"));
        courseCategory.setEntryYear(resultSet.getInt("entry_year"));
        courseCategory.setDepartment(resultSet.getString("department"));
        courseCategory.setProgram(resultSet.getString("program"));
    }

    public static CourseCategory retrieve(int id, String type, int entryYear, String department, String program) {
        try {
            CourseCategory courseCategory = new CourseCategory();
            PreparedStatement preparedStatement = CourseCategory.connection.prepareStatement(properties.getProperty("select"));
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, type);
            preparedStatement.setInt(3, entryYear);
            preparedStatement.setString(4, department);
            preparedStatement.setString(5, program);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                fillDetails(courseCategory, resultSet);
                courseCategory.setIsSaved(true);
                return courseCategory;
            } else {
                throw new Exception("No rows found");
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static ArrayList<CourseCategory> retrieveAll() {
        try {
            ArrayList<CourseCategory> courseCategories = new ArrayList<>();
            CourseCategory courseCategory;
            PreparedStatement preparedStatement = CourseCategory.connection.prepareStatement(properties.getProperty("selectAll"));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                courseCategory = new CourseCategory();
                fillDetails(courseCategory, resultSet);
                courseCategory.setIsSaved(true);
                courseCategories.add(courseCategory);
            }
            return courseCategories;
        } catch (Exception e) {
            return null;
        }
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.lastSavedValues.putIfAbsent("type", type);
        this.type = type;
    }

    public Integer getEntryYear() {
        return entryYear;
    }

    public void setEntryYear(int entryYear) {
        this.lastSavedValues.putIfAbsent("entry_year", String.valueOf(entryYear));
        this.entryYear = entryYear;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.lastSavedValues.putIfAbsent("department", department);
        this.department = department;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.lastSavedValues.putIfAbsent("program", program);
        this.program = program;
    }

    protected void putValues(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setInt(1, this.id);
        preparedStatement.setString(2, this.type);
        preparedStatement.setInt(3, this.entryYear);
        preparedStatement.setString(4, this.department);
        preparedStatement.setString(5, this.program);
    }

    protected void putConditions(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setInt(6, Integer.parseInt(this.lastSavedValues.get("id")));
        preparedStatement.setString(7, this.lastSavedValues.get("type"));
        preparedStatement.setInt(8, Integer.parseInt(this.lastSavedValues.get("entry_year")));
        preparedStatement.setString(9, this.lastSavedValues.get("department"));
        preparedStatement.setString(10, this.lastSavedValues.get("program"));
    }

    protected void updateLastSavedValues() {
        this.lastSavedValues.put("id", String.valueOf(this.id));
        this.lastSavedValues.put("type", this.type);
        this.lastSavedValues.put("entry_year", String.valueOf(this.entryYear));
        this.lastSavedValues.put("department", this.department);
        this.lastSavedValues.put("program", this.program);
    }

    protected void prepareDeleteStatement(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setInt(1, this.id);
        preparedStatement.setString(2, this.type);
        preparedStatement.setInt(3, this.entryYear);
        preparedStatement.setString(4, this.department);
        preparedStatement.setString(5, this.program);
    }
}
