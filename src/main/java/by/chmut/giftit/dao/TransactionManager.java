package by.chmut.giftit.dao;

import by.chmut.giftit.connectionpool.ConnectionPool;
import by.chmut.giftit.service.ServiceException;

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
                connection.close();
                connection = null;
            }
        } catch (SQLException exception) {
            rollback();
            throw new DaoException("Error when try to finish transaction", exception);
        }
    }
//TODO
    public void rollback() throws DaoException {
        try {
            if (connection != null) {
                connection.rollback();
            } else {
                throw new DaoException("Rollback Error - connection missing");
            }
        } catch (SQLException exception) {
            throw new DaoException("Rollback Error", exception);
        }
    }

}
