package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;

import javax.servlet.http.HttpServletRequest;

import static by.chmut.giftit.constant.PathPage.ADMIN_PAGE;

public class AdminCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setPagePath(ADMIN_PAGE);
        return router;
    }
}
