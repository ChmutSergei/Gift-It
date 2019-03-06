package by.chmut.giftit.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static by.chmut.giftit.constant.AttributeName.LOCALE_PARAMETER_NAME;

//@WebFilter (filterName = "locale", urlPatterns = "/controller")

public class LocalizationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String locale = servletRequest.getParameter(LOCALE_PARAMETER_NAME);
        if (locale != null && !locale.isEmpty()) {
            ((HttpServletRequest)servletRequest).getSession().setAttribute(LOCALE_PARAMETER_NAME, locale);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}