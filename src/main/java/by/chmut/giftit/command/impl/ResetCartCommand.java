package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;
import by.chmut.giftit.entity.User;
import by.chmut.giftit.service.CartService;
import by.chmut.giftit.service.ServiceException;
import by.chmut.giftit.service.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

import static by.chmut.giftit.command.CommandType.CART;
import static by.chmut.giftit.constant.AttributeName.*;
import static by.chmut.giftit.constant.PathPage.ERROR_PATH;

/**
 * The Reset cart command class provides
 * that the contents of the user cart are cleared.
 *
 * @author Sergei Chmut.
 */
public class ResetCartCommand implements Command {

    /**
     * The logger for logging possible errors.
     */
    private static final Logger logger = LogManager.getLogger();
    /**
     * The Cart service to take advantage of business logic capabilities.
     */
    private CartService service = ServiceFactory.getInstance().getCartService();

    /**
     * The method invokes the service for clearing the cart of a specific user.
     * If successful, returns Router with redirect to Cart page path,
     * and in case of failure, Router with redirect to Error page path.
     *
     * @param request the request object that is passed to the servlet
     * @return the router object that contains page path for redirect
     */
    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setRedirectPath(CART.name().toLowerCase());
        User user = (User) request.getSession().getAttribute(USER_PARAMETER_NAME);
        try {
            service.deleteAll(user.getUserId());
            request.getSession().setAttribute(COUNT_IN_CART_PARAMETER_NAME, BigDecimal.ZERO);
        } catch (ServiceException exception) {
            logger.error("Error when try to delete ALL cart from database on userId", exception);
            request.getSession().setAttribute(EXCEPTION_PARAMETER_NAME, exception);
            router.setRedirectPath(ERROR_PATH);
        }
        return router;
    }
}
