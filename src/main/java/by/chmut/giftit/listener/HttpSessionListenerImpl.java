package by.chmut.giftit.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import static by.chmut.giftit.constant.AttributeName.*;

@WebListener
public class HttpSessionListenerImpl implements HttpSessionListener {


    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        session.setAttribute(PAGINATION_OFFSET_PARAMETER_NAME, DEFAULT_PAGINATION_OFFSET);
        session.setAttribute(LOCALE_PARAMETER_NAME, DEFAULT_PARAMETER_LOCALE);
        session.setAttribute(PAGINATION_LIMIT_PARAMETER_NAME, DEFAULT_PAGINATION_LIMIT);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

    }
}