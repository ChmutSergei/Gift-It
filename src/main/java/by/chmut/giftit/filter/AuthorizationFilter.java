package by.chmut.giftit.filter;

import by.chmut.giftit.command.CommandType;
import by.chmut.giftit.entity.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static by.chmut.giftit.command.CommandType.SIGNIN;
import static by.chmut.giftit.command.CommandType.SIGNUP;
import static by.chmut.giftit.constant.AttributeName.COMMAND_PARAMETER_NAME;
import static by.chmut.giftit.constant.AttributeName.USER_PARAMETER_NAME;

@WebFilter(urlPatterns = "/controller")

public class AuthorizationFilter implements Filter {

    private static final String HOME_PART_PATH = "/controller?command=home";

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        String commandParameter = req.getParameter(COMMAND_PARAMETER_NAME);
        CommandType type = CommandType.chooseType(commandParameter);
        String contextPath = req.getContextPath();
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute(USER_PARAMETER_NAME);
        if ((SIGNIN.equals(type) || SIGNUP.equals(type)) && user != null) {
            res.sendRedirect(contextPath + HOME_PART_PATH);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}

//            if (user == null)  {
//                session.setAttribute("errorMsg", "accessLog");
//                res.sendRedirect(contextPath + "/controller?command=add_account");
//                return;
//            }
//            if (!user.getRole().equals("admin") & ADMIN.equals(type)) {
//            res.sendRedirect(contextPath + HOME_PART_PATH);
//                return;