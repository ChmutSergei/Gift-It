package by.chmut.giftit.filter;

import by.chmut.giftit.command.CommandType;
import by.chmut.giftit.entity.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

import static by.chmut.giftit.command.CommandType.*;
import static by.chmut.giftit.constant.AttributeName.*;
import static by.chmut.giftit.entity.User.Role.*;
import static by.chmut.giftit.entity.User.Role.MODERATOR;

public class AuthorizationFilter implements Filter {

    private final Set<CommandType> availableCommandForGuest = new HashSet<>(Arrays.asList(
            MAIN, ERROR, SIGN_IN, SIGN_UP, LOGIN, PREVIEW_ITEM, ABOUT, REGISTRATION));

    private final Set<CommandType> availableCommandForUser = new HashSet<>(Arrays.asList(
            MAIN, ERROR, LOGOUT, ACCOUNT, PREVIEW_ITEM, CART, RESET_CART, PAYMENT, MAKE_PAYMENT, ABOUT));

    private final Set<CommandType> availableCommandForDesigner = new HashSet<>(Arrays.asList(
            MAIN, ERROR, LOGOUT, ACCOUNT, PREVIEW_ITEM, CART, RESET_CART, PAYMENT, MAKE_PAYMENT, ABOUT, CREATE_ITEM, ADD_ITEM));

    private final Set<CommandType> availableCommandForModerator = new HashSet<>(Arrays.asList(
            MAIN, ERROR, LOGOUT, ACCOUNT, PREVIEW_ITEM, CART, RESET_CART, PAYMENT, MAKE_PAYMENT, ABOUT, CommandType.MODERATOR, MODERATE));

    private final Set<CommandType> availableCommandForAdmin = new HashSet<>(Arrays.asList(
            MAIN, ERROR, LOGOUT, ACCOUNT, PREVIEW_ITEM, CART, RESET_CART, PAYMENT, MAKE_PAYMENT, ABOUT, ADMINISTRATION,
            SEARCH_USER, USER_PROCESSING, GIVE_ANSWER));

    private final Map<User.Role, Set<CommandType>> accessControlMap = new HashMap<>();

    @Override
    public void init(FilterConfig filterConfig) {
        accessControlMap.put(GUEST, availableCommandForGuest);
        accessControlMap.put(USER, availableCommandForUser);
        accessControlMap.put(DESIGNER, availableCommandForDesigner);
        accessControlMap.put(MODERATOR, availableCommandForModerator);
        accessControlMap.put(ADMIN, availableCommandForAdmin);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String commandParameter = request.getParameter(COMMAND_PARAMETER_NAME);
        CommandType command = CommandType.chooseType(commandParameter);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(USER_PARAMETER_NAME);
        User.Role role = (user != null) ? user.getRole() : GUEST;
        Set<CommandType> accessControl = accessControlMap.get(role);
        if (!accessControl.contains(command)) {
            response.setStatus(response.SC_FORBIDDEN);
            response.sendError(response.SC_FORBIDDEN, "Access Denied - this is a prohibited action for this user");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}