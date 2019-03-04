package by.chmut.giftit.controller;

import by.chmut.giftit.dao.*;
import by.chmut.giftit.entity.Bitmap;
import by.chmut.giftit.entity.Comment;
import by.chmut.giftit.entity.Item;
import by.chmut.giftit.entity.User;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static by.chmut.giftit.constant.AttributeName.*;

class AjaxCommandManager {

    private UserDao userDao = DaoFactory.getInstance().getUserDao();
    private CommentDao commentDao = DaoFactory.getInstance().getCommentDao();
    private ItemDao itemDao = DaoFactory.getInstance().getItemDao();
    private Bitmap bitmapPrice;
    private List<Bitmap> checkedBitmaps = new ArrayList<>();

    int countItemsOnFilter(HttpServletRequest request, Map<String, Bitmap> bitmapStorage) {
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
        List<Integer> itemsId = Collections.emptyList();
        if (!checkedBitmaps.isEmpty()) {
            itemsId = doFilter();
        }
        request.getSession().setAttribute(RESULT_OF_SEARCH_ITEMS_PARAMETER_NAME, itemsId);
        return itemsId.size();
    }


    void updateUserData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String newPhone = request.getParameter(PHONE_PARAMETER_NAME);
        String newAddress = request.getParameter(ADDRESS_PARAMETER_NAME);
        User user = (User) request.getSession().getAttribute(USER_PARAMETER_NAME);
        user.setPhone(newPhone);
        user.setAddress(newAddress);
        try {
            userDao.update(user);
        } catch (DaoException exception) {
            newPhone = "ERROR";
        }
        UserData data = new UserData();
        data.phone = newPhone;
        data.address = newAddress;
        response.getWriter().write(new Gson().toJson(data));
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

    void deleteComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long commentId = Long.parseLong(request.getParameter(COMMENT_ID_PARAMETER_NAME));
        boolean done;
        try {
            done = commentDao.delete(commentId);
        } catch (DaoException e) {
            done = false;
        }
        if (done) {
            response.getWriter().write((new Gson()).toJson(true));
        }
    }

    void addComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Item item = (Item) request.getSession().getAttribute(ITEM_PARAMETER_NAME);
        User user = (User) request.getSession().getAttribute(USER_PARAMETER_NAME);
        String commentMessage = request.getParameter(COMMENT_PARAMETER_NAME);
        Comment comment = new Comment();
        comment.setItemId(item.getItemId());
        comment.setUserId(user.getUserId());
        comment.setDate(LocalDate.now());
        comment.setMessage(commentMessage);
        comment.setCommentStatus(Comment.CommentStatus.NEW);
        boolean success = true;
        try {
            comment = commentDao.create(comment);
            if (comment.getCommentId() == 0) {
                success = false;
            }
        } catch (DaoException e) {
            success = false;
        }
        response.getWriter().write((new Gson()).toJson(success));
    }

    void changeItemStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String stringId = request.getParameter(ITEM_ID_PARAMETER_NAME);
        if (stringId != null) {
            long itemId = Long.parseLong(stringId);
            Item item;
            try {
                item = itemDao.find(itemId, request.getServletContext().getRealPath("")).get();
                boolean status = item.isActive();
                item.setActive(!status);
                item = itemDao.update(item);
            } catch (DaoException e) {
                item = null;
            }
            if (item != null) {
                response.getWriter().write((new Gson()).toJson(true));
            }
        }
    }


    private static class UserData{
        String phone;
        String address;
    }
}
