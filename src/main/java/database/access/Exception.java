package database.access;

public class Exception extends Throwable {
    public static int SUCCESS = 0;
    public static int ALREADY_EXISTS = 1;
    public static int UNAUTHORISED = 2;
    public static int UNKNOWN = 3;
    public static int NOT_EXISTS = 4;
    public static int NOT_UPDATED = 5;
    public static int INVALID_VALUES = 6;

    public static Integer handleSQLException(String sqlState, String message) {
        if (sqlState.equalsIgnoreCase("23514")) {
            System.err.println("You do not have permission to perform this action.");
            return UNAUTHORISED;
        }
        if (sqlState.equalsIgnoreCase("23505")) {
            System.err.println("This entry already exists in the database.");
            return ALREADY_EXISTS;
        }
        if (sqlState.equalsIgnoreCase("23503")) {
            System.err.println("The referenced entry does not exist in the database.");
            return NOT_EXISTS;
        }
        if (sqlState.equalsIgnoreCase("23502")) {
            System.err.println("The entry was not updated because it is missing a required field.");
            return NOT_UPDATED;
        }
        if (sqlState.equalsIgnoreCase("22P02")) {
            System.err.println("The operation was not performed because it contains an invalid value.");
            return INVALID_VALUES;
        }

        System.err.println(message);
        return UNKNOWN;
    }


}
