package by.chmut.giftit.dao;

import by.chmut.giftit.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * The Transaction manager class provides
 * transaction support in the application.
 *
 * @author Sergei Chmut.
 */
public class TransactionManager {

    /**
     * The Connection for transaction.
     */
    private Connection connection;

    /**
     * Begin transaction.
     *
     * @param daos the objects of dao that are involved in the transaction
     * @throws DaoException if transaction can't be begin
     */
    public void beginTransaction(Dao... daos) throws DaoException {
        if (daos.length == 0) {
            throw new DaoException("Not found dao instance for begin transaction");
        }
        if (connection == null) {
            connection = ConnectionPool.getInstance().takeConnection();
        }
        try {
            connection.setAutoCommit(false);
            Arrays.stream(daos).forEach(dao -> dao.setConnection(connection));
        } catch (SQLException exception) {
            throw new DaoException("Error try to begin transaction", exception);
        }
    }

    /**
     * End transaction.
     *
     * @throws DaoException if transaction can't be finished
     */
    public void endTransaction() throws DaoException {
        try {
            if (connection != null) {
                connection.commit();
                connection.setAutoCommit(true);
            }
        } catch (SQLException exception) {
            rollback();
            throw new DaoException("Error when try to finish transaction", exception);
        }
        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException exception) {
            throw new DaoException("Error when try to finish transaction", exception);
        }
    }

    /**
     * Rollback transaction.
     *
     * @throws DaoException if rollback can't be handled
     */
    public void rollback() throws DaoException {
        try {
            if (connection != null) {
                connection.rollback();
                connection.close();
                connection = null;
            }
        } catch (SQLException exception) {
            throw new DaoException("Rollback Error - connection missing", exception);
        }
    }

}
