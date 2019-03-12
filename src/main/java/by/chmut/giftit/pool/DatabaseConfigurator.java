package by.chmut.giftit.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

class DatabaseConfigurator {

    private static final Logger logger = LogManager.getLogger();
    private static final DatabaseConfigurator instance = new DatabaseConfigurator();
    private static final String FILE_NAME = "database";
    private static final String DB_DRIVER = "db.driver";
    private static final String DB_URL = "db.url";
    private static final String DB_USER = "db.user";
    private static final String DB_PASSWORD = "db.password";
    private static final String DB_POOL_SIZE = "db.poolsize";
    private static final String DRIVER_NAME;
    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;
    private static final int POOL_SIZE;

    static {
        try {
            ResourceBundle resource = ResourceBundle.getBundle(FILE_NAME);
            DRIVER_NAME = resource.getString(DB_DRIVER);
            URL = resource.getString(DB_URL);
            USER = resource.getString(DB_USER);
            PASSWORD = resource.getString(DB_PASSWORD);
            POOL_SIZE = Integer.parseInt(resource.getString(DB_POOL_SIZE));
        } catch (MissingResourceException exception) {
            logger.fatal("Fatal error with initialization database parameters", exception);
            throw new ExceptionInInitializerError("Fatal error with initialization database parameters");
        }
    }

    private DatabaseConfigurator() {
    }
    static DatabaseConfigurator getInstance() {
        return instance;
    }

    public String getDriverName() {
        return DRIVER_NAME;
    }

    String getUrl() {
        return URL;
    }

    String getUser() {
        return USER;
    }

    String getPassword() {
        return PASSWORD;
    }

    int getPoolSize() {
        return POOL_SIZE;
    }
}
