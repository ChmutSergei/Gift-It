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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static by.chmut.giftit.constant.AttributeName.*;

@WebServlet(urlPatterns = "/ajax")
//TODO
public class AjaxController extends HttpServlet {

    private static final Logger logger = LogManager.getLogger();
    private UserService userService = new UserServiceImpl();

    private Map<String, Bitmap> bitmapStorage;
    private Bitmap bitmapPrice;
    private List<Bitmap> checkedBitmaps = new ArrayList<>();

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
                findItemsOnFilter(request, response);
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

    private void findItemsOnFilter(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        Map<String, Bitmap> bitmapStorage = (Map<String, Bitmap>) request.getSession().getAttribute(BITMAPS_PARAMETER_NAME);
        if (bitmapStorage == null) {
            bitmapStorage = setBitmaps(request);
        }
        String criteriaPrice = request.getParameter(PRICE_PARAMETER_NAME);
        if (criteriaPrice == null) {
            bitmapPrice = bitmapStorage.get(ALL_PARAMETER_NAME);
        } else {
            bitmapPrice = bitmapStorage.get(criteriaPrice);
            request.getSession().setAttribute(PRICE_CRITERIA_PARAMETER_NAME, criteriaPrice);
        }
        String criteriaType = request.getParameter(TYPE_PARAMETER_NAME);
        if (criteriaType != null) {
            Bitmap bitmap = bitmapStorage.get(criteriaType);
            String actionType = request.getParameter(ACTION_TAG_TYPE_PARAMETER_NAME);
            switch (actionType) {
                case ENABLE_CHECKBOX:
                    checkedBitmaps.add(bitmap);
                    request.getSession().setAttribute(criteriaType, criteriaType);
                    break;
                case DISABLE_CHECKBOX:
                    checkedBitmaps.remove(bitmap);
                    request.getSession().removeAttribute(criteriaType);
                    break;
            }
        }
        List<Integer> itemsId = new ArrayList<>();
        if (!checkedBitmaps.isEmpty()) {
            itemsId = doFilter();
        }
        request.getSession().setAttribute(RESULT_OF_SEARCH_ITEMS_PARAMETER_NAME, itemsId);
        response.getWriter().write(new Gson().toJson(itemsId.size()));
    }

    private List<Integer> doFilter() {
        List<Integer> result = new ArrayList<>();
        int[] resultFilter = checkedBitmaps.get(0).getData();
        for (int i = 1; i < checkedBitmaps.size(); i++) {
            resultFilter = bitwiseOrForTwoArray(resultFilter, checkedBitmaps.get(i).getData());
        }
        resultFilter = bitwiseAndForTwoArray(resultFilter, bitmapPrice.getData());
        for (int i = 0; i < resultFilter.length; i++) {
            if (resultFilter[i] > 0) {
                result.add(i + 1);
            }
        }
        return result;
    }

    private int[] bitwiseOrForTwoArray(int[] first, int[] second) {
        int[] result = new int[first.length];
        for (int i = 0; i < first.length; i++) {
            result[i] = first[i] | second[i];
        }
        return result;
    }

    private int[] bitwiseAndForTwoArray(int[] first, int[] second) {
        int[] result = new int[first.length];
        for (int i = 0; i < first.length; i++) {
            result[i] = first[i] & second[i];
        }
        return result;
    }

    private Map<String, Bitmap> setBitmaps(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, Bitmap> convertBitmaps = Collections.emptyMap();
        try {
            BitmapDao bitmapDao = DaoFactory.getInstance().getBitmapDao();
            TransactionManager manager = new TransactionManager();
            manager.beginTransaction(bitmapDao);
            List<Bitmap> bitmaps = bitmapDao.findAll();
            convertBitmaps = bitmaps.stream()
                    .collect(Collectors.toMap(Bitmap::getName, bitmap -> bitmap));
            session.setAttribute(BITMAPS_PARAMETER_NAME, convertBitmaps);
            manager.endTransaction(bitmapDao);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        session.setAttribute(PAGINATION_OFFSET_PARAMETER_NAME, DEFAULT_PAGINATION_OFFSET);
        session.setAttribute(PAGINATION_LIMIT_PARAMETER_NAME, DEFAULT_PAGINATION_LIMIT);
        return convertBitmaps;
    }

    private enum AjaxCommand {
        CHECK_USERNAME, SET_ITEM_ID, SEARCH_FILTER, RESET_FILTER
    }
}
