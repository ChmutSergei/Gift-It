package by.chmut.giftit.controller;

import by.chmut.giftit.dao.BitmapDao;
import by.chmut.giftit.dao.DaoException;
import by.chmut.giftit.dao.DaoFactory;
import by.chmut.giftit.dao.TransactionManager;
import by.chmut.giftit.entity.Bitmap;
import by.chmut.giftit.entity.Cart;
import by.chmut.giftit.entity.Item;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static by.chmut.giftit.constant.AttributeName.*;

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
                request.getSession().setAttribute(ITEM_ID_PARAMETER_NAME, request.getParameter(ITEM_ID_PARAMETER_NAME));
                response.getWriter().write(new Gson().toJson(true));
                break;
            case SEARCH_FILTER:
                int countItems = commandManager.countItemsOnFilter(request, bitmapStorage);
                response.getWriter().write(new Gson().toJson(countItems));
                break;
            case RESET_FILTER:
                resetFilter(request, response);
                break;
            case ADD_TO_CART:
                addToCart(request);
                break;
            case DELETE_FROM_CART:
                deleteFromCart(request);
                break;
        }
    }

    private void deleteFromCart(HttpServletRequest request) {
        HttpSession session = request.getSession();
        int cartId = Integer.parseInt(request.getParameter(CART_ID_PARAMETER_NAME));
        session.setAttribute(CART_ID_PARAMETER_NAME, cartId);
        session.setAttribute(CART_COMMAND_FLAG_PARAMETER_NAME, DELETE_CART_COMMAND);
    }

    private void addToCart(HttpServletRequest request) {
        String stringCount = request.getParameter(COUNT_ITEM_PARAMETER_NAME);
        int intCount = (stringCount != null) ? Integer.parseInt(stringCount) : 1;
        BigDecimal count;
        if (intCount < 1) {
            count = BigDecimal.ONE;
        } else if (intCount > MAX_COUNT_FOR_ITEM_TO_CART){
            count = new BigDecimal(MAX_COUNT_FOR_ITEM_TO_CART);
        } else {
            count = new BigDecimal(intCount);
        }
        HttpSession session = request.getSession();
        Item item = (Item) session.getAttribute(ITEM_PARAMETER_NAME);
        session.setAttribute(ITEM_TO_ADD_PARAMETER_NAME, item);
        session.setAttribute(COUNT_ITEM_PARAMETER_NAME, count);
        session.setAttribute(CART_COMMAND_FLAG_PARAMETER_NAME, ADD_CART_COMMAND);
    }

    private void resetFilter(HttpServletRequest request, HttpServletResponse response) {

    }

    private void checkUsernameOnExist(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        String username = request.getParameter(USERNAME_PARAMETER_NAME);
        try {
            Optional<User> user = userService.find(username);
            if (user.isPresent()) {
                resp.getWriter().write(new Gson().toJson(true));
            }
        } catch (ServiceException exception) {
            logger.error(exception);
            throw new ServletException("Error when try to find user");
        }
    }

    private enum AjaxCommand {
        CHECK_USERNAME, SET_ITEM_ID, SEARCH_FILTER, RESET_FILTER, ADD_TO_CART, DELETE_FROM_CART
    }
}
