package Models;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

/**
 * This class represents an entry in the table teaching_team.
 *
 * @author Shahnawaz Khan
 * @version 1.0
 * @since 2023-02-15
 */
public class TeachingTeam extends BaseModel {
    private static final String PROPERTIES_FILE;
    private static Properties properties;

    static {
        PROPERTIES_FILE = "teaching_team.properties";
        try {
            properties = new Properties();
            ClassLoader classLoader = TeachingTeam.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(PROPERTIES_FILE);
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int id;
    private String instructor;
    private boolean isCoordinator;

    private static void fillDetails(TeachingTeam teachingTeam, ResultSet resultSet) throws Exception {
        teachingTeam.setId(resultSet.getInt("id"));
        teachingTeam.setInstructor(resultSet.getString("instructor"));
        teachingTeam.setCoordinator(resultSet.getBoolean("is_coordinator"));
    }

    public static TeachingTeam retrieve(int id, String instructor) {
        try {
            TeachingTeam teachingTeam = new TeachingTeam();
            PreparedStatement preparedStatement = TeachingTeam.connection.prepareStatement(properties.getProperty("select"));
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, instructor);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                fillDetails(teachingTeam, resultSet);
                teachingTeam.setIsSaved(true);
                return teachingTeam;
            } else {
                throw new Exception("No rows found");
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static ArrayList<TeachingTeam> retrieveAll() {
        try {
            ArrayList<TeachingTeam> teachingTeams = new ArrayList<>();
            TeachingTeam teachingTeam;
            PreparedStatement preparedStatement = TeachingTeam.connection.prepareStatement(properties.getProperty("selectAll"));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                teachingTeam = new TeachingTeam();
                fillDetails(teachingTeam, resultSet);
                teachingTeam.setIsSaved(true);
                teachingTeams.add(teachingTeam);
            }
            return teachingTeams;
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

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.lastSavedValues.putIfAbsent("instructor", instructor);
        this.instructor = instructor;
    }

    public boolean isCoordinator() {
        return isCoordinator;
    }

    public void setCoordinator(boolean coordinator) {
        this.lastSavedValues.putIfAbsent("is_coordinator", String.valueOf(coordinator));
        isCoordinator = coordinator;
    }

    protected void putValues(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setInt(1, this.id);
        preparedStatement.setString(2, this.instructor);
        preparedStatement.setBoolean(3, this.isCoordinator);
    }

    protected void putConditions(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setInt(4, Integer.parseInt(this.lastSavedValues.get("id")));
        preparedStatement.setString(5, this.lastSavedValues.get("instructor"));
    }

    protected void updateLastSavedValues() {
        this.lastSavedValues.put("id", String.valueOf(this.id));
        this.lastSavedValues.put("instructor", this.instructor);
        this.lastSavedValues.put("is_coordinator", String.valueOf(this.isCoordinator));
    }

    protected void prepareDeleteStatement(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setInt(1, this.id);
        preparedStatement.setString(2, this.instructor);
    }

}
