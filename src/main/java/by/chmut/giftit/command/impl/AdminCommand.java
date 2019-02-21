package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;

import javax.servlet.http.HttpServletRequest;

import static by.chmut.giftit.constant.PathPage.ADD_ITEM_PAGE;

public class AdminCommand implements Command {

    @Override
    public Router execute(HttpServletRequest req) {
        Router router = new Router();
        router.setPagePath(ADD_ITEM_PAGE);
        return router;
    }
}
