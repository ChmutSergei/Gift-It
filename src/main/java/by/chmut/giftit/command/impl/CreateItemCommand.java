package by.chmut.giftit.command.impl;

import by.chmut.giftit.command.Command;
import by.chmut.giftit.controller.Router;

import javax.servlet.http.HttpServletRequest;

import static by.chmut.giftit.constant.PathPage.ADD_ITEM_PAGE;

/**
 * The create item command class implements interface Command
 * and return Add Item Page path for representation.
 *
 * @author Sergei Chmut.
 */
public class CreateItemCommand implements Command {

    /**
     * The method generates and returns the Router for FORWARD to the Add Item page.
     *
     * @param request the request object that is passed to the servlet
     * @return the router object that contains page path for forward
     */
    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setPagePath(ADD_ITEM_PAGE);
        return router;
    }
}
