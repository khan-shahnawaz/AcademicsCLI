package database.access;

public class Exception extends Throwable {
    public static int SUCCESS = 0;
    public static int ALREADY_EXISTS = 1;
    public static int UNAUTHORISED = 2;
    public static int UNKNOWN = 3;
    public static int NOT_EXISTS = 4;
    public static int NOT_UPDATED = 5;
    public static int INVALID_VALUES = 6;
    public static int INVALID_SESSION = 7;
    public static int NOT_OPEN = 8;
    public static int ENROLMENT_NOT_OPEN = 9;
    public static int NOT_ALLOWED_UPDATE = 10;
    public static int ALREADY_ENROLLED = 11;
    public static int CREDIT_LIMIT_EXCEEDED = 12;
    public static int PREREQ_FAILED = 13;

    public static Integer handleSQLException(String sqlState, String message) {

        /*
        Custom Errors:
        99901: Not defined
    99902: Not open
    99903: enrolment not open
    99900: Not allowed update
    99905: Already Enrolled
    99906: Credit Limit Exceeded
    99907: Not allowed to enroll(prereq)
         */

        if (sqlState.equalsIgnoreCase("42501")) {
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
        if (sqlState.equalsIgnoreCase("99901")) {
            System.err.println("The operation was not performed because the calender for the academic session does not exits for this event.");
            return INVALID_SESSION;
        }
        if (sqlState.equalsIgnoreCase("99902")) {
            System.err.println("The operation was not performed because the event is not open in the calender.");
            return NOT_OPEN;
        }
        if (sqlState.equalsIgnoreCase("99903")) {
            System.err.println("The operation was not performed because enrolment is not open in the calender.");
            return ENROLMENT_NOT_OPEN;
        }
        if (sqlState.equalsIgnoreCase("99900")) {
            System.err.println("The operation was not performed because it is not allowed to update at this moment.");
            return NOT_ALLOWED_UPDATE;
        }
        if (sqlState.equalsIgnoreCase("99905")) {
            System.err.println("The operation was not performed because the student is already enrolled and passed the course.");
            return ALREADY_ENROLLED;
        }
        if (sqlState.equalsIgnoreCase("99906")) {
            System.err.println("The operation was not performed because the student has exceeded the credit limit.");
            return CREDIT_LIMIT_EXCEEDED;
        }
        if (sqlState.equalsIgnoreCase("99907")) {
            System.err.println("The operation was not performed because the student has not obtained the required grade in one of the prerequisite or CGPA criteria not satisfied.");
            return PREREQ_FAILED;
        }

        System.err.println(message);
        return UNKNOWN;
    }


}
