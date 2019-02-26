package by.chmut.giftit.connectionpool;

import by.chmut.giftit.dao.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {

    private static final Logger logger = LogManager.getLogger();

    private static ConnectionPool instance;
    private static ReentrantLock locking = new ReentrantLock();
    private static AtomicBoolean instanceCreated = new AtomicBoolean(false);
    private BlockingQueue<ProxyConnection> availableConnections;
    private ConcurrentSkipListSet<ProxyConnection> usedConnections;
    private final int poolSize;

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

    private ConnectionPool() throws DaoException {
        DatabaseConfigurator configurator = DatabaseConfigurator.getInstance();
        poolSize = configurator.getPoolSize();
        initConnectionPool(configurator);
    }

    private void initConnectionPool(DatabaseConfigurator configurator) throws DaoException {
        availableConnections = new LinkedBlockingDeque<>(poolSize);
        usedConnections = new ConcurrentSkipListSet<>();
        try {
            Class.forName(configurator.getDriverName());
            for (int i = 0; i < poolSize; i++) {
                ProxyConnection proxyConnection = createConnection(configurator);
                availableConnections.add(proxyConnection);
            }
            if (availableConnections.isEmpty()) {
                throw new DaoException("Error with init connections");
            }
            if (availableConnections.size() < poolSize) {
                for (int i = 0; i < poolSize - availableConnections.size(); i++) {
                    ProxyConnection proxyConnection = createConnection(configurator);
                    availableConnections.add(proxyConnection);
                }
            }
        } catch (SQLException | ClassNotFoundException exception) {
            logger.error("Error when init connections", exception);
        }
    }

    private ProxyConnection createConnection(DatabaseConfigurator configurator) throws SQLException {
        Connection connection = DriverManager.getConnection(configurator.getUrl(),
                configurator.getUser(), configurator.getPassword());
        return new ProxyConnection(connection);
    }

    public Connection takeConnection() {
        ProxyConnection connection = null;
        try {
            connection = availableConnections.take();
            usedConnections.add(connection);
        } catch (InterruptedException e) {
            logger.error("Error when try to take connection");
            Thread.currentThread().interrupt();
        }
        return connection;
    }

    public void releaseConnection(Connection connection) {
        if (connection instanceof ProxyConnection) {
            ProxyConnection proxyConnection = (ProxyConnection) connection;
            usedConnections.remove(proxyConnection);
            try {
                availableConnections.put(proxyConnection);
            } catch (InterruptedException exception) {
                logger.error("Error when try to put connection", exception);
            }
        }
    }

    public void closePool() {
        for (int i = 0; i < poolSize; i++) {
            try {
                ProxyConnection connection = availableConnections.take();
                connection.reallyClose();
            } catch (InterruptedException | SQLException exception) {
                logger.error("Error when closing connection pool", exception);
            }
        }
        deregisterDrivers();
    }

    private void deregisterDrivers() {
        Arrays.stream(new Enumeration[]{DriverManager.getDrivers()})
                .forEach(driver -> {
                    try {
                        DriverManager.deregisterDriver((Driver) driver);
                    } catch (SQLException exception) {
                        logger.error("Error deregister driver: " + driver, exception);
                    }
                });
    }
}