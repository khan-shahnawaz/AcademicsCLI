package models;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

/**
 * This class represents a curriculum.
 *
 * @author Shahnawaz Khan
 * @version 1.0
 * @since 2023-02-15
 */
public class Curriculum extends BaseModel {
    private static final String PROPERTIES_FILE;
    private static Properties properties;

    static {
        PROPERTIES_FILE = "curriculum.properties";
        try {
            properties = new Properties();
            ClassLoader classLoader = Curriculum.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(PROPERTIES_FILE);
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String program;
    private String courseType;
    private float minCredits;

    public static void fillDetails(Curriculum curriculum, ResultSet resultSet) {
        try {
            curriculum.setProgram(resultSet.getString("program"));
            curriculum.setCourseType(resultSet.getString("course_type"));
            curriculum.setMinCredits(resultSet.getFloat("min_credits"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Curriculum retrieve(String program, String courseType) throws Exception {
        Curriculum curriculum = new Curriculum();
        PreparedStatement preparedStatement = Curriculum.connection.prepareStatement(properties.getProperty("select"));
        preparedStatement.setString(1, program);
        preparedStatement.setString(2, courseType);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            fillDetails(curriculum, resultSet);
            return curriculum;
        }
        return null;

    }

    public static ArrayList<Curriculum> retrieveAll() throws Exception {
        ArrayList<Curriculum> curriculums = new ArrayList<>();
        PreparedStatement preparedStatement = Curriculum.connection.prepareStatement(properties.getProperty("selectAll"));
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Curriculum curriculum = new Curriculum();
            fillDetails(curriculum, resultSet);
            curriculums.add(curriculum);
        }
        return curriculums;
    }

    public Properties getProperties() {
        return properties;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.lastSavedValues.putIfAbsent("program", program);
        this.program = program;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.lastSavedValues.putIfAbsent("course_type", courseType);
        this.courseType = courseType;
    }

    public float getMinCredits() {
        return minCredits;
    }

    public void setMinCredits(float minCredits) {
        this.lastSavedValues.putIfAbsent("min_credits", String.valueOf(minCredits));
        this.minCredits = minCredits;
    }

    protected void putValues(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setString(1, this.program);
        preparedStatement.setString(2, this.courseType);
        preparedStatement.setFloat(3, this.minCredits);
    }

    protected void putConditions(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setString(4, this.program);
        preparedStatement.setString(5, this.courseType);
    }

    protected void updateLastSavedValues() {
        this.lastSavedValues.put("program", this.program);
        this.lastSavedValues.put("course_type", this.courseType);
        this.lastSavedValues.put("min_credits", String.valueOf(this.minCredits));
    }

    @Override
    protected void prepareDeleteStatement(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setString(1, this.program);
        preparedStatement.setString(2, this.courseType);
    }
}
