package by.chmut.giftit.listener;

import by.chmut.giftit.dao.DaoException;
import by.chmut.giftit.dao.DaoFactory;
import by.chmut.giftit.dao.ItemDao;
import by.chmut.giftit.dao.TransactionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import java.time.LocalDate;

import static by.chmut.giftit.constant.AttributeName.*;

@WebListener
public class HttpSessionListenerImpl implements HttpSessionListener {

    private static final Logger logger = LogManager.getLogger();

    private ItemDao itemDao = DaoFactory.getInstance().getItemDao();
    private TransactionManager manager = new TransactionManager();

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        session.setAttribute(LOCALE_PARAMETER_NAME, DEFAULT_PARAMETER_LOCALE);
        session.setAttribute(PAGINATION_LIMIT_PARAMETER_NAME, DEFAULT_PAGINATION_LIMIT);
        session.setAttribute(NUMBER_PAGE_PARAMETER_NAME, DEFAULT_NUMBER_PAGE);
        session.setAttribute(PRICE_CRITERIA_PARAMETER_NAME, ALL_PARAMETER_NAME);
        session.setAttribute(DATE_NOW_PARAMETER_NAME, LocalDate.now());
        try {
            manager.beginTransaction(itemDao);
            int countItem = itemDao.countAllItem();
            manager.endTransaction();
            session.setAttribute(COUNT_ITEM_AFTER_SEARCH_PARAMETER_NAME, countItem);
        } catch (DaoException exception) {
            logger.error("Error when try to count all item in catalog");
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
    }
}