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
public class Prerequisite extends BaseModel {
    private static final String PROPERTIES_FILE;
    private static Properties properties;

    static {
        PROPERTIES_FILE = "prerequisite.properties";
        try {
            properties = new Properties();
            ClassLoader classLoader = Prerequisite.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(PROPERTIES_FILE);
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int id;
    private String prerequisiteCode;
    private String minGrade;

    private static void fillDetails(Prerequisite prerequisite, ResultSet resultSet) throws Exception {
        prerequisite.setId(resultSet.getInt("id"));
        prerequisite.setPrerequisiteCode(resultSet.getString("prereq"));
        prerequisite.setMinGrade(resultSet.getString("min_grade"));
    }

    public static Prerequisite retrieve(int id, String prerequisiteCode) {
        try {
            Prerequisite prerequisite = new Prerequisite();
            PreparedStatement preparedStatement = Prerequisite.connection.prepareStatement(properties.getProperty("select"));
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, prerequisiteCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                fillDetails(prerequisite, resultSet);
                prerequisite.setIsSaved(true);
                return prerequisite;
            } else {
                throw new Exception("No rows found");
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static ArrayList<Prerequisite> retrieveAll() {
        try {
            ArrayList<Prerequisite> prerequisites = new ArrayList<>();
            Prerequisite prerequisite;
            PreparedStatement preparedStatement = Prerequisite.connection.prepareStatement(properties.getProperty("selectAll"));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                prerequisite = new Prerequisite();
                fillDetails(prerequisite, resultSet);
                prerequisite.setIsSaved(true);
                prerequisites.add(prerequisite);
            }
            return prerequisites;
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

    public String getPrerequisiteCode() {
        return prerequisiteCode;
    }

    public void setPrerequisiteCode(String prerequisiteCode) {
        this.lastSavedValues.putIfAbsent("prereq", prerequisiteCode);
        this.prerequisiteCode = prerequisiteCode;
    }

    public String getMinGrade() {
        return minGrade;
    }

    public void setMinGrade(String minGrade) {
        this.lastSavedValues.putIfAbsent("min_grade", minGrade);
        this.minGrade = minGrade;
    }

    protected void putValues(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setInt(1, this.id);
        preparedStatement.setString(2, this.prerequisiteCode);
        preparedStatement.setString(3, this.minGrade);
    }

    protected void putConditions(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setInt(4, this.id);
        preparedStatement.setString(5, this.prerequisiteCode);
    }

    protected void updateLastSavedValues() {
        this.lastSavedValues.put("id", String.valueOf(this.id));
        this.lastSavedValues.put("prereq", this.prerequisiteCode);
        this.lastSavedValues.put("min_grade", this.minGrade);
    }

    protected void prepareDeleteStatement(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setInt(1, this.id);
        preparedStatement.setString(2, this.prerequisiteCode);
    }
}
