package by.chmut.giftit.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.time.LocalDate;

import static by.chmut.giftit.constant.AttributeName.*;

@WebListener
public class HttpSessionListenerImpl implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        session.setAttribute(LOCALE_PARAMETER_NAME, DEFAULT_PARAMETER_LOCALE);
        session.setAttribute(PAGINATION_LIMIT_PARAMETER_NAME, DEFAULT_PAGINATION_LIMIT);
        session.setAttribute(NUMBER_PAGE_PARAMETER_NAME, DEFAULT_NUMBER_PAGE);
        session.setAttribute(PRICE_CRITERIA_PARAMETER_NAME, ALL_PARAMETER_NAME);
        session.setAttribute(DATE_NOW_PARAMETER_NAME, LocalDate.now());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
    }
}