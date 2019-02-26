package by.chmut.giftit.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter (urlPatterns = "/controller")

public class LocalizationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String locale = servletRequest.getParameter("locale");
        if (locale != null && !locale.isEmpty()) {
            ((HttpServletRequest)servletRequest).getSession().setAttribute("locale", locale);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
