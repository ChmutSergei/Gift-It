package by.chmut.giftit.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * The Xss filter class provides protection against XSS attacks.
 *
 * @author Sergei Chmut.
 */
public class XssFilter implements Filter {

    /**
     * Init.
     *
     * @param filterConfig the filter config
     * @throws ServletException the servlet exception
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * The method returns for each request - the request is wrapped in XssRequestWrapper class.
     *
     * @param servletRequest  an {@link ServletRequest} object that
     *                        contains the request the client has made
     *                        of the servlet
     * @param servletResponse an {@link ServletResponse} object that
     *                        contains the response the servlet sends
     *                        to the client
     * @param filterChain     access to a filterChain of filters to control it
     * @throws IOException      if an input or output error is
     *                          detected when the method handles the request
     * @throws ServletException if the request could not be handled
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        filterChain.doFilter(new XssRequestWrapper((HttpServletRequest) servletRequest), servletResponse);
    }

    /**
     * Destroy.
     */
    @Override
    public void destroy() {
    }
}
