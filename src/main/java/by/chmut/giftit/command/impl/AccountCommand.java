package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;
import by.chmut.giftit.entity.Cart;
import by.chmut.giftit.entity.Item;
import by.chmut.giftit.entity.User;
import by.chmut.giftit.service.ItemService;
import by.chmut.giftit.service.ServiceException;
import by.chmut.giftit.service.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static by.chmut.giftit.constant.AttributeName.ITEMS_FOR_CART_PARAMETER_NAME;
import static by.chmut.giftit.constant.AttributeName.RESULT_OF_SEARCH_ITEMS_PARAMETER_NAME;
import static by.chmut.giftit.constant.AttributeName.USER_PARAMETER_NAME;
import static by.chmut.giftit.constant.PathPage.ACCOUNT_PAGE;
import static by.chmut.giftit.constant.PathPage.ERROR_PATH;

public class AccountCommand implements Command {

    private static final Logger logger = LogManager.getLogger();

    private ItemService itemService = ServiceFactory.getInstance().getItemService();

    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setPagePath(ACCOUNT_PAGE);
        User user = (User) request.getSession().getAttribute(USER_PARAMETER_NAME);
        try {
            List<Item> paidItems = itemService.findPaidItems(user.getUserId(), request.getServletContext().getRealPath(""));
            request.getSession().setAttribute(ITEMS_FOR_CART_PARAMETER_NAME, paidItems);
        } catch (ServiceException exception) {
            logger.error("Error when try to find paid Items", exception);
            router.setRedirectPath(ERROR_PATH);
        }
        return router;
    }
}
