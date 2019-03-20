package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;
import by.chmut.giftit.entity.Cart;
import by.chmut.giftit.entity.User;
import by.chmut.giftit.service.CartService;
import by.chmut.giftit.service.ServiceException;
import by.chmut.giftit.service.ServiceFactory;
import by.chmut.giftit.service.UserService;
import by.chmut.giftit.validator.UserValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static by.chmut.giftit.constant.AttributeName.*;
import static by.chmut.giftit.constant.PathPage.*;

/**
 * The Login command class provides user login to the application.
 *
 * @author Sergei Chmut.
 */
public class LoginCommand implements Command {

    /**
     * The logger for logging possible errors.
     */
    private static final Logger logger = LogManager.getLogger();
    /**
     * The User service to take advantage of business logic capabilities.
     */
    private UserService userService = ServiceFactory.getInstance().getUserService();
    /**
     * The Cart service to take advantage of business logic capabilities.
     */
    private CartService cartService = ServiceFactory.getInstance().getCartService();

    /**
     * The method accepts user data from the request,
     * checks the existence of the user, as well as its authentication.
     * If successful, the user is logged in,
     * otherwise if occur errors return redirect Router with Error page path.
     * If user authentication failed set message for view and
     * return redirect Router with Login page path.
     *
     * @param request the request object that is passed to the servlet
     * @return the router object that contains page path for forward or redirect
     */
    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setRedirectPath(MAIN_PATH);
        String username = request.getParameter(USERNAME_PARAMETER_NAME);
        String password = request.getParameter(PASSWORD_PARAMETER_NAME);
        Optional<User> user = Optional.empty();
        boolean userValid = false;
        try {
            user = userService.find(username);
            if (user.isPresent()) {
                userValid = UserValidator.validatePassword(user.get(), password);
            }
        } catch (ServiceException exception) {
            logger.error("Error when try to find User", exception);
            request.getSession().setAttribute(EXCEPTION_PARAMETER_NAME, exception);
            router.setRedirectPath(ERROR_PATH);
        }
        if (userValid) {
            request.getSession().setAttribute(USER_PARAMETER_NAME, user.get());
            try {
                BigDecimal count = calculateCountItemsInCart(user.get().getUserId());
                request.getSession().setAttribute(COUNT_IN_CART_PARAMETER_NAME, count);
            } catch (ServiceException exception) {
                logger.error("Error with set count items in the user's cart", exception);
            }
        } else {
            request.getSession().setAttribute(MESSAGE_PARAMETER_NAME, MESSAGE_LOGIN_FAILED_KEY);
            router.setRedirectPath(SIGN_IN_PATH);
        }
        return router;
    }

    /**
     * Calculate count items in the cart.
     *
     * @param userId the user id
     * @return the count items in the cart
     * @throws ServiceException if the attempt to calculate count items could not be handled
     */
    private BigDecimal calculateCountItemsInCart(long userId) throws ServiceException {
        List<Cart> carts = cartService.findCartByUserId(userId);
        BigDecimal count = BigDecimal.ZERO;
        for (Cart cart : carts) {
            count = count.add(cart.getCount());
        }
        return count;
    }
}
