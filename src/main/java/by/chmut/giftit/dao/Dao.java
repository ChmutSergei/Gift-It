package by.chmut.giftit.dao;

import by.chmut.giftit.entity.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface Dao<K, T extends Entity> {

    Logger logger = LogManager.getLogger();

    List<T> findAll() throws DaoException;

    T findEntity(K id) throws DaoException;

    boolean delete(K id) throws DaoException;

    boolean delete(T entity) throws DaoException;

    boolean create(T entity) throws DaoException;

    T update(T entity) throws DaoException;

    void setConnection(Connection connection);

    Connection getConnection();

    default void close(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            logger.error("Error with close Statement", e);
        }
    }

    default void close(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            logger.error("Error with close ResultSet", e);
        }
    }
}
