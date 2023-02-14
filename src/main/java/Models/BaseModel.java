package Models;

import DatabaseAccess.DAOInterface;
import DatabaseAccess.DBConnectionSingleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Properties;

/**
 * This class is the base model.
 * @author Shahnawaz Khan
 * @version 1.0
 * @since 2023-02-13
 */
public abstract class BaseModel implements DAOInterface {
    protected static Properties properties;
    protected static Connection connection;
    protected static String PROPERTIES_FILE = "base.properties";
    protected boolean isSaved;
    protected final HashMap<String, String> lastSavedValues;

    public BaseModel() {

        BaseModel.connection = DBConnectionSingleton.getConnection();
        this.lastSavedValues = new HashMap<>();
        this.isSaved = false;
    }

    public boolean getIsSaved() {
        return this.isSaved;
    }

    public void setIsSaved(boolean isSaved) {
        this.isSaved = isSaved;
    }

    protected abstract void putValues(PreparedStatement preparedStatement) throws Exception;
    protected abstract void putConditions(PreparedStatement preparedStatement) throws Exception;

    public boolean save() {
        if (!this.isSaved) {
            try {
                int numRowInserted;
                PreparedStatement preparedStatement = BaseModel.connection.prepareStatement(properties.getProperty("insert"));
                putValues(preparedStatement);
                numRowInserted = preparedStatement.executeUpdate();
                if (numRowInserted == 0) {
                    throw new Exception("Insertion failed");
                }
                this.isSaved = true;
                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            try {
                int numRowUpdated;
                PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty("update"));
                this.putValues(preparedStatement);
                this.putConditions(preparedStatement);
                numRowUpdated = preparedStatement.executeUpdate();

                if (numRowUpdated == 0) {
                    throw new Exception("No rows affected");
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    abstract protected void prepareDeleteStatement(PreparedStatement preparedStatement) throws Exception;

    @Override
    public boolean delete() {
        this.save();
        try {
            int numAffectedRows;
            PreparedStatement preparedStatement = Student.connection.prepareStatement(properties.getProperty("delete"));
            this.prepareDeleteStatement(preparedStatement);
            numAffectedRows = preparedStatement.executeUpdate();
            if (numAffectedRows == 0) {
                throw new Exception("No rows affected");
            }
            this.isSaved = false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
