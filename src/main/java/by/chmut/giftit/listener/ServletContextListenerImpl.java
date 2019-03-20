package by.chmut.giftit.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;

import static by.chmut.giftit.constant.AttributeName.DEFAULT_ITEM_PATH;

/**
 * The Servlet context listener class provides the execution of actions
 * when creating and destroying the servlet context.
 *
 * @author Sergei Chmut.
 */
@WebListener
public class ServletContextListenerImpl implements ServletContextListener {

    /**
     * When creating the servlet context method creates a directory
     * for saving item's image files.
     *
     * @param servletContextEvent the creating servlet context event
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String itemImagePath = servletContextEvent.getServletContext().getRealPath(DEFAULT_ITEM_PATH);
        File itemImageDir = new File(itemImagePath);
        if (!itemImageDir.exists()) {
            itemImageDir.mkdir();
        }
    }

    /**
     * The method performs actions upon context destruction,
     * no actions are specified.
     *
     * @param servletContextEvent the destroying servlet context event
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
