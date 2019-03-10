package by.chmut.giftit.filter;

import by.chmut.giftit.command.CommandType;
import by.chmut.giftit.entity.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static by.chmut.giftit.command.CommandType.SIGN_IN;
import static by.chmut.giftit.command.CommandType.SIGN_UP;
import static by.chmut.giftit.constant.AttributeName.COMMAND_PARAMETER_NAME;
import static by.chmut.giftit.constant.AttributeName.USER_PARAMETER_NAME;
import static by.chmut.giftit.entity.User.Role.GUEST;

public class AuthorizationFilter implements Filter {

    private static final String MAIN_PART_PATH = "/controller?command=main";
    private static final String SIGN_UP_PART_PATH = "/controller?command=sign_up";

    private User.Role role;

    @Override
    public void init(FilterConfig filterConfig) {
    }

    /**
     * список страниц что нужно будет закрыть -
     * Account
     * Cart
     * EmptyCart
     * Administration
     * AddItem - страница для designer
     * Processing User
     * Moderator
     * Payment
     * идея создать список доступных страниц для каждой роли и проверять на contains в switch
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        String commandParameter = req.getParameter(COMMAND_PARAMETER_NAME);
        CommandType type = CommandType.chooseType(commandParameter);
        String contextPath = req.getContextPath();
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute(USER_PARAMETER_NAME);


        role = (user != null) ? user.getRole() : GUEST; // делаем Map с ключами role

        if ((SIGN_IN.equals(type) || SIGN_UP.equals(type)) && user != null) {
            res.sendRedirect(contextPath + MAIN_PART_PATH);
            return;
        }
//        if (PREVIEW_ITEM.equals(type) && (user == null)) {
//            res.sendRedirect(contextPath + SIGN_UP_PART_PATH);
//            return;
//        }
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
//            res.sendRedirect(contextPath + MAIN_PART_PATH);
//                return;