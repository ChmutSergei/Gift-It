package by.chmut.giftit.dao;

/**
 * The DaoException class provides the creation
 * of exceptions occurring at the application level Dao.
 *
 * @author Sergei Chmut.
 */
public class DaoException extends Exception {

    /**
     * Instantiates a new DaoException.
     */
    public DaoException() {
    }

    /**
     * Instantiates a new DaoException.
     *
     * @param message exception message
     */
    public DaoException(String message) {
        super(message);
    }

    /**
     * Instantiates a new DaoException.
     *
     * @param exception another exception
     */
    public DaoException(Exception exception) {
        super(exception);
    }

    /**
     * Instantiates a new DaoException.
     *
     * @param message   exception message
     * @param exception another exception
     */
    public DaoException(String message, Exception exception) {
        super(message, exception);
    }
}
