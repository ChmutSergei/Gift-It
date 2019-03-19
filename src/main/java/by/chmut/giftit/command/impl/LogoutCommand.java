package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;

import javax.servlet.http.HttpServletRequest;

import static by.chmut.giftit.constant.PathPage.MAIN_PATH;

/**
 * The Logout command class
 * performs the user logout process.
 *
 * @author Sergei Chmut.
 */
public class LogoutCommand implements Command {

    /**
     * The method invalidate users session and returns the Router for REDIRECT to the MAIN page.
     *
     * @param request the request object that is passed to the servlet
     * @return the router object that contains page path for redirect
     */
    @Override
    public Router execute(HttpServletRequest request) {
        request.getSession().invalidate();
        Router router = new Router();
        router.setRedirectPath(MAIN_PATH);
        return router;
    }
}
