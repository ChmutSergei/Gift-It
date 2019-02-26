package by.chmut.giftit.controller;

import by.chmut.giftit.dao.BitmapDao;
import by.chmut.giftit.dao.DaoException;
import by.chmut.giftit.dao.DaoFactory;
import by.chmut.giftit.dao.TransactionManager;
import by.chmut.giftit.entity.Bitmap;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static by.chmut.giftit.constant.AttributeName.COMMAND_PARAMETER_NAME;
import static by.chmut.giftit.constant.AttributeName.USERNAME_PARAMETER_NAME;

@WebServlet(urlPatterns = "/ajax")

public class AjaxController extends HttpServlet {

    private static final Logger logger = LogManager.getLogger();
    private static final Map<String, Bitmap> bitmapStorage;

    private UserService userService = new UserServiceImpl();
    private AjaxCommandManager commandManager = new AjaxCommandManager();

    static {
        BitmapDao bitmapDao = DaoFactory.getInstance().getBitmapDao();
        TransactionManager manager = new TransactionManager();
        List<Bitmap> bitmaps = Collections.emptyList();
        try {
            manager.beginTransaction(bitmapDao);
            bitmaps = bitmapDao.findAll();
            manager.endTransaction(bitmapDao);
        } catch (DaoException exception) {
            logger.error("Error when try to set bitmaps from database", exception);
        }
        bitmapStorage = bitmaps.stream()
                .collect(Collectors.toMap(Bitmap::getName, bitmap -> bitmap));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter(COMMAND_PARAMETER_NAME);
        AjaxCommand command = AjaxCommand.valueOf(type);
        switch (command) {
            case CHECK_USERNAME:
                checkUsernameOnExist(request, response);
                break;
            case SET_ITEM_ID:
                break;
            case SEARCH_FILTER:
                int countItems = commandManager.countItemsOnFilter(request, bitmapStorage);
                response.getWriter().write(new Gson().toJson(countItems));
                break;
            case RESET_FILTER:
                resetFilter(request, response);
                break;
        }
    }

    private void resetFilter(HttpServletRequest request, HttpServletResponse response) {

    }

    private void checkUsernameOnExist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter(USERNAME_PARAMETER_NAME);
        try {
            User user = userService.find(username);
            if (user != null) {
                resp.getWriter().write(new Gson().toJson(true));
            }
        } catch (ServiceException exception) {
            logger.error(exception);
            throw new ServletException("Error when try to find user");
        }
    }

    private enum AjaxCommand {
        CHECK_USERNAME, SET_ITEM_ID, SEARCH_FILTER, RESET_FILTER
    }
}
