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
import java.util.Map;
import java.util.Optional;

import static by.chmut.giftit.constant.AttributeName.*;
import static by.chmut.giftit.constant.PathPage.ERROR_PATH;

/**
 * The Registration command class provides data about the new user,
 * saving the new user and its automatic authentication.
 *
 * @author Sergei Chmut.
 */
public class RegistrationCommand implements Command {

    /**
     * The logger for logging possible errors.
     */
    private static final Logger logger = LogManager.getLogger();
    /**
     * The User service to take advantage of business logic capabilities.
     */
    private UserService service = ServiceFactory.getInstance().getUserService();

    /**
     * The method receives data about the new user from the request
     * and sends them to the service level for validation and storage.
     * If successful, automatically authenticates the user to the system
     * and returns Router with Previous page path.
     * In case of failure, registers the corresponding message
     * and returns the Router with Error page path.
     *
     * @param request the request object that is passed to the servlet
     * @return the router object that contains page path for redirect
     */
    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        String previousPage = (String) request.getSession().getAttribute(PREVIOUS_PAGE_PARAMETER_NAME);
        router.setRedirectPath(previousPage);
        Map<String, String> userParameters = getParametersFromRequest(request);
        Optional<User> optionalUser = Optional.empty();
        try {
            optionalUser = Optional.ofNullable(service.create(userParameters));
        } catch (ServiceException exception) {
            logger.error("Error when try to save new User", exception);
            request.getSession().setAttribute(EXCEPTION_PARAMETER_NAME, exception);
            router.setRedirectPath(ERROR_PATH);
        }
        optionalUser.ifPresent(user -> request.getSession().setAttribute(USER_PARAMETER_NAME, user));
        return router;
    }

    /**
     * Method provide to get parameters from request and return it in the map.
     *
     * @param request the request object that is passed to the servlet
     * @return the map of parameters from request
     */
    private Map<String, String> getParametersFromRequest(HttpServletRequest request) {
        Map<String, String> parameters = new HashMap<>();
        String username = request.getParameter(USERNAME_PARAMETER_NAME);
        String password = request.getParameter(PASSWORD_PARAMETER_NAME);
        String confirmPassword = request.getParameter(CONFIRM_PASSWORD_PARAMETER_NAME);
        String firstName = request.getParameter(FIRST_NAME_PARAMETER_NAME);
        String lastName = request.getParameter(LAST_NAME_PARAMETER_NAME);
        String email = request.getParameter(EMAIL_PARAMETER_NAME);
        String phone = request.getParameter(PHONE_PARAMETER_NAME);
        String address = request.getParameter(ADDRESS_PARAMETER_NAME);
        parameters.put(USERNAME_PARAMETER_NAME, username);
        parameters.put(PASSWORD_PARAMETER_NAME, password);
        parameters.put(CONFIRM_PASSWORD_PARAMETER_NAME, confirmPassword);
        parameters.put(FIRST_NAME_PARAMETER_NAME, firstName);
        parameters.put(LAST_NAME_PARAMETER_NAME, lastName);
        parameters.put(EMAIL_PARAMETER_NAME, email);
        parameters.put(PHONE_PARAMETER_NAME, phone);
        parameters.put(ADDRESS_PARAMETER_NAME, address);
        return parameters;
    }
}
