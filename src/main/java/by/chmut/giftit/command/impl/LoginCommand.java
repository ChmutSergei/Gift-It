package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;
import by.chmut.giftit.entity.Cart;
import by.chmut.giftit.entity.User;
import by.chmut.giftit.service.CartService;
import by.chmut.giftit.service.ServiceException;
import by.chmut.giftit.service.ServiceFactory;
import by.chmut.giftit.service.UserService;
import by.chmut.giftit.validator.PasswordValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static by.chmut.giftit.constant.AttributeName.*;
import static by.chmut.giftit.constant.PathPage.*;

public class LoginCommand implements Command {

    private static final Logger logger = LogManager.getLogger();
    private UserService service = ServiceFactory.getInstance().getUserService();
    private CartService cartService = ServiceFactory.getInstance().getCartService();

    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        String previousPage = (String) request.getSession().getAttribute(PREVIOUS_PAGE_PARAMETER_NAME);
        router.setRedirectPath(previousPage);
        String username = request.getParameter(USERNAME_PARAMETER_NAME);
        String password = request.getParameter(PASSWORD_PARAMETER_NAME);
        Optional<User> user = Optional.empty();
        boolean userValid = false;
        try {
            user = service.find(username);
            if (user.isPresent()) {
                userValid = PasswordValidator.validateUser(user.get(), password);
            }
        } catch (ServiceException exception) {
            logger.error("Error when try to find User", exception);
            request.getSession().setAttribute(EXCEPTION_PARAMETER_NAME, exception);
            router.setRedirectPath(ERROR_PATH);
        }
        if (userValid) {
            request.getSession().setAttribute(USER_PARAMETER_NAME, user.get());
            BigDecimal count = calculateCountItemsInCart(user.get().getUserId());
            request.getSession().setAttribute(COUNT_IN_CART_PARAMETER_NAME, count);
        } else {
            request.getSession().setAttribute(MESSAGE_PARAMETER_NAME, MESSAGE_LOGIN_FAILED_KEY);
            router.setRedirectPath(SIGN_IN_PATH);
        }
        return router;
    }

    private BigDecimal calculateCountItemsInCart(long userId) {
        List<Cart> carts = Collections.emptyList();
        try {
            carts = cartService.getCart(userId);
        } catch (ServiceException exception) {
            logger.error("Error with set count items in the users cart");
        }
        BigDecimal count = BigDecimal.ZERO;
        for (Cart cart:carts) {
            count = count.add(cart.getCount());
        }
        return count;
    }
}
