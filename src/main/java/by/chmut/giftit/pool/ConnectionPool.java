package by.chmut.giftit.pool;

import by.chmut.giftit.dao.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The Connection pool class provides the creation
 * of a pool of database connections,
 * as well as the management of the provision and
 * return connections into the pool
 *
 * @author Sergei Chmut.
 */
public class ConnectionPool {

    /**
     * The logger for logging possible errors.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * The ConnectionPool instance.
     */
    private static ConnectionPool instance;
    /**
     * The ReentrantLock for the use of locking.
     */
    private static ReentrantLock locking = new ReentrantLock();
    /**
     * The constant indicate - instance created.
     */
    private static AtomicBoolean instanceCreated = new AtomicBoolean(false);
    /**
     * The Available connections.
     */
    private BlockingQueue<Connection> availableConnections;
    /**
     * The Used connections.
     */
    private ConcurrentSkipListSet<Connection> usedConnections;
    /**
     * The Pool size.
     */
    private final int poolSize;

    /**
     * The method returns the singleton object
     *
     * @return the instance - singleton object
     * @throws DaoException if the get instance could not be handled
     */
    public static ConnectionPool getInstance() throws DaoException {
        if (!instanceCreated.get()) {
            locking.lock();
            try {
                if (!instanceCreated.get()) {
                    instance = new ConnectionPool();
                    instanceCreated.set(true);
                }
            } finally {
                locking.unlock();
            }
        }
        return instance;
    }

    /**
     * Instantiates a new Connection pool.
     *
     * @throws DaoException if the init pool could not be handled
     */
    private ConnectionPool() throws DaoException {
        DatabaseConfigurator configurator = DatabaseConfigurator.getInstance();
        poolSize = configurator.getPoolSize();
        initConnectionPool(configurator);
    }

    /**
     * Init connection pool.
     *
     * @param configurator the configurator with connection pool params
     * @throws DaoException if the create connection could not be handled
     */
    private void initConnectionPool(DatabaseConfigurator configurator) throws DaoException {
        availableConnections = new LinkedBlockingDeque<>(poolSize);
        usedConnections = new ConcurrentSkipListSet<>();
        try {
            Class.forName(configurator.getDriverName());
            for (int i = 0; i < poolSize; i++) {
                Connection connection = createConnection(configurator);
                availableConnections.add(connection);
            }
            if (availableConnections.isEmpty()) {
                throw new DaoException("Error with init connections");
            }
            if (availableConnections.size() < poolSize) {
                for (int i = 0; i < poolSize - availableConnections.size(); i++) {
                    Connection connection = createConnection(configurator);
                    availableConnections.add(connection);
                }
            }
        } catch (SQLException | ClassNotFoundException exception) {
            logger.error("Error when init connections", exception);
        }
    }

    /**
     * Method return new connection.
     *
     * @param configurator the configurator with connection pool params
     * @return the new connection
     * @throws SQLException if the create connection could not be handled
     */
    private Connection createConnection(DatabaseConfigurator configurator) throws SQLException {
        Connection connection = DriverManager.getConnection(configurator.getUrl(),
                configurator.getUser(), configurator.getPassword());
        return new ProxyConnection(connection);
    }

    /**
     * Method take available connection,
     * if pool empty - waiting until any connection will be returned
     *
     * @return the connection object
     */
    public Connection takeConnection() {
        Connection connection = null;
        try {
            connection = availableConnections.take();
            usedConnections.add(connection);
        } catch (InterruptedException e) {
            logger.error("Error when try to take connection");
            Thread.currentThread().interrupt();
        }
        return connection;
    }

    /**
     * Method return used connection into the pool
     *
     * @param connection used connection
     */
    public void releaseConnection(Connection connection) {
        if (connection instanceof ProxyConnection) {
            usedConnections.remove(connection);
            try {
                availableConnections.put(connection);
            } catch (InterruptedException exception) {
                logger.error("Error when try to put connection", exception);
            }
        }
    }

    /**
     * Method ensures the closing all connections of the pool.
     */
    public void closePool() {
        for (int i = 0; i < poolSize; i++) {
            try {
                ProxyConnection connection = (ProxyConnection) availableConnections.take();
                connection.reallyClose();
            } catch (InterruptedException | SQLException exception) {
                logger.error("Error when closing connection pool", exception);
            }
        }
        deregisterDrivers();
    }

    /**
     * Method provides to deregister drivers.
     */
    private void deregisterDrivers() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException exception) {
                logger.error("Error deregister driver: " + driver, exception);
            }
        }
    }
}