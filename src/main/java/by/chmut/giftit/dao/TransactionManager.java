package by.chmut.giftit.dao;

import by.chmut.giftit.connectionpool.ConnectionPool;

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void endTransaction(Dao dao) {
        try {
            if (connection != null) {
                connection.commit();
                connection.setAutoCommit(true);
                connection.close();
            }
            connection = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
