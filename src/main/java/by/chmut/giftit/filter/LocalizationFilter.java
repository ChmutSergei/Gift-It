package by.chmut.giftit.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static by.chmut.giftit.constant.AttributeName.LOCALE_PARAMETER_NAME;

/**
 * The Localization filter class provides
 * installation of selected locale
 *
 * @author Sergei Chmut.
 */
public class LocalizationFilter implements Filter {

    /**
     * Init.
     *
     * @param filterConfig the filter config
     */
    @Override
    public void init(FilterConfig filterConfig) {
    }

    /**
     * The method receives a user-defined choice of locale (if any) and sets it.
     *
     * @param servletRequest  an {@link ServletRequest} object that
     *                        contains the request the client has made
     *                        of the servlet
     * @param servletResponse an {@link ServletResponse} object that
     *                        contains the response the servlet sends
     *                        to the client
     * @param filterChain     access to a chain of filters to control it
     * @throws IOException      if an input or output error is
     *                          detected when the method handles the request
     * @throws ServletException if the request could not be handled
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String locale = servletRequest.getParameter(LOCALE_PARAMETER_NAME);
        if (locale != null && !locale.isEmpty()) {
            ((HttpServletRequest) servletRequest).getSession().setAttribute(LOCALE_PARAMETER_NAME, locale);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * Destroy.
     */
    @Override
    public void destroy() {
    }
}