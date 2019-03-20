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
import static by.chmut.giftit.constant.AttributeName.COMMAND_PARAMETER_NAME;
import static by.chmut.giftit.constant.AttributeName.USER_PARAMETER_NAME;
import static by.chmut.giftit.entity.User.Role.*;
import static by.chmut.giftit.entity.User.Role.MODERATOR;

/**
 * The Authorization filter class provides control over access
 * to pages and execution of commands, according to user Role.
 *
 * @author Sergei Chmut.
 */
public class AuthorizationFilter implements Filter {

    /**
     * The Available command for guest role.
     */
    private final Set<CommandType> availableCommandForGuest = new HashSet<>(Arrays.asList(
            MAIN, ERROR, SIGN_IN, SIGN_UP, LOGIN, PREVIEW_ITEM, ABOUT, REGISTRATION));

    /**
     * The Available command for user role.
     */
    private final Set<CommandType> availableCommandForUser = new HashSet<>(Arrays.asList(
            MAIN, ERROR, LOGOUT, ACCOUNT, PREVIEW_ITEM, CART, RESET_CART, PAYMENT, MAKE_PAYMENT, ABOUT));

    /**
     * The Available command for designer role.
     */
    private final Set<CommandType> availableCommandForDesigner = new HashSet<>(Arrays.asList(
            MAIN, ERROR, LOGOUT, ACCOUNT, PREVIEW_ITEM, CART, RESET_CART, PAYMENT, MAKE_PAYMENT,
            ABOUT, CREATE_ITEM, ADD_ITEM));

    /**
     * The Available command for moderator role.
     */
    private final Set<CommandType> availableCommandForModerator = new HashSet<>(Arrays.asList(
            MAIN, ERROR, LOGOUT, ACCOUNT, PREVIEW_ITEM, CART, RESET_CART, PAYMENT, MAKE_PAYMENT, ABOUT,
            CommandType.MODERATOR, MODERATE));

    /**
     * The Available command for admin role.
     */
    private final Set<CommandType> availableCommandForAdmin = new HashSet<>(Arrays.asList(
            MAIN, ERROR, LOGOUT, ACCOUNT, PREVIEW_ITEM, CART, RESET_CART, PAYMENT, MAKE_PAYMENT, ABOUT,
            ADMINISTRATION, SEARCH_USER, USER_PROCESSING, GIVE_ANSWER));

    /**
     * The Access control map contains lists of available commands by key-Role of user.
     */
    private final Map<User.Role, Set<CommandType>> accessControlMap = new HashMap<>();

    /**
     * Init Access control map.
     *
     * @param filterConfig the filter config
     */
    @Override
    public void init(FilterConfig filterConfig) {
        accessControlMap.put(GUEST, availableCommandForGuest);
        accessControlMap.put(USER, availableCommandForUser);
        accessControlMap.put(DESIGNER, availableCommandForDesigner);
        accessControlMap.put(MODERATOR, availableCommandForModerator);
        accessControlMap.put(ADMIN, availableCommandForAdmin);
    }

    /**
     * The method receives the command from the request and checks
     * it for access  to execute with according to the user's Role.
     * If the user does not have the right to execute the command,
     * an error will be returned to him - access denied
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

    /**
     * Destroy.
     */
    @Override
    public void destroy() {
    }
}