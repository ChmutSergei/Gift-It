package by.chmut.giftit.controller;

import by.chmut.giftit.entity.Bitmap;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static by.chmut.giftit.constant.AttributeName.*;

class AjaxCommandManager {

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
}
