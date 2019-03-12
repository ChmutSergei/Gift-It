package by.chmut.giftit.pool;

import by.chmut.giftit.dao.DaoException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;

public class ConnectionPoolTest {

    private ConnectionPool pool;
    private Connection connection;
    private BlockingQueue availableConnections;
    private ConcurrentSkipListSet usedConnections;

    @BeforeMethod
    public void setUp() throws DaoException, NoSuchFieldException, IllegalAccessException {
        pool = ConnectionPool.getInstance();
        Field field1 = pool.getClass().getDeclaredField("availableConnections");
        Field field2 = pool.getClass().getDeclaredField("usedConnections");
        field1.setAccessible(true);
        field2.setAccessible(true);
        availableConnections = (BlockingQueue) field1.get(pool);
        usedConnections = (ConcurrentSkipListSet) field2.get(pool);
    }

    @AfterMethod
    public void tearDown() {
        pool = null;
        availableConnections = null;
        usedConnections = null;
    }

    @Test(description = "Test that the method getInstance returns the notNull reference")
    public void testGetInstance() {
        Assert.assertNotNull(pool);
    }

    @DataProvider(name = "forGetInstance")
    public static Object[][] forGetInstance() throws DaoException {
        return new Object[][]{
                {ConnectionPool.getInstance()},
                {ConnectionPool.getInstance()},
                {ConnectionPool.getInstance()},
                {ConnectionPool.getInstance()},
                {ConnectionPool.getInstance()}
        };
    }

    @Test(dataProvider = "forGetInstance", description = "Test that the method returns the reference " +
            "to the same object in memory when it is executed again")
    public void testGetInstanceRepeat(ConnectionPool actual) throws DaoException {
        ConnectionPool expected = pool;
        Assert.assertSame(actual, expected);
    }

    @Test(priority = 0, description = "Test that available connections in the pool will reduce by one when method invoke")
    public void testTakeConnectionAvailable() throws NoSuchFieldException, IllegalAccessException {
        int expected = availableConnections.size() - 1;
        connection = pool.takeConnection();
        Field field = pool.getClass().getDeclaredField("availableConnections");
        field.setAccessible(true);
        BlockingQueue queueAfter = (BlockingQueue) field.get(pool);
        int actual = queueAfter.size();
        Assert.assertEquals(actual, expected);
    }

    @Test(priority = 1, description = "Test that available connections in the pool will increment by one when method invoke")
    public void testReleaseConnectionAvailable() throws NoSuchFieldException, IllegalAccessException {
        int expected = availableConnections.size() + 1;
        pool.releaseConnection(connection);
        Field field = pool.getClass().getDeclaredField("availableConnections");
        field.setAccessible(true);
        BlockingQueue queueAfter = (BlockingQueue) field.get(pool);
        int actual = queueAfter.size();
        Assert.assertEquals(actual, expected);
    }

    @Test(priority = 2, description = "Test that used connections in the pool will increment by one when method invoke")
    public void testTakeConnectionUsed() throws NoSuchFieldException, IllegalAccessException {
        int expected = usedConnections.size() + 1;
        connection = pool.takeConnection();
        Field field = pool.getClass().getDeclaredField("usedConnections");
        field.setAccessible(true);
        ConcurrentSkipListSet queueAfter = (ConcurrentSkipListSet) field.get(pool);
        int actual = queueAfter.size();
        Assert.assertEquals(actual, expected);
    }

    @Test(priority = 3, description = "Test that used connections in the pool will reduce by one when method invoke")
    public void testReleaseConnectionUsed() throws NoSuchFieldException, IllegalAccessException {
        int expected = usedConnections.size() - 1;
        pool.releaseConnection(connection);
        Field field = pool.getClass().getDeclaredField("usedConnections");
        field.setAccessible(true);
        ConcurrentSkipListSet queueAfter = (ConcurrentSkipListSet) field.get(pool);
        int actual = queueAfter.size();
        Assert.assertEquals(actual, expected);
    }

    @Test(priority = 4, description = "Test that available connections will be empty size when method closePool() invoke")
    public void testClosePoolAvailable() throws NoSuchFieldException, IllegalAccessException {
        pool.closePool();
        int expected = 0;
        Field field = pool.getClass().getDeclaredField("availableConnections");
        field.setAccessible(true);
        BlockingQueue queueAfter = (BlockingQueue) field.get(pool);
        int actual = queueAfter.size();
        Assert.assertEquals(actual, expected);
    }
}
