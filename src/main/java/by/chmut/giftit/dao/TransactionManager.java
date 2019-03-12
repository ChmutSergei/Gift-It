package by.chmut.giftit.dao;

import by.chmut.giftit.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

public class TransactionManager {

    private Connection connection;

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

    public void endTransaction(Dao dao) throws DaoException {
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
