package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;
import by.chmut.giftit.entity.User;
import by.chmut.giftit.service.ServiceException;
import by.chmut.giftit.service.UserService;
import by.chmut.giftit.service.impl.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

import static by.chmut.giftit.constant.AttributeName.*;
import static by.chmut.giftit.constant.PathPage.*;

public class LoginCommand implements Command {
    
    private static final Logger logger = LogManager.getLogger();
    private UserService service = new UserServiceImpl();

    @Override
    public Router execute(HttpServletRequest req) {
        Router router = new Router();
        router.setRedirectPath(HOME_PATH);
        String username = req.getParameter(USERNAME_PARAMETER_NAME);
        String password = req.getParameter(PASSWORD_PARAMETER_NAME);
        User user = null;
        boolean userValid = false;
        try {
            user = service.find(username);
            if (user != null) {
                userValid = service.validateUser(user, password);
            }
        } catch (ServiceException exception) {
            logger.error(exception);
            req.getSession().setAttribute(EXCEPTION_PARAMETER_NAME, exception);
            router.setRedirectPath(ERROR_PATH);
        }
        if (userValid) {
            req.getSession().setAttribute(USER_PARAMETER_NAME, user);
        } else {
            req.getSession().setAttribute(MESSAGE_PARAMETER_NAME, MESSAGE_LOGIN_FAILED_KEY);
            router.setRedirectPath(SIGNIN_PATH);
        }
        return router;
    }
}
