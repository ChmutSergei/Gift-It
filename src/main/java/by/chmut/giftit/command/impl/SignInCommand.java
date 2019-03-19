package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;

import javax.servlet.http.HttpServletRequest;

import static by.chmut.giftit.constant.PathPage.SIGN_IN_PAGE;

/**
 * The Sign in command class implements interface Command
 * and return Sign In Page path for representation.
 *
 * @author Sergei Chmut.
 */
public class SignInCommand implements Command {

    /**
     * The method generates and returns the Router for FORWARD to the Sign In page.
     *
     * @param request the request object that is passed to the servlet
     * @return the router object that contains page path for forward
     */
    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setPagePath(SIGN_IN_PAGE);
        return router;
    }
}
