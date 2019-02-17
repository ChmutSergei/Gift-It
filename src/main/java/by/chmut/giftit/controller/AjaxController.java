package by.chmut.giftit.controller;

import by.chmut.giftit.entity.User;
import by.chmut.giftit.service.ServiceException;
import by.chmut.giftit.service.UserService;
import by.chmut.giftit.service.impl.UserServiceImpl;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static by.chmut.giftit.constant.AttributeName.COMMAND_PARAMETER_NAME;
import static by.chmut.giftit.constant.AttributeName.USERNAME_PARAMETER_NAME;

@WebServlet(urlPatterns = "/ajax")
//TODO
public class AjaxController extends HttpServlet {

    private static final Logger logger = LogManager.getLogger();
    private static final boolean EXISTS = true;
    private static final boolean NOT_EXIST = false;
    private UserService userService = new UserServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type = req.getParameter(COMMAND_PARAMETER_NAME);
        AjaxCommand command = AjaxCommand.valueOf(type);
        switch (command) {
            case CHECK_USERNAME:
                checkUsernameOnExist(req, resp);
                break;
            case SET_ITEM_ID:
                break;
        }
    }

    private void checkUsernameOnExist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter(USERNAME_PARAMETER_NAME);
        try {
            User user = userService.find(username);
            if (user == null) {
                resp.getWriter().write(new Gson().toJson(NOT_EXIST));
            } else {
                resp.getWriter().write(new Gson().toJson(EXISTS));
            }
        } catch (ServiceException exception) {
            logger.error(exception);
            throw new ServletException("Error when try to find user");
        }
    }

    private enum AjaxCommand {
        CHECK_USERNAME, SET_ITEM_ID
    }
}
