package by.chmut.giftit.dao;

import by.chmut.giftit.entity.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * The interface Data Access Object (Dao) to abstract and encapsulate
 * all access to the data source.
 * The DAO manages the connection with the data source to obtain
 * and store data.
 * The DAO implements the access mechanism required
 * to work with the data source
 *
 * @param <K> the type of id
 * @param <T> the type extends Entity
 * @author Sergei Chmut.
 */
public interface Dao<K, T extends Entity> {

    /**
     * The logger for logging possible errors.
     */
    Logger logger = LogManager.getLogger();

    /**
     * Find all entity from the database.
     *
     * @return the list of Entity
     * @throws DaoException if find all can't be handled
     */
    List<T> findAll() throws DaoException;

    /**
     * Find entity by id.
     *
     * @param id entity's id
     * @return the optional Entity
     * @throws DaoException if find entity can't be handled
     */
    Optional<T> findEntity(K id) throws DaoException;

    /**
     * Delete entity by id.
     *
     * @param id entity's id
     * @return true if delete done otherwise false
     * @throws DaoException if delete can't be handled
     */
    boolean delete(K id) throws DaoException;

    /**
     * Delete entity.
     *
     * @param entity the entity
     * @return true if delete done otherwise false
     * @throws DaoException if delete can't be handled
     */
    boolean delete(T entity) throws DaoException;

    /**
     * Add entity to database.
     *
     * @param entity the entity
     * @return Entity that was created
     * @throws DaoException if create entity can't be handled
     */
    T create(T entity) throws DaoException;

    /**
     * Update entity in database..
     *
     * @param entity the entity
     * @return Entity that was updated
     * @throws DaoException if update entity can't be handled
     */
    T update(T entity) throws DaoException;

    void setConnection(Connection connection);

    Connection getConnection();

    /**
     * Close statement.
     *
     * @param statement the statement
     */
    default void close(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            logger.error("Error with close Statement", e);
        }
    }

    /**
     * Close resultSet.
     *
     * @param resultSet the result set
     */
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
