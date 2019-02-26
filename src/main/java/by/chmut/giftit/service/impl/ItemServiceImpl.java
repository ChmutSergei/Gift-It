package by.chmut.giftit.service.impl;

import by.chmut.giftit.dao.*;
import by.chmut.giftit.entity.Bitmap;
import by.chmut.giftit.entity.Item;
import by.chmut.giftit.service.ItemService;
import by.chmut.giftit.service.ServiceException;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

import static by.chmut.giftit.constant.AttributeName.*;

public class ItemServiceImpl implements ItemService {

    private DaoFactory factory = DaoFactory.getInstance();

    private ItemDao itemDao = factory.getItemDao();
    private BitmapDao bitmapDao = factory.getBitmapDao();
    private CommentDao commentDao = factory.getCommentDao();
    private TransactionManager manager = new TransactionManager();

    @Override
    public boolean createItem(Map<String, Object> itemParameters) throws ServiceException {
        Item item = setParameters(itemParameters);
        boolean result;
        try {
            manager.beginTransaction(itemDao, bitmapDao);
            List<Bitmap> bitmaps = bitmapDao.findAll();
            updateItemFilter(bitmaps, item);
            result = itemDao.create(item);
            for (Bitmap bitmap : bitmaps) {
                bitmapDao.update(bitmap);
            }
            manager.endTransaction(itemDao);
        } catch (DaoException exception) {
//            manager.rollback();
            throw new ServiceException(exception);
        }
        return result;
    }

    @Override
    public List<Item> findResultOfFilterItems(List<Integer> itemId, int limit, int offset, String pathForTempFiles) throws ServiceException {
        if (itemId.isEmpty()){
            return Collections.emptyList();
        }
        itemId.sort((id1, id2) -> id2 - id1);
        int threshold = offset + limit;
        if (threshold > itemId.size()) {
            threshold = itemId.size();
        }
        List<Item> result = new ArrayList<>();
        try {
            manager.beginTransaction(itemDao);
            for (int i = offset; i < threshold; i++) {
                result.add(itemDao.findEntity((long)itemId.get(i), pathForTempFiles));
            }
            manager.endTransaction(itemDao);
        } catch (DaoException exception) {
//            manager.rollback();
            throw new ServiceException(exception);
        }
        return result;
    }

    @Override
    public List<Item> findAll(String pathForTempFiles, int limit, int offset) throws ServiceException {
        List<Item> result;
        try {
            manager.beginTransaction(itemDao);
            result = itemDao.findAll(pathForTempFiles, limit, offset);
            manager.endTransaction(itemDao);
        } catch (DaoException exception) {
//            manager.rollback();
            throw new ServiceException(exception);
        }
        return result;
    }

    @Override
    public Map<Long, Integer> findCommentCountForItem(List<Item> items) throws ServiceException {
        Map<Long, Integer> result = new HashMap<>();
        try {
            manager.beginTransaction(commentDao);
            for (Item item: items) {
                int count = commentDao.countCommentForItem(item.getItemId());
                result.put(item.getItemId(), count);
            }
            manager.endTransaction(commentDao);
        } catch (DaoException exception) {
//            manager.rollback();
            throw new ServiceException(exception);
        }
        return result;
    }

    private void updateItemFilter(List<Bitmap> bitmaps, Item item) {
        String parameterPrice = selectOnValue(item.getCost());
        for (Bitmap bitmap : bitmaps) {
            int[] data = bitmap.getData();
            int[] newData = new int[data.length + 1];
            System.arraycopy(data, 0, newData, 0, data.length);
            if (bitmap.getName().equals(parameterPrice) || bitmap.getName().equals(item.getType())) {
                newData[newData.length - 1] = 1;
            }
            bitmap.setData(newData);
        }
    }

    private String selectOnValue(BigDecimal price) {
        if (price.compareTo(LOW_BORDER_PRICE) <= 0) {
            return LOW_PARAMETER_NAME;
        } else {
            if (price.compareTo(MEDIUM_BORDER_PRICE) <= 0) {
                return MEDIUM_PARAMETER_NAME;
            } else {
                if (price.compareTo(HIGH_BORDER_PRICE) <= 0) {
                    return HIGH_PARAMETER_NAME;
                }
            }
        }
        return null;
    }

    private Item setParameters(Map<String, Object> itemParameters) {
        Item item = new Item();
        item.setItemName((String) itemParameters.get(ITEM_NAME_PARAMETER_NAME));
        item.setType(((String[]) itemParameters.get(TYPE_PARAMETER_NAME))[0]);
        item.setDescription((String) itemParameters.get(DESCRIPTION_PARAMETER_NAME));
        String[] active = (String[]) itemParameters.get(ACTIVE_PARAMETER_NAME);
        item.setActive(ACTIVE_PARAMETER_NAME.equals(active[0]));
        item.setCost(new BigDecimal((String) itemParameters.get(PRICE_PARAMETER_NAME)));
        item.setImage(new File((String) itemParameters.get(ITEM_IMAGE_PARAMETER_NAME)));
        return item;
    }

    @Override
    public boolean create(Item item) throws ServiceException {
        return false;
    }

    @Override
    public Item find(Long id) throws ServiceException {
        return null;
    }

    @Override
    public Item update(Item item) throws ServiceException {
        return null;
    }

    @Override
    public boolean delete(Serializable id) throws ServiceException {
        return false;
    }
}