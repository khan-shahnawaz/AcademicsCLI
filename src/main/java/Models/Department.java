package Models;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

/**
 * This class represents a Department.
 * @author Shahnawaz Khan
 * @version 1.0
 * @since 2023-02-14
 */

public class Department extends BaseModel {
    private static final String PROPERTIES_FILE;
    private static Properties properties;
    private String id;
    private String name;

    static {
        PROPERTIES_FILE = "department.properties";
        try {
            properties = new Properties();
            ClassLoader classLoader = Department.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(PROPERTIES_FILE);
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.lastSavedValues.putIfAbsent("dep_id", id);
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.lastSavedValues.putIfAbsent("name", name);
        this.name = name;
    }

    protected void putValues(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setString(1, this.id);
        preparedStatement.setString(2, this.name);
    }

    protected void putConditions(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setString(3, this.lastSavedValues.get("dep_id"));
    }

    protected void updateLastSavedValues() {
        this.lastSavedValues.put("dep_id", this.id);
        this.lastSavedValues.put("name", this.name);
    }

    @Override
    protected void prepareDeleteStatement(PreparedStatement preparedStatement) throws Exception {
        System.out.println("Delete"+preparedStatement.toString());
        preparedStatement.setString(1, this.id);
        System.out.println("Delete2"+preparedStatement.toString());
    }

    private static void fillDetails(Department department, ResultSet resultSet) throws Exception {
        department.setId(resultSet.getString("dep_id"));
        department.setName(resultSet.getString("name"));
    }

    public static Department retrieve(String code) {
        try {
            Department department = new Department();
            PreparedStatement preparedStatement = Department.connection.prepareStatement(properties.getProperty("select"));
            preparedStatement.setString(1, code);
            System.out.println(preparedStatement.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                fillDetails(department, resultSet);
                System.out.println(department.getId());
                department.setIsSaved(true);
                return department;
            }
            System.out.println("No result");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Department> retrieveAll() {
        try {
            ArrayList<Department> departments = new ArrayList<>();
            PreparedStatement preparedStatement = Department.connection.prepareStatement(properties.getProperty("selectAll"));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Department department = new Department();
                fillDetails(department, resultSet);
                department.setIsSaved(true);
                departments.add(department);
            }
            return departments;
        } catch (Exception e) {
            return null;
        }
    }
}
