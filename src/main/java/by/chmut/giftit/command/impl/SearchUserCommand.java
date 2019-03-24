package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;
import by.chmut.giftit.entity.User;
import by.chmut.giftit.service.ServiceException;
import by.chmut.giftit.service.ServiceFactory;
import by.chmut.giftit.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static by.chmut.giftit.constant.AttributeName.*;
import static by.chmut.giftit.constant.PathPage.ERROR_PATH;
import static by.chmut.giftit.constant.PathPage.USER_PROCESSING_PAGE;

/**
 * The Search user command class provides the output of user search results
 * according to the specified parameters
 * or the output of already updated user.
 *
 * @author Sergei Chmut.
 */
public class SearchUserCommand implements Command {

    /**
     * The logger for logging possible errors.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * The User service to take advantage of business logic capabilities.
     */
    private UserService service = ServiceFactory.getInstance().getUserService();

    /**
     * Method in the case of displaying search results - gets the parameters from the request
     * and sends them to the service level. Results are saved for the view.
     * If you need to display updated data about users,
     * request them from the service and transfer it to the view.
     * If errors occur, Router with Error page path will be returned
     * and a corresponding message will be sent to the view.
     *
     * @param request the request object that is passed to the servlet
     * @return the router object that contains page path for forward or redirect
     */
    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setPagePath(USER_PROCESSING_PAGE);
        if (request.getSession().getAttribute(SHOW_RESULT_PARAMETER_NAME) == null) {
            Map<String, String> parametersSearch = getParameters(request);
            try {
                List<User> users = service.searchUserByParams(parametersSearch);
                if (users.isEmpty()) {
                    request.getSession().setAttribute(MESSAGE_PARAMETER_NAME, MESSAGE_NOT_FOUND_USERS_KEY);
                } else {
                    request.getSession().setAttribute(RESULT_OF_SEARCH_USERS_PARAMETER_NAME, users);
                }
            } catch (ServiceException exception) {
                logger.error("Error when try to search Users by parameters", exception);
                request.getSession().setAttribute(EXCEPTION_PARAMETER_NAME, exception);
                router.setRedirectPath(ERROR_PATH);
            }
        } else {
            List<User> users = (List<User>) request.getSession().getAttribute(RESULT_OF_SEARCH_USERS_PARAMETER_NAME);
            try {
                users = service.findUsersAfterUpdate(users);
            } catch (ServiceException exception) {
                logger.error("Error when try to search Users after update", exception);
                request.getSession().setAttribute(EXCEPTION_PARAMETER_NAME, exception);
                router.setRedirectPath(ERROR_PATH);
            }
            request.getSession().setAttribute(RESULT_OF_SEARCH_USERS_PARAMETER_NAME, users);
            request.getSession().removeAttribute(SHOW_RESULT_PARAMETER_NAME);
        }
        return router;
    }

    /**
     * Method provides to get parameters from request.
     *
     * @param request the request object that is passed to the servlet
     * @return the map of the search parameters
     */
    private Map<String, String> getParameters(HttpServletRequest request) {
        Map<String, String> parametersSearch = new HashMap<>();
        parametersSearch.put(USER_SEARCH_TYPE_PARAMETER_NAME, request.getParameter(USER_SEARCH_TYPE_PARAMETER_NAME));
        parametersSearch.put(USER_ID_PARAMETER_NAME, request.getParameter(USER_ID_PARAMETER_NAME));
        parametersSearch.put(USERNAME_PARAMETER_NAME, request.getParameter(USERNAME_PARAMETER_NAME));
        parametersSearch.put(INIT_DATE_PARAMETER_NAME, request.getParameter(INIT_DATE_PARAMETER_NAME));
        return parametersSearch;
    }
}
