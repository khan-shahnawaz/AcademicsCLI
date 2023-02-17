package models;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

/**
 * This class is used to maintain Academic Calendar.
 *
 * @author Shahnawaz Khan
 * @version 1.0
 * @since 2023-02-15
 */

public class AcademicCalender extends BaseModel {
    private static final String PROPERTIES_FILE;
    private static Properties properties;

    static {
        PROPERTIES_FILE = "academic_calender.properties";
        try {
            properties = new Properties();
            ClassLoader classLoader = AcademicCalender.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(PROPERTIES_FILE);
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int year;
    private String semester;
    private String event;
    private String startDate;
    private String endDate;

    private static void fillDetails(ResultSet resultSet, AcademicCalender academicCalender) throws Exception {
        academicCalender.setYear(resultSet.getInt("year"));
        academicCalender.setSemester(resultSet.getString("semester"));
        academicCalender.setEvent(resultSet.getString("event"));
        academicCalender.setStartDate(resultSet.getString("start_date"));
        academicCalender.setEndDate(resultSet.getString("end_date"));
    }

    public static AcademicCalender retrieve(int year, String semester, String event) {
        try {
            AcademicCalender academicCalender = new AcademicCalender();
            PreparedStatement preparedStatement = AcademicCalender.connection.prepareStatement(properties.getProperty("select"));
            preparedStatement.setInt(1, year);
            preparedStatement.setString(2, semester);
            preparedStatement.setString(3, event);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                fillDetails(resultSet, academicCalender);
                academicCalender.setIsSaved(true);
                return academicCalender;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<AcademicCalender> retrieveAll() {
        try {
            ArrayList<AcademicCalender> academicCalenders = new ArrayList<>();
            PreparedStatement preparedStatement = AcademicCalender.connection.prepareStatement(properties.getProperty("selectAll"));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                AcademicCalender academicCalender = new AcademicCalender();
                fillDetails(resultSet, academicCalender);
                academicCalender.setIsSaved(true);
                academicCalenders.add(academicCalender);
            }
            return academicCalenders;
        } catch (Exception e) {
            return null;
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.lastSavedValues.putIfAbsent("year", String.valueOf(year));
        this.year = year;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.lastSavedValues.putIfAbsent("semester", semester);
        this.semester = semester;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.lastSavedValues.putIfAbsent("event", event);
        this.event = event;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.lastSavedValues.putIfAbsent("start_date", startDate);
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.lastSavedValues.putIfAbsent("end_date", endDate);
        this.endDate = endDate;
    }

    protected void putValues(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setInt(1, this.year);
        preparedStatement.setString(2, this.semester);
        preparedStatement.setString(3, this.event);
        preparedStatement.setString(4, this.startDate);
        preparedStatement.setString(5, this.endDate);
    }

    protected void putConditions(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setInt(6, Integer.parseInt(this.lastSavedValues.get("year")));
        preparedStatement.setString(7, this.lastSavedValues.get("semester"));
        preparedStatement.setString(8, this.lastSavedValues.get("event"));
    }

    protected void updateLastSavedValues() {
        this.lastSavedValues.put("year", String.valueOf(this.year));
        this.lastSavedValues.put("semester", this.semester);
        this.lastSavedValues.put("event", this.event);
        this.lastSavedValues.put("start_date", this.startDate);
        this.lastSavedValues.put("end_date", this.endDate);
    }

    protected void prepareDeleteStatement(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setInt(1, this.year);
        preparedStatement.setString(2, this.semester);
        preparedStatement.setString(3, this.event);
    }
}