package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;
import by.chmut.giftit.entity.User;
import by.chmut.giftit.service.ServiceException;
import by.chmut.giftit.service.ServiceFactory;
import by.chmut.giftit.service.UserService;
import by.chmut.giftit.service.impl.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static by.chmut.giftit.constant.AttributeName.*;
import static by.chmut.giftit.constant.PathPage.ERROR_PATH;

public class RegistrationCommand implements Command {

    private static final Logger logger = LogManager.getLogger();
    private UserService service = ServiceFactory.getInstance().getUserService();

    @Override
    public Router execute(HttpServletRequest req) {
        Router router = new Router();
        String previousPage = (String) req.getSession().getAttribute(PREVIOUS_PAGE_PARAMETER_NAME);
        router.setRedirectPath(previousPage);
        Map<String, String> userParameters = setParametersFromRequest(req);
        Optional<User> user = Optional.empty();
        try {
            user = Optional.ofNullable(service.create(userParameters));
        } catch (ServiceException exception) {
            logger.error(exception);
            req.getSession().setAttribute(EXCEPTION_PARAMETER_NAME, exception);
            router.setRedirectPath(ERROR_PATH);
        }
        if (user.isPresent()) {
            req.getSession().setAttribute(USER_PARAMETER_NAME, user.get());
            return router;
        }
        return router;
    }

    private Map<String, String> setParametersFromRequest(HttpServletRequest req) {
        Map<String, String> parameters = new HashMap<>();
        String username = req.getParameter(USERNAME_PARAMETER_NAME);
        String password = req.getParameter(PASSWORD_PARAMETER_NAME);
        String confirmPassword = req.getParameter(CONFIRM_PASSWORD_PARAMETER_NAME);
        String firstName = req.getParameter(FIRST_NAME_PARAMETER_NAME);
        String lastName = req.getParameter(LAST_NAME_PARAMETER_NAME);
        String email = req.getParameter(EMAIL_PARAMETER_NAME);
        String phone = req.getParameter(PHONE_PARAMETER_NAME);
        String address = req.getParameter(ADDRESS_PARAMETER_NAME);
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
