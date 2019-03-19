package by.chmut.giftit.controller;

import by.chmut.giftit.command.CommandType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static by.chmut.giftit.command.CommandType.MAIN;
import static by.chmut.giftit.constant.AttributeName.*;
import static by.chmut.giftit.constant.PathPage.ERROR_PATH;

/**
 * The Controller class accepts all user http requests sent by the GET and POST methods
 * and processes them in the performRequest method.
 *
 * @author Sergei Chmut.
 */
@WebServlet(urlPatterns = "/controller")
public class Controller extends HttpServlet {

    /**
     * Overridden doGet method processing requests sent by the GET method.
     *
     * @param request  an {@link HttpServletRequest} object that
     *                  contains the request the client has made
     *                  of the servlet
     * @param response an {@link HttpServletResponse} object that
     *                 contains the response the servlet sends
     *                 to the client
     *
     * @exception IOException   if an input or output error is
     *                              detected when the servlet handles
     *                              the request
     *
     * @exception ServletException  if the request for the GET
     *                                  could not be handled
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        performRequest(request, response);
    }

    /**
     * Overridden doPost method processing requests sent by the POST method.
     *
     * @param request  an {@link HttpServletRequest} object that
     *                  contains the request the client has made
     *                  of the servlet
     * @param response an {@link HttpServletResponse} object that
     *                 contains the response the servlet sends
     *                 to the client
     *
     * @exception IOException   if an input or output error is
     *                              detected when the servlet handles
     *                              the GET request
     *
     * @exception ServletException  if the request for the GET
     *                                  could not be handled
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        performRequest(request, response);
    }

    /**
     * The method executes the command passed in the request,
     * gets a Router and executes either FORWARD or REDIRECT.
     *
     * @param request  an {@link HttpServletRequest} object that
     *                  contains the request the client has made
     *                  of the servlet
     * @param response an {@link HttpServletResponse} object that
     *                 contains the response the servlet sends
     *                 to the client
     *
     * @exception IOException   if an input or output error is
     *                              detected when the servlet handles
     *                              the POST request
     *
     * @exception ServletException  if the request for the POST
     *                                  could not be handled
     */
    private void performRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String commandParameter = request.getParameter(COMMAND_PARAMETER_NAME);
        CommandType commandType = CommandType.chooseType(commandParameter);
        Router router = commandType.getCommand().execute(request);
        switch (router.getDispatcher()) {
            case FORWARD:
                setAttributes(request, router, commandType);
                request.getRequestDispatcher(router.getTemplatePage()).forward(request, response);
                break;
            case REDIRECT:
                response.sendRedirect(request.getContextPath() + router.getPagePath());
                break;
            default:
                throw new ServletException("Impossible state - unsupported command in Controller");
        }
    }

    /**
     * The method before executing FORWARD
     * sets the required attributes to the session.
     *
     * @param request     the request object that is passed to the servlet
     * @param router      the router object that contains page path when forward
     * @param commandType the type of command for execute
     */
    private void setAttributes(HttpServletRequest request, Router router, CommandType commandType) {
        HttpSession session = request.getSession();
        String title = (String) session.getAttribute(TITLE_ATTRIBUTE_NAME);
        String commandName = commandType.name().toLowerCase();
        if (title == null) {
            title = MAIN.name().toLowerCase();
        }
        if (!title.equals(commandName)) {
            session.setAttribute(PREVIOUS_PAGE_PARAMETER_NAME, title);
            session.removeAttribute(MESSAGE_PARAMETER_NAME);
            session.removeAttribute(SUCCESSFULLY_COMPLETED_PARAMETER_NAME);
        }
        session.setAttribute(TITLE_ATTRIBUTE_NAME, commandName);
        session.setAttribute(PAGE_PATH_PARAMETER_NAME, router.getPagePath());
        if (ERROR_PATH.equals(session.getAttribute(PREVIOUS_PAGE_PARAMETER_NAME))) {
            session.removeAttribute(EXCEPTION_PARAMETER_NAME);
        }
    }

}
