package by.chmut.giftit.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.time.LocalDate;

import static by.chmut.giftit.constant.AttributeName.*;

/**
 * The Http session listener class provides the addition
 * of the necessary attributes to the session when it is created.
 *
 * @author Sergei Chmut.
 */
@WebListener
public class HttpSessionListenerImpl implements HttpSessionListener {

    /**
     * The method adds start attributes to the session -
     * the default locale, parameters for the pagination, and others.
     *
     * @param httpSessionEvent the creating http session event
     */
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        session.setAttribute(LOCALE_PARAMETER_NAME, DEFAULT_PARAMETER_LOCALE);
        session.setAttribute(PAGINATION_LIMIT_PARAMETER_NAME, DEFAULT_PAGINATION_LIMIT);
        session.setAttribute(NUMBER_PAGE_PARAMETER_NAME, DEFAULT_NUMBER_PAGE);
        session.setAttribute(PRICE_CRITERIA_PARAMETER_NAME, ALL_PARAMETER_NAME);
        session.setAttribute(DATE_NOW_PARAMETER_NAME, LocalDate.now());
    }

    /**
     * The method performs actions upon session destruction,
     * no actions are specified
     *
     * @param httpSessionEvent the destroying http session event
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
    }
}