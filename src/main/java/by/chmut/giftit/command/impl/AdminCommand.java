package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;
import by.chmut.giftit.entity.Cart;
import by.chmut.giftit.entity.Item;
import by.chmut.giftit.entity.Order;
import by.chmut.giftit.entity.User;
import by.chmut.giftit.service.ItemService;
import by.chmut.giftit.service.OrderService;
import by.chmut.giftit.service.ServiceException;
import by.chmut.giftit.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static by.chmut.giftit.constant.AttributeName.*;
import static by.chmut.giftit.constant.PathPage.ADMIN_PAGE;

public class AdminCommand implements Command {

    private OrderService orderService = ServiceFactory.getInstance().getOrderService();
    private ItemService itemService = ServiceFactory.getInstance().getItemService();

    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setPagePath(ADMIN_PAGE);
        List<Order> orders;
        try {
            orders = orderService.findPaidOrder();
            Map<Long, User> userForOrders = orderService.findUserForOrders(orders);
            Map<Long, List<Item>> itemForOrders = orderService.findItemForOrders(orders);
            Item item = findCheckedItem(request).get();
            request.getSession().setAttribute(ORDERS_PARAMETER_NAME, orders);
            request.getSession().setAttribute(USERS_FOR_ORDERS_PARAMETER_NAME, userForOrders);
            request.getSession().setAttribute(ITEMS_FOR_ORDERS_PARAMETER_NAME, itemForOrders);
            request.getSession().setAttribute(ITEM_PARAMETER_NAME, item);
        } catch (ServiceException exception) {
            request.getSession().setAttribute(MESSAGE_PARAMETER_NAME, MESSAGE_ADMIN_ORDERS_ERROR_KEY);
        }
        return router;
    }

    private Optional<Item> findCheckedItem(HttpServletRequest request) throws ServiceException {
        String stringItemId = (String) request.getSession().getAttribute(ITEM_ID_PARAMETER_NAME);
        long itemId = 1L;
        if (stringItemId != null) {
            itemId = Long.parseLong(stringItemId);
        }
        return itemService.find(itemId, request.getServletContext().getRealPath(""));
    }
}
