package Models;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

/**
 * This class stores default prerequisites for the courses in the catalog.
 *
 * @author Shahnawaz Khan
 * @version 1.0
 * @since 2023-02-15
 */
public class DefaultPrerequisite extends BaseModel {
    private static final String PROPERTIES_FILE;
    private static Properties properties;

    static {
        PROPERTIES_FILE = "prerequisite_default.properties";
        try {
            properties = new Properties();
            ClassLoader classLoader = DefaultPrerequisite.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(PROPERTIES_FILE);
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String catalogCode;
    private String prerequisiteCode;
    private String minGrade;

    private static void fillDetails(DefaultPrerequisite defaultPrerequisite, ResultSet resultSet) throws Exception {
        defaultPrerequisite.setCatalogCode(resultSet.getString("code"));
        defaultPrerequisite.setPrerequisiteCode(resultSet.getString("prereq"));
        defaultPrerequisite.setMinGrade(resultSet.getString("min_grade"));
    }

    public static DefaultPrerequisite retrieve(String catalogCode, String prerequisiteCode) {
        try {
            DefaultPrerequisite defaultPrerequisite = new DefaultPrerequisite();
            PreparedStatement preparedStatement = DefaultPrerequisite.connection.prepareStatement(properties.getProperty("select"));
            preparedStatement.setString(1, catalogCode);
            preparedStatement.setString(2, prerequisiteCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                fillDetails(defaultPrerequisite, resultSet);
                defaultPrerequisite.setIsSaved(true);
                return defaultPrerequisite;
            } else {
                throw new Exception("No rows found");
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static ArrayList<DefaultPrerequisite> retrieveAll() {
        try {
            ArrayList<DefaultPrerequisite> defaultPrerequisites = new ArrayList<>();
            DefaultPrerequisite defaultPrerequisite;
            PreparedStatement preparedStatement = DefaultPrerequisite.connection.prepareStatement(properties.getProperty("selectAll"));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                defaultPrerequisite = new DefaultPrerequisite();
                fillDetails(defaultPrerequisite, resultSet);
                defaultPrerequisite.setIsSaved(true);
                defaultPrerequisites.add(defaultPrerequisite);
            }
            return defaultPrerequisites;
        } catch (Exception e) {
            return null;
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public String getCatalogCode() {
        return catalogCode;
    }

    public void setCatalogCode(String catalogCode) {
        this.lastSavedValues.putIfAbsent("code", catalogCode);
        this.catalogCode = catalogCode;
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
        preparedStatement.setString(1, this.catalogCode);
        preparedStatement.setString(2, this.prerequisiteCode);
        preparedStatement.setString(3, this.minGrade);
    }

    protected void putConditions(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setString(4, this.lastSavedValues.get("code"));
        preparedStatement.setString(5, this.lastSavedValues.get("prereq"));
    }

    protected void updateLastSavedValues() {
        this.lastSavedValues.put("code", this.catalogCode);
        this.lastSavedValues.put("prereq", this.prerequisiteCode);
        this.lastSavedValues.put("min_grade", this.minGrade);
    }

    protected void prepareDeleteStatement(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setString(1, this.catalogCode);
        preparedStatement.setString(2, this.prerequisiteCode);
    }
}
