package by.chmut.giftit.service.impl;

import by.chmut.giftit.dao.*;
import by.chmut.giftit.entity.Bitmap;
import by.chmut.giftit.entity.Comment;
import by.chmut.giftit.entity.Item;
import by.chmut.giftit.service.ItemService;
import by.chmut.giftit.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

import static by.chmut.giftit.constant.AttributeName.*;

public class ItemServiceImpl implements ItemService {

    private static final Logger logger = LogManager.getLogger();

    private ItemDao itemDao = DaoFactory.getInstance().getItemDao();
    private BitmapDao bitmapDao = DaoFactory.getInstance().getBitmapDao();
    private CommentDao commentDao = DaoFactory.getInstance().getCommentDao();
    private TransactionManager manager = new TransactionManager();

    @Override
    public List<Item> findItemsOnId(List<Integer> itemId, int limit, int offset, String pathForTempFiles) throws ServiceException {
        if (itemId.isEmpty()) {
            return Collections.emptyList();
        }
        itemId.sort((id1, id2) -> id2 - id1);
        int maxItemId = offset + limit;
        if (maxItemId > itemId.size()) {
            maxItemId = itemId.size();
        }
        List<Item> result = new ArrayList<>();
        try {
            manager.beginTransaction(itemDao);
            for (int i = offset; i < maxItemId; i++) {
                Optional<Item> item = itemDao.find((long) itemId.get(i), pathForTempFiles);
                item.ifPresent(result::add);
            }
            manager.endTransaction(itemDao);
        } catch (DaoException exception) {
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            throw new ServiceException(exception);
        }
        return result;
    }

    @Override
    public List<Item> findAll(String pathForTempFiles, int limit, int offset) throws ServiceException {
        List<Item> result;
        try { manager.beginTransaction(itemDao);
            result = itemDao.findAll(pathForTempFiles, limit, offset);
            manager.endTransaction(itemDao);
        } catch (DaoException exception) {
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            throw new ServiceException(exception);
        }
        return result;
    }

    @Override
    public List<Item> findPaidItems(long userId, String filePath) throws ServiceException {
        List<Item> items;
        try {
            manager.beginTransaction(itemDao);
            items = itemDao.findPaidItems(userId, filePath);
            manager.endTransaction(itemDao);
        } catch (DaoException exception) {
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            throw new ServiceException(exception);
        }
        return items;
    }

    @Override
    public Optional<Item> find(Long id, String pathForFile) throws ServiceException {
        Optional<Item> item;
        try {
            manager.beginTransaction(itemDao);
            item = itemDao.find(id, pathForFile);
            manager.endTransaction(itemDao);
        } catch (DaoException exception) {
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            throw new ServiceException(exception);
        }
        return item;
    }

    @Override
    public Item create(Map<String, Object> itemParameters) throws ServiceException {
        Item item = setParameters(itemParameters);
        try {
            manager.beginTransaction(itemDao, bitmapDao);
            List<Bitmap> bitmaps = bitmapDao.findAll();
            if (item.getPrice() == null) {
                updateItemFilter(bitmaps, item);
            }
            item = itemDao.create(item);
            for (Bitmap bitmap : bitmaps) {
                bitmapDao.update(bitmap);
            }
            manager.endTransaction(itemDao);
        } catch (DaoException exception) {
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            throw new ServiceException(exception);
        }
        return item;
    }

    private Item setParameters(Map<String, Object> itemParameters) {
        Item item = new Item();
        item.setItemName((String) itemParameters.get(ITEM_NAME_PARAMETER_NAME));
        item.setType(((String[]) itemParameters.get(TYPE_PARAMETER_NAME))[0]);
        item.setDescription((String) itemParameters.get(DESCRIPTION_PARAMETER_NAME));
        String[] active = (String[]) itemParameters.get(ACTIVE_PARAMETER_NAME);
        item.setActive(ACTIVE_PARAMETER_NAME.equals(active[0]));
        item.setPrice(new BigDecimal((String) itemParameters.get(PRICE_PARAMETER_NAME)));
        item.setImage(new File((String) itemParameters.get(ITEM_IMAGE_PARAMETER_NAME)));
        return item;
    }

    private void updateItemFilter(List<Bitmap> bitmaps, Item item) {
        String parameterPrice = selectOnValue(item.getPrice());
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
        return null; // never
    }

    @Override
    public Map<Long, Integer> findCountCommentsForItem(List<Item> items) throws ServiceException {
        Map<Long, Integer> result = new HashMap<>();
        try {
            manager.beginTransaction(commentDao);
            for (Item item : items) {
                int count = commentDao.countCommentForItem(item.getItemId());
                result.put(item.getItemId(), count);
            }
            manager.endTransaction(commentDao);
        } catch (DaoException exception) {
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            throw new ServiceException(exception);
        }
        return result;
    }

    @Override
    public List<Comment> findCommentOnItem(long id) throws ServiceException {
        List<Comment> comment;
        try {
            manager.beginTransaction(itemDao);
            comment = commentDao.findByItemId(id);
            manager.endTransaction(itemDao);
        } catch (DaoException exception) {
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            throw new ServiceException(exception);
        }
        return comment;
    }

    @Override
    public List<Item> find(List<Comment> comments) throws ServiceException {
        List<Item> items = new ArrayList<>();
        try {
            manager.beginTransaction(itemDao);
            for (Comment comment:comments) {
                Optional<Item> item = itemDao.find(comment.getCommentId());
                item.ifPresent(items::add);
            }
            manager.endTransaction(itemDao);
        } catch (DaoException e) {
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            throw new ServiceException(e);
        }
        return items;
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