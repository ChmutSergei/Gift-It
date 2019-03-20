package by.chmut.giftit.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The Encoding filter class ensures that the correct encoding
 * is set in all requests and responses processed.
 *
 * @author Sergei Chmut.
 */
public class EncodingFilter implements Filter {

    /**
     * The default Encoding.
     */
    private String encoding = "UTF-8";

    /**
     * Init parameter encoding from filter config.
     *
     * @param filterConfig the filter config
     */
    @Override
    public void init(FilterConfig filterConfig) {
        String encodingParam = filterConfig.getInitParameter("encoding");
        if (encodingParam != null && !"".equals(encodingParam)) {
            encoding = encodingParam;
        }
    }

    /**
     * The method establishes a single encoding in all requests and responses.
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
        servletRequest.setCharacterEncoding(encoding);
        servletResponse.setContentType("text/html; charset=" + encoding);
        servletResponse.setCharacterEncoding(encoding);
        ((HttpServletResponse) servletResponse).setHeader("Content-Language", encoding);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * Destroy.
     */
    @Override
    public void destroy() {
    }
}