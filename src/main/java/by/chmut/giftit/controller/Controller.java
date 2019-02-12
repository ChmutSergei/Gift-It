package by.chmut.giftit.controller;

import by.chmut.giftit.command.CommandType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static by.chmut.giftit.constant.AttributeName.COMMAND_PARAMETER_NAME;
import static by.chmut.giftit.constant.AttributeName.TITLE_ATTRIBUTE_NAME;
import static by.chmut.giftit.constant.PathPage.HOME_PAGE;
import static by.chmut.giftit.constant.PathPage.TEMPLATE_PAGE;

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
        String pagePath = router.getPagePath();
        request.getSession().setAttribute("pagePath", pagePath);
        request.getSession().setAttribute(TITLE_ATTRIBUTE_NAME, commandType.name());
        switch (router.getDispatcher()) {
            case FORWARD:
                request.getRequestDispatcher(TEMPLATE_PAGE).forward(request, response);
                break;
            case REDIRECT:
                response.sendRedirect(TEMPLATE_PAGE);
                break;
        }
    }
}
