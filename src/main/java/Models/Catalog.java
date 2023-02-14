package Models;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

/**
 * This class represents a catalog.
 * @author Shahnawaz Khan
 * @version 1.0
 * @since 2023-02-12
 */

public class Catalog extends BaseModel {
    private String code;
    private String name;
    private String description;
    private float credits;
    private float l;
    private float t;
    private float p;
    private float s;
    private float c;


    static {
        PROPERTIES_FILE = "catalog.properties";
        try {
            properties = new Properties();
            ClassLoader classLoader = Student.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(PROPERTIES_FILE);
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.lastSavedValues.putIfAbsent("code", code);
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.lastSavedValues.putIfAbsent("name", name);
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.lastSavedValues.putIfAbsent("description", description);
        this.description = description;
    }

    public float getCredits() {
        return credits;
    }

    public void setCredits(float credits) {
        this.lastSavedValues.putIfAbsent("credits", String.valueOf(credits));
        this.credits = credits;
    }

    public float getL() {
        return l;
    }

    public void setL(float l) {
        this.lastSavedValues.putIfAbsent("l", String.valueOf(l));
        this.l = l;
    }

    public float getT() {
        return t;
    }

    public void setT(float t) {
        this.lastSavedValues.putIfAbsent("t", String.valueOf(t));
        this.t = t;
    }

    public float getP() {
        return p;
    }

    public void setP(float p) {
        this.lastSavedValues.putIfAbsent("p", String.valueOf(p));
        this.p = p;
    }

    public float getS() {
        return s;
    }

    public void setS(float s) {
        this.lastSavedValues.putIfAbsent("s", String.valueOf(s));
        this.s = s;
    }

    public float getC() {
        return c;
    }

    public void setC(float c) {
        this.lastSavedValues.putIfAbsent("c", String.valueOf(c));
        this.c = c;
    }

    protected void putValues(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setString(1, this.code);
        preparedStatement.setString(2, this.name);
        preparedStatement.setString(3, this.description);
        preparedStatement.setFloat(4, this.credits);
        preparedStatement.setFloat(5, this.l);
        preparedStatement.setFloat(6, this.t);
        preparedStatement.setFloat(7, this.p);
        preparedStatement.setFloat(8, this.s);
        preparedStatement.setFloat(9, this.c);
    }

    protected void putConditions(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setString(10, this.code);
    }

    @Override
    protected void prepareDeleteStatement(PreparedStatement preparedStatement) throws Exception {
        preparedStatement.setString(1, this.code);
    }

    private static void fillDetails(Catalog catalog, ResultSet resultSet) throws Exception {
        catalog.setCode(resultSet.getString("code"));
        catalog.setName(resultSet.getString("name"));
        catalog.setDescription(resultSet.getString("description"));
        catalog.setCredits(resultSet.getFloat("credits"));
        catalog.setL(resultSet.getFloat("l"));
        catalog.setT(resultSet.getFloat("t"));
        catalog.setP(resultSet.getFloat("p"));
        catalog.setS(resultSet.getFloat("s"));
        catalog.setC(resultSet.getFloat("c"));
    }

    public static Catalog retrieve(String code) {
        try {
            Catalog catalog = new Catalog();
            PreparedStatement preparedStatement = Catalog.connection.prepareStatement(properties.getProperty("select"));
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                fillDetails(catalog, resultSet);
                catalog.setIsSaved(true);
                return catalog;
            } else {
                throw new Exception("No rows found");
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static ArrayList<Catalog> retrieveAll() {
        try {
            ArrayList<Catalog> catalogs = new ArrayList<>();
            Catalog catalog;
            PreparedStatement preparedStatement = Catalog.connection.prepareStatement(properties.getProperty("selectAll"));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                catalog = new Catalog();
                fillDetails(catalog, resultSet);
                catalog.setIsSaved(true);
                catalogs.add(catalog);
            }
            return catalogs;
        } catch (Exception e) {
            return null;
        }
    }


}
