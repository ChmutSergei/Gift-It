package by.chmut.giftit.listener;

import by.chmut.giftit.dao.DaoException;
import by.chmut.giftit.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;

import static by.chmut.giftit.constant.AttributeName.DEFAULT_ITEM_PATH;

@WebListener
public class ServletContextListenerImpl implements ServletContextListener {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String itemImagePath = servletContextEvent.getServletContext().getRealPath(DEFAULT_ITEM_PATH);
        File itemImageDir = new File(itemImagePath);
        if (!itemImageDir.exists()) {
            itemImageDir.mkdir();
        }
        try {
            ConnectionPool.getInstance();
        } catch (DaoException exception) {
            logger.error("Error when try to init connection pool");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
