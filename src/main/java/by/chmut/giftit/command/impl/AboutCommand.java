package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;

import javax.servlet.http.HttpServletRequest;

import static by.chmut.giftit.constant.PathPage.ABOUT_PAGE;

/**
 * The About command class implements interface Command
 * and return About Page path for representation.
 *
 * @author Sergei Chmut.
 */
public class AboutCommand implements Command {

    /**
     * The method generates and returns the Router for FORWARD to the ABOUT page.
     *
     * @param request the request object that is passed to the servlet
     * @return the router object that contains page path for forward
     */
    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setPagePath(ABOUT_PAGE);
        return router;
    }
}
