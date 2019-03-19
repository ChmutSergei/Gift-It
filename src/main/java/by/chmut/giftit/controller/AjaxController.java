package by.chmut.giftit.controller;

import by.chmut.giftit.dao.BitmapDao;
import by.chmut.giftit.dao.DaoException;
import by.chmut.giftit.dao.DaoFactory;
import by.chmut.giftit.dao.TransactionManager;
import by.chmut.giftit.entity.Bitmap;
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
import static by.chmut.giftit.constant.AttributeName.ITEM_ID_PARAMETER_NAME;

/**
 * The Ajax controller class serves to process only ajax requests.
 *
 * @author Sergei Chmut.
 */
@WebServlet(urlPatterns = "/ajax")

public class AjaxController extends HttpServlet {

    /**
     * The logger for logging possible errors.
     */
    private static final Logger logger = LogManager.getLogger();
    /**
     * The constant bitmap storage is used for all filtering requests for searching.
     */
    private static final Map<String, Bitmap> bitmapStorage;

    /**
     * The Command manager class is helper class that contains the implementation of ajax commands.
     */
    private AjaxCommandManager commandManager = new AjaxCommandManager();

    /**
     * One-time initialization bitmap storage
     */
    static {
        BitmapDao bitmapDao = DaoFactory.getInstance().getBitmapDao();
        TransactionManager manager = new TransactionManager();
        List<Bitmap> bitmaps = Collections.emptyList();
        try {
            manager.beginTransaction(bitmapDao);
            bitmaps = bitmapDao.findAll();
            manager.endTransaction();
        } catch (DaoException exception) {
            logger.error("Error when try to set bitmaps from database", exception);
        }
        bitmapStorage = bitmaps.stream()
                .collect(Collectors.toMap(Bitmap::getName, bitmap -> bitmap));
    }

    /**
     * Overridden doPost method processing requests sent by the POST method with ajax command.
     * The method parses the request and, if there is a contains ajax command execute it.
     *
     * @param request  an {@link HttpServletRequest} object that
     *                 contains the request the client has made
     *                 of the servlet
     * @param response an {@link HttpServletResponse} object that
     *                 contains the response the servlet sends
     *                 to the client
     * @throws IOException if an input or output error is
     *                     detected when the servlet handles
     *                     the POST request
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
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
                commandManager.countItemsOnFilter(request, response, bitmapStorage);
                break;
            case RESET_FILTER:
                commandManager.resetFilter(request, bitmapStorage);
                break;
            case ADD_TO_CART:
                commandManager.addToCart(request);
                break;
            case DELETE_FROM_CART:
                commandManager.deleteFromCart(request);
                break;
            case UPDATE_USER_ADDRESS_PHONE:
                commandManager.updateUserData(request, response);
                break;
            case DELETE_COMMENT:
                commandManager.deleteComment(request, response);
                break;
            case ADD_COMMENT:
                commandManager.addComment(request, response);
                break;
            case CHANGE_STATUS_ITEM:
                commandManager.changeItemStatus(request, response);
                break;
            case ACCEPT_QUESTION:
                commandManager.acceptQuestion(request, response);
                break;
            case ACCEPT_COMMENT:
                commandManager.acceptComment(request, response);
                break;
            default:
                throw new ServletException("Impossible state: unsupported ajax command");
        }
    }

    /**
     * The enum Ajax command.
     *
     * @author Sergei Chmut.
     */
    private enum AjaxCommand {
        /**
         * Check username ajax command.
         */
        CHECK_USERNAME,
        /**
         * Set item id ajax command.
         */
        SET_ITEM_ID,
        /**
         * Search filter ajax command.
         */
        SEARCH_FILTER,
        /**
         * Reset filter ajax command.
         */
        RESET_FILTER,
        /**
         * Add to the cart ajax command.
         */
        ADD_TO_CART,
        /**
         * Delete from the cart ajax command.
         */
        DELETE_FROM_CART,
        /**
         * Update user address and phone ajax command.
         */
        UPDATE_USER_ADDRESS_PHONE,
        /**
         * Delete comment ajax command.
         */
        DELETE_COMMENT,
        /**
         * Add comment ajax command.
         */
        ADD_COMMENT,
        /**
         * Change status of item ajax command.
         */
        CHANGE_STATUS_ITEM,
        /**
         * Accept question ajax command.
         */
        ACCEPT_QUESTION,
        /**
         * Accept comment ajax command.
         */
        ACCEPT_COMMENT
    }
}
