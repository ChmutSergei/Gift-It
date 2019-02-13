package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;
import by.chmut.giftit.entity.User;
import by.chmut.giftit.service.ServiceException;
import by.chmut.giftit.service.UserService;
import by.chmut.giftit.service.impl.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.http.HttpServletRequest;

import static by.chmut.giftit.constant.AttributeName.*;
import static by.chmut.giftit.constant.PathPage.ERROR_PAGE;

public class RegistrationCommand implements Command {

    private static final Logger logger = LogManager.getLogger();
    private UserService service = new UserServiceImpl();

    @Override
    public Router execute(HttpServletRequest req) {
        Router router = new Router();
        router.setPagePath(req.getParameter(PREVIOUS_PAGE_PARAMETER_NAME));
        User user = createUserWithParameters(req);
        boolean success = false;
        if (user != null) {
            try {
                success = service.create(user);
            } catch (ServiceException e) {
                logger.error(e);
            }
        }
        if (!success) {
            req.getSession().setAttribute(MESSAGE_PARAMETER_NAME, MESSAGE_WITH_CREATE_USER_KEY);
            router.setPagePath(ERROR_PAGE);
        }
        req.getSession().setAttribute(USER_PARAMETER_NAME, user);
        return router;
    }

    private User createUserWithParameters(HttpServletRequest req) {
        String username = req.getParameter(USERNAME_PARAMETER_NAME);
        String password = req.getParameter(PASSWORD_PARAMETER_NAME);
        String firstName = req.getParameter(FIRST_NAME_PARAMETER_NAME);
        String lastName = req.getParameter(LAST__NAME_PARAMETER_NAME);
        String email = req.getParameter(EMAIL_PARAMETER_NAME);
        String phone = req.getParameter(PHONE_PARAMETER_NAME);
        String address = req.getParameter(ADDRESS_PARAMETER_NAME);
        if (username == null || password == null || firstName == null || lastName == null || email == null ||
                phone == null || address == null) {
            return null;
        }
        String passHashed = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User();
        user.setUsername(username);
        user.setPassword(passHashed);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);
        user.setRole(User.Role.USER);
        return user;
    }
}
