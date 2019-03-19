package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;
import by.chmut.giftit.entity.Item;
import by.chmut.giftit.entity.Order;
import by.chmut.giftit.entity.Question;
import by.chmut.giftit.entity.User;
import by.chmut.giftit.service.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static by.chmut.giftit.constant.AttributeName.*;
import static by.chmut.giftit.constant.PathPage.ADMIN_PAGE;

/**
 * The Admin command class sets the necessary data for the view
 * and return Admin Page path for representation.
 *
 * @author Sergei Chmut.
 */
public class AdminCommand implements Command {

    /**
     * The Order service to take advantage of business logic capabilities.
     */
    private OrderService orderService = ServiceFactory.getInstance().getOrderService();
    /**
     * The Item service to take advantage of business logic capabilities.
     */
    private ItemService itemService = ServiceFactory.getInstance().getItemService();
    /**
     * The Question service to take advantage of business logic capabilities.
     */
    private QuestionService questionService = ServiceFactory.getInstance().getQuestionService();

    /**
     * The method gets specific data,
     * what to provide access to them in the view.
     * In any case returns the Router with Admin Page path,
     * but if errors occurs they will be submitted for view level.
     *
     * @param request the request object that is passed to the servlet
     * @return the router object that contains page path for forward
     */
    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setPagePath(ADMIN_PAGE);
        List<Order> orders;
        try {
            orders = orderService.findPaidOrder();
            request.getSession().setAttribute(ORDERS_PARAMETER_NAME, orders);
            Map<Long, User> userForOrders = orderService.findUserForOrders(orders);
            request.getSession().setAttribute(USERS_FOR_ORDERS_PARAMETER_NAME, userForOrders);
            Map<Long, List<Item>> itemForOrders = orderService.findItemForOrders(orders);
            request.getSession().setAttribute(ITEMS_FOR_ORDERS_PARAMETER_NAME, itemForOrders);
            Item item = findCheckedItem(request).get();
            request.getSession().setAttribute(ITEM_PARAMETER_NAME, item);
            List<Question> unansweredQuestion = questionService.findActualQuestion();
            request.getSession().setAttribute(UNANSWERED_QUESTIONS_PARAMETER_NAME, unansweredQuestion);
            List<Question> allQuestion = questionService.findAllQuestion();
            request.getSession().setAttribute(QUESTIONS_PARAMETER_NAME, allQuestion);
        } catch (ServiceException exception) {
            request.getSession().setAttribute(MESSAGE_PARAMETER_NAME, MESSAGE_ADMIN_ORDERS_ERROR_KEY);
        }
        return router;
    }

    /**
     * The method finds Item according to the received request.
     *
     * @param request the request object that is passed to the servlet
     * @return the optional Item
     * @throws ServiceException if the attempt to find the item could not be handled
     */
    private Optional<Item> findCheckedItem(HttpServletRequest request) throws ServiceException {
        String stringItemId = (String) request.getSession().getAttribute(ITEM_ID_PARAMETER_NAME);
        long itemId = 1L;
        if (stringItemId != null) {
            itemId = Long.parseLong(stringItemId);
        }
        return itemService.find(itemId, request.getServletContext().getRealPath(DEFAULT_ITEM_PATH));
    }
}
