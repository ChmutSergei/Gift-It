package by.chmut.giftit.controller;

import by.chmut.giftit.dao.BitmapDao;
import by.chmut.giftit.dao.DaoException;
import by.chmut.giftit.dao.DaoFactory;
import by.chmut.giftit.dao.TransactionManager;
import by.chmut.giftit.entity.Bitmap;
import by.chmut.giftit.entity.Item;
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
                commandManager.checkUsernameOnExist(request, response);
                break;
            case SET_ITEM_ID:
                request.getSession().setAttribute(ITEM_ID_PARAMETER_NAME, request.getParameter(ITEM_ID_PARAMETER_NAME));
                break;
            case SEARCH_FILTER:
                int countItems = commandManager.countItemsOnFilter(request, bitmapStorage);
                request.getSession().setAttribute(COUNT_ITEM_AFTER_SEARCH_PARAMETER_NAME, countItems);
                request.getSession().setAttribute(NUMBER_PAGE_PARAMETER_NAME, DEFAULT_NUMBER_PAGE);
                response.getWriter().write(new Gson().toJson(countItems));
                break;
            case RESET_FILTER:
                resetFilter(request);
                break;
            case ADD_TO_CART: // TODO check User exist
                addToCart(request);
                break;
            case DELETE_FROM_CART: // TODO check User exist?
                deleteFromCart(request);
                break;
            case UPDATE_USER_ADDRESS_PHONE:
                commandManager.updateUserData(request,response);
                break;
            case DELETE_COMMENT:
                commandManager.deleteComment(request,response);
                break;
            case ADD_COMMENT:
                commandManager.addComment(request, response);
                break;
            case CHANGE_STATUS_ITEM:
                commandManager.changeItemStatus(request, response);
                break;
            case ACCEPT_QUESTION:
                commandManager.acceptQuestion(request,response);
                break;
            case ACCEPT_COMMENT:
                commandManager.acceptComment(request, response);
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
        BigDecimal countInCart = (BigDecimal) session.getAttribute(COUNT_IN_CART_PARAMETER_NAME);
        countInCart = countInCart.add(count);
        Item item = (Item) session.getAttribute(ITEM_PARAMETER_NAME);
        session.setAttribute(ITEM_TO_ADD_PARAMETER_NAME, item);
        session.setAttribute(COUNT_ITEM_PARAMETER_NAME, count);
        session.setAttribute(COUNT_IN_CART_PARAMETER_NAME, countInCart);
        session.setAttribute(CART_COMMAND_FLAG_PARAMETER_NAME, ADD_CART_COMMAND);
    }

    private void resetFilter(HttpServletRequest request) {
        HttpSession session = request.getSession();
        commandManager.setCheckedBitmaps(new ArrayList<>());
        int countAllItems = bitmapStorage.get(LOW_PARAMETER_NAME).getData().length;
        session.setAttribute(COUNT_ITEM_AFTER_SEARCH_PARAMETER_NAME, countAllItems);
        session.setAttribute(PRICE_CRITERIA_PARAMETER_NAME, ALL_PARAMETER_NAME);
        session.setAttribute(PAGINATION_LIMIT_PARAMETER_NAME, DEFAULT_PAGINATION_LIMIT);
        session.setAttribute(NUMBER_PAGE_PARAMETER_NAME, DEFAULT_NUMBER_PAGE);
        session.removeAttribute(RESULT_OF_SEARCH_ITEMS_PARAMETER_NAME);
        session.removeAttribute(CUP_PARAMETER_NAME);
        session.removeAttribute(SHIRT_PARAMETER_NAME);
        session.removeAttribute(PLATE_PARAMETER_NAME);
        session.removeAttribute(PILLOW_PARAMETER_NAME);
        session.removeAttribute(PUZZLE_PARAMETER_NAME);
        session.removeAttribute(MOUSE_PAD_PARAMETER_NAME);
        session.removeAttribute(TOWEL_PARAMETER_NAME);
    }

    private enum AjaxCommand {
        CHECK_USERNAME,
        SET_ITEM_ID,
        SEARCH_FILTER,
        RESET_FILTER,
        ADD_TO_CART,
        DELETE_FROM_CART,
        UPDATE_USER_ADDRESS_PHONE,
        DELETE_COMMENT,
        ADD_COMMENT,
        CHANGE_STATUS_ITEM,
        ACCEPT_QUESTION,
        ACCEPT_COMMENT
    }
}
