package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;

import javax.servlet.http.HttpServletRequest;

import static by.chmut.giftit.constant.AttributeName.USER_PARAMETER_NAME;
import static by.chmut.giftit.constant.PathPage.HOME_PATH;

public class LogoutCommand implements Command {

    @Override
    public Router execute(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_PARAMETER_NAME);
        Router router = new Router();
        router.setRedirectPath(HOME_PATH);
        return router;
    }
}
