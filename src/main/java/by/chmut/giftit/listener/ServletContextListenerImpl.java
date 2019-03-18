package by.chmut.giftit.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;

import static by.chmut.giftit.constant.AttributeName.DEFAULT_ITEM_PATH;

@WebListener
public class ServletContextListenerImpl implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String itemImagePath = servletContextEvent.getServletContext().getRealPath(DEFAULT_ITEM_PATH);
        File itemImageDir = new File(itemImagePath);
        if (!itemImageDir.exists()) {
            itemImageDir.mkdir();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
