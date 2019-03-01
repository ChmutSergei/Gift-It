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

import static by.chmut.giftit.command.CommandType.CART;
import static by.chmut.giftit.constant.AttributeName.USER_PARAMETER_NAME;
import static by.chmut.giftit.constant.PathPage.ERROR_PATH;

public class EmptyCartCommand implements Command {

    private static final Logger logger = LogManager.getLogger();
    private CartService service = ServiceFactory.getInstance().getCartService();

    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setRedirectPath(CART.name().toLowerCase());
        User user = (User) request.getSession().getAttribute(USER_PARAMETER_NAME);
        try {
            service.deleteAll(user.getUserId());
        } catch (ServiceException exception) {
            logger.error("Error when try to delete ALL cart from database on userId", exception);
            router.setRedirectPath(ERROR_PATH);
        }
        return router;
    }
}
