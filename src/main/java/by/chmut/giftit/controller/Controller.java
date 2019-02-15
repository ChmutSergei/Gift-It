package by.chmut.giftit.controller;

import by.chmut.giftit.command.CommandType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static by.chmut.giftit.constant.AttributeName.*;
import static by.chmut.giftit.constant.PathPage.ERROR_PATH;

@WebServlet(urlPatterns = "/controller")
public class Controller extends HttpServlet {

    /**
     * Method doGet ...
     *
     * @param request of type HttpServletRequest
     * @param response of type HttpServletResponse
     * @throws ServletException when
     * @throws IOException when
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        performRequest(request, response);
    }

    /**
     * Method doPost ...
     *
     * @param request of type HttpServletRequest
     * @param response of type HttpServletResponse
     * @throws ServletException when
     * @throws IOException when
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        performRequest(request, response);
    }

    /**
     * Method performRequest ...
     *
     * @param request of type HttpServletRequest
     * @param response of type HttpServletResponse
     * @throws ServletException when
     * @throws IOException when
     */
    private void performRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String commandParameter = request.getParameter(COMMAND_PARAMETER_NAME);
        CommandType commandType = CommandType.chooseType(commandParameter);
        Router router = commandType.getCommand().execute(request);
        switch (router.getDispatcher()) {
            case FORWARD:
                setAttributes(request, router, commandType);
                request.getRequestDispatcher(router.getPage()).forward(request, response);
                break;
            case REDIRECT:
                response.sendRedirect(request.getContextPath() + router.getRedirectPath());
                break;
        }
    }

    private void setAttributes(HttpServletRequest request, Router router, CommandType commandType) {
        HttpSession session = request.getSession();
        String title = (String) session.getAttribute(TITLE_ATTRIBUTE_NAME);
        String commandName = commandType.name().toLowerCase();
        if (title == null) {
            title = CommandType.HOME.name().toLowerCase();
        }
        if (!title.equals(commandName)) {
            session.setAttribute(PREVIOUS_PAGE_PARAMETER_NAME, title);
            session.removeAttribute(MESSAGE_PARAMETER_NAME);
        }
        session.setAttribute(TITLE_ATTRIBUTE_NAME, commandName);
        session.setAttribute(PAGE_PATH_PARAMETER_NAME, router.getPagePath());
        if (ERROR_PATH.equals(session.getAttribute(PREVIOUS_PAGE_PARAMETER_NAME))) {
            session.removeAttribute(EXCEPTION_PARAMETER_NAME);
        }
    }
}
