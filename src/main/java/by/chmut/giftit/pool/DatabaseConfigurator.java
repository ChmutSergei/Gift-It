package by.chmut.giftit.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * The Database configurator class
 * provides to load connection pool parameters from file.
 *
 * @author Sergei Chmut.
 */
class DatabaseConfigurator {

    /**
     * The logger for logging possible errors.
     */
    private static final Logger logger = LogManager.getLogger();
    /**
     * The constant DatabaseConfigurator instance.
     */
    private static final DatabaseConfigurator instance = new DatabaseConfigurator();
    /**
     * The constant FILE_NAME key in the bundle.
     */
    private static final String FILE_NAME = "database";
    /**
     * The constant DB_DRIVER key in the bundle.
     */
    private static final String DB_DRIVER = "db.driver";
    /**
     * The constant DB_URL key in the bundle.
     */
    private static final String DB_URL = "db.url";
    /**
     * The constant DB_USER key in the bundle.
     */
    private static final String DB_USER = "db.user";
    /**
     * The constant DB_PASSWORD key in the bundle.
     */
    private static final String DB_PASSWORD = "db.password";
    /**
     * The constant DB_POOL_SIZE key in the bundle.
     */
    private static final String DB_POOL_SIZE = "db.poolsize";
    /**
     * The constant DRIVER_NAME loaded from file.
     */
    private static final String DRIVER_NAME;
    /**
     * The constant URL loaded from file.
     */
    private static final String URL;
    /**
     * The constant USER loaded from file.
     */
    private static final String USER;
    /**
     * The constant PASSWORD loaded from file.
     */
    private static final String PASSWORD;
    /**
     * The constant POOL_SIZE loaded from file.
     */
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

    /**
     * Prohibition of creating objects of this class
     */
    private DatabaseConfigurator() {
    }

    static DatabaseConfigurator getInstance() {
        return instance;
    }

    String getDriverName() {
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
