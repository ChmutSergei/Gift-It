package by.chmut.giftit.command;

import by.chmut.giftit.controller.Router;

import javax.servlet.http.HttpServletRequest;

/**
 * The interface Command provides to process command from the client.
 *
 * @author Sergei Chmut.
 */
public interface Command {

    /**
     * Execute command from the client.
     * Processes the data from the request and forms the Router object.
     *
     * @param request the request object that is passed to the servlet
     * @return the router object that contains page path for forward or redirect
     */
    Router execute(HttpServletRequest request);
}
