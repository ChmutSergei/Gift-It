package by.chmut.giftit.service;

public class ServiceException extends Exception {

    private static final long serialVersionUID = -1247982146979135909L;

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Exception e) {
        super(e);
    }

    public ServiceException(String message, Exception e) {
        super(message, e);
    }
}
