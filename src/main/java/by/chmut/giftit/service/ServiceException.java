package by.chmut.giftit.service;

/**
 * The Service exception class provides the creation
 * of exceptions occurring at the application level Service.
 *
 * @author Sergei Chmut.
 */
public class ServiceException extends Exception {

    /**
     * Instantiates a new Service exception.
     */
    public ServiceException() {
    }

    /**
     * Instantiates a new Service exception.
     *
     * @param message exception message
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Service exception.
     *
     * @param exception the another exception
     */
    public ServiceException(Exception exception) {
        super(exception);
    }

    /**
     * Instantiates a new Service exception.
     *
     * @param message   exception message
     * @param exception another exception
     */
    public ServiceException(String message, Exception exception) {
        super(message, exception);
    }
}
