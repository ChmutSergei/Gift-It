package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;
import by.chmut.giftit.entity.Cart;
import by.chmut.giftit.entity.Order;
import by.chmut.giftit.entity.User;
import by.chmut.giftit.service.OrderService;
import by.chmut.giftit.service.ServiceException;
import by.chmut.giftit.service.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static by.chmut.giftit.command.CommandType.PAYMENT;
import static by.chmut.giftit.constant.AttributeName.*;
import static by.chmut.giftit.constant.PathPage.ERROR_PATH;

public class CheckPaymentCommand implements Command {

    private static final Logger logger = LogManager.getLogger();

    private OrderService service = ServiceFactory.getInstance().getOrderService();

    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setRedirectPath(PAYMENT.name().toLowerCase());
        Map<String, String> paymentParameters = setParameters(request);
        Order order = (Order) request.getSession().getAttribute(NEW_ORDER_PARAMETER_NAME);
        if (order == null) {
            try {
                order = createNewOrder(request);
            } catch (ServiceException exception) {
                logger.error("Error when try to create order", exception);
                request.getSession().setAttribute(EXCEPTION_PARAMETER_NAME, exception);
                router.setRedirectPath(ERROR_PATH);
            }
        }
        BigDecimal totalSum = (BigDecimal) request.getSession().getAttribute(TOTAL_PARAMETER_NAME);
        boolean done = false;
        try {
            done = service.payment(paymentParameters, totalSum, order);
        } catch (ServiceException exception) {
            logger.error("Error when try to payment order", exception);
            request.getSession().setAttribute(EXCEPTION_PARAMETER_NAME, exception);
            router.setRedirectPath(ERROR_PATH);
        }
        if (done) {
            request.getSession().setAttribute(PAYMENT_SUCCESS_PARAMETER_NAME, PAYMENT_SUCCESS_PARAMETER_NAME);
            request.getSession().removeAttribute(NEW_ORDER_PARAMETER_NAME);
            request.getSession().removeAttribute(CART_PARAMETER_NAME);
            request.getSession().removeAttribute(TOTAL_PARAMETER_NAME);
            request.getSession().removeAttribute(ORDER_DETAILS_PARAMETER_NAME);
            request.getSession().setAttribute(COUNT_IN_CART_PARAMETER_NAME, BigDecimal.ZERO);
        } else {
            request.getSession().setAttribute(MESSAGE_PARAMETER_NAME, MESSAGE_PAYMENT_FAILED_KEY);
        }
        return router;
    }

    private Order createNewOrder(HttpServletRequest request) throws ServiceException {
        User user = (User) request.getSession().getAttribute(USER_PARAMETER_NAME);
        List<Cart> carts = (List<Cart>) request.getSession().getAttribute(CART_PARAMETER_NAME);
        String orderDetails = (String) request.getSession().getAttribute(ORDER_DETAILS_PARAMETER_NAME);
        Order order = service.create(user, carts, orderDetails);
        return order;
    }

    private Map<String, String> setParameters(HttpServletRequest request) {
        Map<String, String> result = new HashMap<>();
        result.put(CARD_NAME_PARAMETER_NAME, request.getParameter(CARD_NAME_PARAMETER_NAME));
        result.put(CARD_CVC_PARAMETER_NAME, request.getParameter(CARD_CVC_PARAMETER_NAME));
        result.put(CARD_NUMBER_PARAMETER_NAME, request.getParameter(CARD_NUMBER_PARAMETER_NAME));
        result.put(CARD_MONTH_PARAMETER_NAME, request.getParameter(CARD_MONTH_PARAMETER_NAME));
        result.put(CARD_YEAR_PARAMETER_NAME, request.getParameter(CARD_YEAR_PARAMETER_NAME));
        return result;
    }
}
