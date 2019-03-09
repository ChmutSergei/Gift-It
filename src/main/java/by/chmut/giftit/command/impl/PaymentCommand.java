package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;

import javax.servlet.http.HttpServletRequest;

import static by.chmut.giftit.constant.AttributeName.ORDER_DETAILS_PARAMETER_NAME;
import static by.chmut.giftit.constant.PathPage.PAYMENT_PAGE;

public class PaymentCommand implements Command {

    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setPagePath(PAYMENT_PAGE);
        String details = request.getParameter(ORDER_DETAILS_PARAMETER_NAME);
        request.getSession().setAttribute(ORDER_DETAILS_PARAMETER_NAME, details);
        return router;
    }
}
