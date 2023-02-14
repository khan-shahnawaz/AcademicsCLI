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
    protected static Connection connection;
    protected static String PROPERTIES_FILE = "base.properties";
    protected boolean isSaved;
    protected final HashMap<String, String> lastSavedValues;

    public BaseModel() {

        BaseModel.connection = DBConnectionSingleton.getConnection();
        this.lastSavedValues = new HashMap<>();
        this.isSaved = false;
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

    public boolean save() {
        if (!this.isSaved) {
            try {
                int numRowInserted;
                PreparedStatement preparedStatement = BaseModel.connection.prepareStatement(this.getProperties().getProperty("insert"));
                System.out.println(PROPERTIES_FILE);
                System.out.println(this.getClass());
                putValues(preparedStatement);
                System.out.println(preparedStatement.toString()+this.getClass());
                numRowInserted = preparedStatement.executeUpdate();
                this.updateLastSavedValues();
                System.out.println(numRowInserted);
                if (numRowInserted == 0) {
                    throw new Exception("Insertion failed");
                }
                this.isSaved = true;
                System.out.println("Inserted Successfully");
                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            try {
                int numRowUpdated;
                PreparedStatement preparedStatement = connection.prepareStatement(this.getProperties().getProperty("update"));
                this.putValues(preparedStatement);
                this.putConditions(preparedStatement);
                System.out.println(preparedStatement.toString());
                numRowUpdated = preparedStatement.executeUpdate();
                this.updateLastSavedValues();
                if (numRowUpdated == 0) {
                    throw new Exception("No rows affected");
                }
                System.out.println("Updated Successfully");
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    abstract protected void prepareDeleteStatement(PreparedStatement preparedStatement) throws Exception;
    abstract protected void updateLastSavedValues();

    @Override
    public boolean delete() {
        this.save();
        try {
            int numAffectedRows;
            PreparedStatement preparedStatement = BaseModel.connection.prepareStatement(this.getProperties().getProperty("delete"));
            System.out.println(this.getProperties().getProperty("delete")+"ll");
            this.prepareDeleteStatement(preparedStatement);
            System.out.println(preparedStatement.toString()+this.getProperties().getProperty("delete")+"ll");
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
