package group4.group4.Exceptions;

import java.sql.SQLException;

public class DaoException extends SQLException {
    public DaoException() {

    }
    public DaoException(String message) {
        super(message);
    }

    // Constructor that accepts both a message and a cause
    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
