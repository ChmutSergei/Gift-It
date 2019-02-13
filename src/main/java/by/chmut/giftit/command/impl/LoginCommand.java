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
import static by.chmut.giftit.constant.PathPage.ERROR_PAGE;

public class LoginCommand implements Command {
    
    private static final Logger logger = LogManager.getLogger();
    private UserService service = new UserServiceImpl();

    @Override
    public Router execute(HttpServletRequest req) {
        Router router = new Router();
        router.setPagePath(req.getParameter(PREVIOUS_PAGE_PARAMETER_NAME));
        String username = req.getParameter(USERNAME_PARAMETER_NAME);
        String password = req.getParameter(PASSWORD_PARAMETER_NAME);
        boolean userValid = false;
        try {
            User user = service.find(username);
            if (user != null) {
                userValid = service.validateUser(user, password);
            }
        } catch (ServiceException e) {
            logger.error(e);
        }
        if (!userValid) {
            req.getSession().setAttribute(MESSAGE_PARAMETER_NAME, MESSAGE_LOGIN_FAILED_KEY);
            router.setPagePath(ERROR_PAGE);
        }
        return router;
    }
}
