package by.chmut.giftit.dao;

public class DaoException extends Exception {

    private static final long serialVersionUID = -3760861607605184650L;

    public DaoException() {
    }

    public DaoException(String message) {
        super(message);
    }

    public DaoException(Exception e) {
        super(e);
    }

    public DaoException(String message, Exception e) {
        super(message, e);
    }
}
