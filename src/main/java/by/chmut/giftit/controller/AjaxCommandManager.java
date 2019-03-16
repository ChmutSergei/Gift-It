package by.chmut.giftit.controller;

import by.chmut.giftit.entity.Bitmap;
import by.chmut.giftit.entity.Item;
import by.chmut.giftit.entity.User;
import by.chmut.giftit.service.AjaxService;
import by.chmut.giftit.service.ServiceFactory;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static by.chmut.giftit.constant.AttributeName.*;

class AjaxCommandManager {

    private AjaxService ajaxService = ServiceFactory.getInstance().getAjaxService();
    private Bitmap bitmapPrice;
    private List<Bitmap> checkedBitmaps = new ArrayList<>();

    void setCheckedBitmaps(List<Bitmap> checkedBitmaps) {
        this.checkedBitmaps = checkedBitmaps;
    }

    int countItemsOnFilter(HttpServletRequest request, Map<String, Bitmap> bitmapStorage) {
        String criteriaPrice = request.getParameter(PRICE_PARAMETER_NAME);
        bitmapPrice = bitmapStorage.get(criteriaPrice);
        request.getSession().setAttribute(PRICE_CRITERIA_PARAMETER_NAME,
                criteriaPrice != null ? criteriaPrice : ALL_PARAMETER_NAME);
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
        int countAllItems = bitmapStorage.get(LOW_PARAMETER_NAME).getData().length;
        int[] filterForAllItem = checkedBitmaps.isEmpty() ? Arrays.stream(new int[countAllItems]).map(i -> i = 1).toArray() : null;
        List<Integer> itemsId = doFilter(filterForAllItem);
        int count = itemsId.size();
        request.getSession().setAttribute(RESULT_OF_SEARCH_ITEMS_PARAMETER_NAME, itemsId);
        return count;
    }


    private List<Integer> doFilter(int[] resultFilter) {
        List<Integer> result = new ArrayList<>();
        if (resultFilter == null) {
            resultFilter = checkedBitmaps.get(0).getData();
            for (int i = 1; i < checkedBitmaps.size(); i++) {
                resultFilter = bitwiseOrForTwoArray(resultFilter, checkedBitmaps.get(i).getData());
            }
        }
        if (bitmapPrice != null) {
            resultFilter = bitwiseAndForTwoArray(resultFilter, bitmapPrice.getData());
        }
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

    void updateUserData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String newPhone = request.getParameter(PHONE_PARAMETER_NAME);
        String newAddress = request.getParameter(ADDRESS_PARAMETER_NAME);
        User user = (User) request.getSession().getAttribute(USER_PARAMETER_NAME);
        user.setPhone(newPhone);
        user.setAddress(newAddress);
        boolean result = ajaxService.updateUserData(user);
        UserData data = new UserData();
        data.address = newAddress;
        if (result) {
            data.phone = newPhone;
        } else {
            data.phone = "ERROR";
        }
        response.getWriter().write(new Gson().toJson(data));
    }

    void checkUsernameOnExist(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter(USERNAME_PARAMETER_NAME);
        boolean result = ajaxService.checkUsernameOnExist(username);
        response.getWriter().write(new Gson().toJson(result));
    }

    void deleteComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long commentId = Long.parseLong(request.getParameter(COMMENT_ID_PARAMETER_NAME));
        boolean result = ajaxService.deleteComment(commentId);
        response.getWriter().write((new Gson()).toJson(result));
    }

    void addComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Item item = (Item) request.getSession().getAttribute(ITEM_PARAMETER_NAME);
        User user = (User) request.getSession().getAttribute(USER_PARAMETER_NAME);
        String commentMessage = request.getParameter(COMMENT_PARAMETER_NAME);
        if (user != null && commentMessage.length() > MIN_LENGTH_COMMENT && commentMessage.length() < MAX_LENGTH_COMMENT) {
            boolean result = ajaxService.addComment(item.getItemId(), user.getUserId(), commentMessage);
            response.getWriter().write((new Gson()).toJson(result));
        }
    }

    void changeItemStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String stringId = request.getParameter(ITEM_ID_PARAMETER_NAME);
        long itemId = Long.parseLong(stringId);
        boolean result = ajaxService.changeItemStatus(itemId, request.getServletContext().getRealPath(DEFAULT_ITEM_PATH));
        response.getWriter().write((new Gson()).toJson(result));
    }

    void acceptQuestion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String message = request.getParameter(QUESTION_PARAMETER_NAME);
        User user = (User) request.getSession().getAttribute(USER_PARAMETER_NAME);
        if (user != null && message.length() > MIN_LENGTH_COMMENT && message.length() < MAX_LENGTH_COMMENT) {
            boolean result = ajaxService.acceptQuestion(user.getUserId(), message);
            response.getWriter().write((new Gson()).toJson(result));
        }
    }

    void acceptComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String stringId = request.getParameter(COMMENT_ID_PARAMETER_NAME);
        long commentId = Long.parseLong(stringId);
        boolean result = ajaxService.acceptComment(commentId);
        response.getWriter().write((new Gson()).toJson(result));
    }

    private static class UserData {
        String phone;
        String address;
    }
}
