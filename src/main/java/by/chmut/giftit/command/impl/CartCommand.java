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
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static by.chmut.giftit.constant.AttributeName.*;
import static by.chmut.giftit.constant.PathPage.CART_PAGE;
import static by.chmut.giftit.constant.PathPage.ERROR_PATH;

/**
 * The Cart command class provides management of a specific user's shopping cart
 * and return Cart Page path for representation.
 *
 * @author Sergei Chmut.
 */
public class CartCommand implements Command {
    /**
     * The logger for logging possible errors.
     */
    private static final Logger logger = LogManager.getLogger();
    /**
     * The current User - client.
     */
    private User user;
    /**
     * The shopping cart for current user.
     */
    private List<Cart> cartList;
    /**
     * The map of items used in the cart.
     */
    private Map<Long, Item> items;
    /**
     * The Cart service to take advantage of business logic capabilities.
     */
    private CartService service = ServiceFactory.getInstance().getCartService();

    /**
     * The method forms a cart of items for a specific user.
     * There is a check on whether the items are still active (in stock).
     * If the request contains a command to delete from the cart or add to the cart,
     * the command is processed.
     * The result is returned to the user in the view and the resulting amount
     * is calculated for payment.
     * If errors occur during the execution of the method,
     * the method returns the Router with the Error page path.
     *
     * @param request the request object that is passed to the servlet
     * @return the router object that contains page path for forward or redirect
     */
    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setPagePath(CART_PAGE);
        user = (User) request.getSession().getAttribute(USER_PARAMETER_NAME);
        try {
            cartList = service.findCartByUserId(user.getUserId());
            items = service.findItemsForCart(cartList, request.getServletContext().getRealPath(DEFAULT_ITEM_PATH));
            checkItemsOnActive();
        } catch (ServiceException exception) {
            logger.error("Error when try to fill the cart from database", exception);
            request.getSession().setAttribute(EXCEPTION_PARAMETER_NAME, exception);
            router.setRedirectPath(ERROR_PATH);
        }
        String modifyCommand = (String) request.getSession().getAttribute(CART_COMMAND_FLAG_PARAMETER_NAME);
        if (modifyCommand != null) {
            try {
                processCommand(modifyCommand, request);
            } catch (ServiceException exception) {
                logger.error("Error when try to modify the cart", exception);
                request.getSession().setAttribute(EXCEPTION_PARAMETER_NAME, exception);
                router.setRedirectPath(ERROR_PATH);
            }
        }
        if (!cartList.isEmpty()) {
            BigDecimal total = calculateTotal();
            request.getSession().setAttribute(TOTAL_PARAMETER_NAME, total);
            request.getSession().setAttribute(ITEMS_FOR_CART_PARAMETER_NAME, items);
        }
        request.getSession().setAttribute(CART_PARAMETER_NAME, cartList);
        return router;
    }

    /**
     * Checking items on active status and remove it from the cart if it's false.
     */
    private void checkItemsOnActive() {
        for (Map.Entry entry : items.entrySet()) {
            Item item = (Item) entry.getValue();
            if (!item.isActive()) {
                for (int i = 0; i < cartList.size(); i++) {
                    if (cartList.get(i).getItemId() == item.getItemId()) {
                        cartList.remove(i);
                    }
                }
            }
        }
    }

    /**
     * The method handles the addition or removal of items from the cart.
     *
     * @param modifyCommand the modify command - add or remove
     * @param request the request object that is passed to the servlet
     * @throws ServiceException if the attempt to add or remove the item could not be handled
     */
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
                BigDecimal countItemsInCart = (BigDecimal) request.getSession().getAttribute(COUNT_IN_CART_PARAMETER_NAME);
                countItemsInCart = countItemsInCart.subtract(cartToRemove.get().getCount());
                request.getSession().setAttribute(COUNT_IN_CART_PARAMETER_NAME, countItemsInCart);
                request.getSession().removeAttribute(CART_ID_PARAMETER_NAME);
                request.getSession().removeAttribute(CART_COMMAND_FLAG_PARAMETER_NAME);
                break;
            default:
                throw new ServiceException("Impossible state: unsupported operation exception when modify cart");
        }
    }

    /**
     * Calculating total cart price.
     *
     * @return the total price
     */
    private BigDecimal calculateTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (Cart cart : cartList) {
            BigDecimal price = items.get(cart.getCartId()).getPrice();
            total = total.add(price.multiply(cart.getCount()));
        }
        return total;
    }

}
