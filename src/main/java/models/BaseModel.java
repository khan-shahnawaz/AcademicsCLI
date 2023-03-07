package models;

import database.access.DAOInterface;
import database.access.DBConnectionSingleton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

/**
 * This class is the base model.
 *
 * @author Shahnawaz Khan
 * @version 1.0
 * @since 2023-02-13
 */
public abstract class BaseModel implements DAOInterface {
    protected static Connection connection;
    protected static String PROPERTIES_FILE = "base.properties";

    static {
        BaseModel.connection = DBConnectionSingleton.getConnection();
    }

    protected final HashMap<String, String> lastSavedValues;
    protected boolean isSaved;

    public BaseModel() {
        this.lastSavedValues = new HashMap<>();
        this.isSaved = false;
    }

    public static void refreshConnection() {
        BaseModel.connection = DBConnectionSingleton.restartConnection();
    }

    public abstract Properties getProperties();

    public boolean getIsSaved() {
        return this.isSaved;
    }

    public void setIsSaved(boolean isSaved) {
        this.isSaved = isSaved;
    }

    protected abstract void putValues(PreparedStatement preparedStatement) throws Exception;

    protected abstract void putConditions(PreparedStatement preparedStatement) throws Exception;

    public String save() {
        if (!this.isSaved) {
            try {
                PreparedStatement preparedStatement = BaseModel.connection.prepareStatement(this.getProperties().getProperty("insert"));
                putValues(preparedStatement);
                preparedStatement.executeUpdate();
                this.updateLastSavedValues();
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
                if (numRowUpdated == 0) {
                    throw new SQLException("No rows affected");
                }
                return "00000";

            } catch (SQLException e) {
                return e.getSQLState();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    abstract protected void prepareDeleteStatement(PreparedStatement preparedStatement) throws Exception;

    abstract protected void updateLastSavedValues();

    @Override
    public String delete() {
        this.save();
        try {
            int numAffectedRows;
            PreparedStatement preparedStatement = BaseModel.connection.prepareStatement(this.getProperties().getProperty("delete"));
            this.prepareDeleteStatement(preparedStatement);
            numAffectedRows = preparedStatement.executeUpdate();

            if (numAffectedRows == 0) {
                throw new Exception("No rows affected");
            }
            this.isSaved = false;
            return "00000";
        } catch (SQLException e) {
            return e.getSQLState();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
