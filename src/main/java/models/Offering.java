package models;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * This class represents a course offering.
 *
 * @author Shahnawaz Khan
 * @version 1.0
 * @since 2023-02-15
 */
public class Offering extends BaseModel {
    private static final String PROPERTIES_FILE;
    private static Properties properties;

    static {
        PROPERTIES_FILE = "offering.properties";
        try {
            properties = new Properties();
            ClassLoader classLoader = Offering.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(PROPERTIES_FILE);
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int id;
    private String code;
    private String semester;
    private int year;
    private int section;
    private String status;
    private String department;
    private String coordinator;
    private float minCGPA;

    private static void fillDetails(Offering offering, ResultSet resultSet) {
        try {
            offering.setId(resultSet.getInt("id"));
            offering.setCode(resultSet.getString("code"));
            offering.setSemester(resultSet.getString("semester"));
            offering.setYear(resultSet.getInt("year"));
            offering.setSection(resultSet.getInt("section"));
            offering.setStatus(resultSet.getString("status"));
            offering.setDepartment(resultSet.getString("department"));
            offering.setCoordinator(resultSet.getString("coordinator"));
            offering.setMinCGPA(resultSet.getFloat("min_cgpa"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Offering retrieve(String code, String semester, int year, int section) throws Exception {
        Offering offering = new Offering();
        PreparedStatement preparedStatement = Offering.connection.prepareStatement(properties.getProperty("select"));
        preparedStatement.setString(1, code);
        preparedStatement.setString(2, semester);
        preparedStatement.setInt(3, year);
        preparedStatement.setInt(4, section);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Offering.fillDetails(offering, resultSet);
            offering.setIsSaved(true);
            return offering;
        }
        return null;
    }

    public static ArrayList<Offering> retrieveAll() throws Exception {
        ArrayList<Offering> offerings = new ArrayList<>();
        PreparedStatement preparedStatement = Offering.connection.prepareStatement(properties.getProperty("selectAll"));
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Offering offering = new Offering();
            fillDetails(offering, resultSet);
            offering.setIsSaved(true);
            offerings.add(offering);
        }
        return offerings;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.lastSavedValues.putIfAbsent("code", code);
        this.code = code;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.lastSavedValues.putIfAbsent("semester", semester);
        this.semester = semester;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.lastSavedValues.putIfAbsent("year", String.valueOf(year));
        this.year = year;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.lastSavedValues.putIfAbsent("section", String.valueOf(section));
        this.section = section;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.lastSavedValues.putIfAbsent("status", status);
        this.status = status;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.lastSavedValues.putIfAbsent("department", department);
        this.department = department;
    }

    public String getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(String coordinator) {
        this.lastSavedValues.putIfAbsent("coordinator", coordinator);
        this.coordinator = coordinator;
    }

    public float getMinCGPA() {
        return minCGPA;
    }

    public void setMinCGPA(float minCGPA) {
        this.lastSavedValues.putIfAbsent("min_cgpa", String.valueOf(minCGPA));
        this.minCGPA = minCGPA;
    }

    protected void putValues(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setString(1, this.code);
        preparedStatement.setString(2, this.semester);
        preparedStatement.setInt(3, this.year);
        preparedStatement.setInt(4, this.section);
        preparedStatement.setString(5, this.status);
        preparedStatement.setString(6, this.department);
        preparedStatement.setString(7, this.coordinator);
        preparedStatement.setFloat(8, this.minCGPA);
    }

    protected void putConditions(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setInt(9, this.id);
    }

    protected void updateLastSavedValues() {
        this.lastSavedValues.put("code", this.code);
        this.lastSavedValues.put("semester", this.semester);
        this.lastSavedValues.put("year", String.valueOf(this.year));
        this.lastSavedValues.put("section", String.valueOf(this.section));
        this.lastSavedValues.put("status", this.status);
        this.lastSavedValues.put("department", this.department);
        this.lastSavedValues.put("coordinator", this.coordinator);
        this.lastSavedValues.put("min_cgpa", String.valueOf(this.minCGPA));
    }

    @Override
    protected void prepareDeleteStatement(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setInt(1, this.id);
    }

    @Override
    public String save() {
        Offering insertedOffering;
        if (!this.isSaved) {
            try {
                int numRowInserted;
                PreparedStatement preparedStatement = BaseModel.connection.prepareStatement(this.getProperties().getProperty("insert"));
                putValues(preparedStatement);
                numRowInserted = preparedStatement.executeUpdate();
                this.updateLastSavedValues();
                insertedOffering = Offering.retrieve(this.code, this.semester, this.year, this.section);
                if (insertedOffering == null) {
                    throw new Exception("Insertion failed");
                }
                this.id = insertedOffering.getId();
                if (numRowInserted == 0) {
                    throw new Exception("Insertion failed");
                }
                this.isSaved = true;
                return "00000";

            } catch (SQLException e) {
                return e.getSQLState();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                int numRowUpdated;
                PreparedStatement preparedStatement = connection.prepareStatement(this.getProperties().getProperty("update"));
                this.putValues(preparedStatement);
                this.putConditions(preparedStatement);
                numRowUpdated = preparedStatement.executeUpdate();
                this.updateLastSavedValues();
                insertedOffering = Offering.retrieve(this.code, this.semester, this.year, this.section);
                if (insertedOffering == null) {
                    throw new Exception("No rows affected");
                }
                this.id = insertedOffering.getId();
                if (numRowUpdated == 0) {
                    throw new Exception("No rows affected");
                }
                return "00000";
            } catch (SQLException e) {
                return e.getSQLState();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
