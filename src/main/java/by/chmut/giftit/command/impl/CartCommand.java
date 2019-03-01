package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;
import by.chmut.giftit.entity.Cart;
import by.chmut.giftit.entity.Item;
import by.chmut.giftit.entity.User;
import by.chmut.giftit.service.CartService;
import by.chmut.giftit.service.ServiceException;
import by.chmut.giftit.service.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.*;

import static by.chmut.giftit.constant.AttributeName.*;
import static by.chmut.giftit.constant.PathPage.CART_PAGE;
import static by.chmut.giftit.constant.PathPage.ERROR_PATH;

public class CartCommand implements Command {

    private static final Logger logger = LogManager.getLogger();

    private User user;
    private List<Cart> cartList;
    private Map<Long, Item> items;
    private CartService service = ServiceFactory.getInstance().getCartService();

    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setPagePath(CART_PAGE);
        user = (User) request.getSession().getAttribute(USER_PARAMETER_NAME);
        try {
            cartList = service.getCart(user.getUserId());
            items = service.getItemsForCart(cartList, request.getServletContext().getRealPath(""));
        } catch (ServiceException exception) {
            logger.error("Error when try to fill the cart from database", exception);
            router.setRedirectPath(ERROR_PATH);
        }
        String modifyCommand = (String) request.getSession().getAttribute(CART_COMMAND_FLAG_PARAMETER_NAME);
        if (modifyCommand != null) {
            try {
                processCommand(modifyCommand, request);
            } catch (ServiceException exception) {
                logger.error("Error when try to modify the cart", exception);
                router.setRedirectPath(ERROR_PATH);
            }
        }
        if (cartList.isEmpty()) {
            request.getSession().setAttribute(MESSAGE_PARAMETER_NAME, MESSAGE_CART_EMPTY_KEY);
        } else {
            BigDecimal total = calculateTotal();
            request.getSession().setAttribute(TOTAL_PARAMETER_NAME, total);
            request.getSession().setAttribute(CART_PARAMETER_NAME, cartList);
            request.getSession().setAttribute(ITEMS_FOR_CART_PARAMETER_NAME, items);
        }
        return router;
    }

    private void processCommand(String modifyCommand, HttpServletRequest request) throws ServiceException {
        switch (modifyCommand) {
            case ADD_CART_COMMAND:
                Item item = (Item) request.getSession().getAttribute(ITEM_TO_ADD_PARAMETER_NAME);
                BigDecimal count = (BigDecimal) request.getSession().getAttribute(COUNT_ITEM_PARAMETER_NAME);
                Cart cartToAdd = service.create(item.getItemId(), user.getUserId(), count);
                cartList.add(cartToAdd);
                items.put(cartToAdd.getCartId(), item);
                request.getSession().removeAttribute(ITEM_TO_ADD_PARAMETER_NAME);
                request.getSession().removeAttribute(COUNT_ITEM_PARAMETER_NAME);
                request.getSession().removeAttribute(CART_COMMAND_FLAG_PARAMETER_NAME);
                break;
            case DELETE_CART_COMMAND:
                int cartId = (int) request.getSession().getAttribute(CART_ID_PARAMETER_NAME);
                Optional<Cart> cartToRemove = service.findCart(cartId);
                cartToRemove.ifPresent(cart -> cartList.remove(cart));
                service.delete(cartId);
                request.getSession().removeAttribute(CART_ID_PARAMETER_NAME);
                request.getSession().removeAttribute(CART_COMMAND_FLAG_PARAMETER_NAME);
                break;
            default:
                throw new ServiceException("Unsupported operation exception when modify cart");
        }
    }

    private BigDecimal calculateTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (Cart cart : cartList) {
            BigDecimal price = items.get(cart.getCartId()).getPrice();
            total = total.add(price.multiply(cart.getCount()));
        }
        return total;
    }

}
