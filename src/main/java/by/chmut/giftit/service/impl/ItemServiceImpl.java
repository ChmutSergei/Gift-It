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
import java.math.BigDecimal;
import java.util.*;

import static by.chmut.giftit.constant.AttributeName.*;

/**
 * The Item service class.
 *
 * @author Sergei Chmut.
 */
public class ItemServiceImpl implements ItemService {

    /**
     * The constant logger.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * The Item dao.
     */
    private ItemDao itemDao = DaoFactory.getInstance().getItemDao();
    /**
     * The Bitmap dao.
     */
    private BitmapDao bitmapDao = DaoFactory.getInstance().getBitmapDao();
    /**
     * The Comment dao.
     */
    private CommentDao commentDao = DaoFactory.getInstance().getCommentDao();
    /**
     * The Manager.
     */
    private TransactionManager manager = new TransactionManager();

    /**
     * Find results search with limit list.
     *
     * @param itemId           the item id
     * @param limit            the limit
     * @param offset           the offset
     * @param pathForTempFiles the path for temp files
     * @return the list
     * @throws ServiceException the service exception
     */
    @Override
    public List<Item> findResultsSearchWithLimit(List<Integer> itemId, int limit, int offset, String pathForTempFiles) throws ServiceException {
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
            manager.endTransaction();
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

    /**
     * Find all with limit list.
     *
     * @param pathForTempFiles the path for temp files
     * @param limit            the limit
     * @param offset           the offset
     * @return the list
     * @throws ServiceException the service exception
     */
    @Override
    public List<Item> findAllWithLimit(String pathForTempFiles, int limit, int offset) throws ServiceException {
        List<Item> result;
        try {
            manager.beginTransaction(itemDao);
            result = itemDao.findAllWithLimit(pathForTempFiles, limit, offset);
            manager.endTransaction();
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

    /**
     * Find paid items list.
     *
     * @param userId   the user id
     * @param filePath the file path
     * @return the list
     * @throws ServiceException the service exception
     */
    @Override
    public List<Item> findPaidItems(long userId, String filePath) throws ServiceException {
        List<Item> items;
        try {
            manager.beginTransaction(itemDao);
            items = itemDao.findPaidItems(userId, filePath);
            manager.endTransaction();
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

    /**
     * Find optional.
     *
     * @param id          the id
     * @param pathForFile the path for file
     * @return the optional
     * @throws ServiceException the service exception
     */
    @Override
    public Optional<Item> find(Long id, String pathForFile) throws ServiceException {
        Optional<Item> item;
        try {
            manager.beginTransaction(itemDao);
            item = itemDao.find(id, pathForFile);
            manager.endTransaction();
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

    /**
     * Create item.
     *
     * @param itemParameters the item parameters
     * @return the item
     * @throws ServiceException the service exception
     */
    @Override
    public Item create(Map<String, Object> itemParameters) throws ServiceException {
        Item item = setParameters(itemParameters);
        try {
            manager.beginTransaction(itemDao, bitmapDao);
            List<Bitmap> bitmaps = bitmapDao.findAll();
            updateItemFilter(bitmaps, item);
            item = itemDao.create(item);
            for (Bitmap bitmap : bitmaps) {
                bitmapDao.update(bitmap);
            }
            manager.endTransaction();
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

    /**
     * Sets parameters.
     *
     * @param itemParameters the item parameters
     * @return the parameters
     */
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

    /**
     * Update item filter.
     *
     * @param bitmaps the bitmaps
     * @param item    the item
     */
    private void updateItemFilter(List<Bitmap> bitmaps, Item item) {
        List<String> possibleCriteriaPrice = selectOnPrice(item.getPrice());
        for (Bitmap bitmap : bitmaps) {
            int[] data = bitmap.getData();
            int[] newData = new int[data.length + 1];
            System.arraycopy(data, 0, newData, 0, data.length);
            if (bitmap.getName().equals(item.getType()) || possibleCriteriaPrice.contains(bitmap.getName())) {
                newData[newData.length - 1] = 1;
            }
            bitmap.setData(newData);
        }
    }

    /**
     * Select on price list.
     *
     * @param price the price
     * @return the list
     */
    private List<String> selectOnPrice(BigDecimal price) {
        price = price != null ? price : BigDecimal.ZERO;
        List<String> criteriaPrice = new ArrayList<>();
        if (price.compareTo(LOW_BORDER_PRICE) <= 0) {
            criteriaPrice.add(LOW_PARAMETER_NAME);
        }
        if (price.compareTo(MEDIUM_BORDER_PRICE) <= 0) {
            criteriaPrice.add(MEDIUM_PARAMETER_NAME);
        }
        if (price.compareTo(HIGH_BORDER_PRICE) <= 0) {
            criteriaPrice.add(HIGH_PARAMETER_NAME);
        }
        return criteriaPrice;
    }

    /**
     * Find count comments for item map.
     *
     * @param items the items
     * @return the map
     * @throws ServiceException the service exception
     */
    @Override
    public Map<Long, Integer> findCountCommentsForItem(List<Item> items) throws ServiceException {
        Map<Long, Integer> result = new HashMap<>();
        try {
            manager.beginTransaction(commentDao);
            for (Item item : items) {
                int count = commentDao.countCommentForItem(item.getItemId());
                result.put(item.getItemId(), count);
            }
            manager.endTransaction();
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

    /**
     * Find comment on item list.
     *
     * @param itemId the item id
     * @param status the status
     * @return the list
     * @throws ServiceException the service exception
     */
    @Override
    public List<Comment> findCommentOnItem(long itemId, Comment.CommentStatus status) throws ServiceException {
        List<Comment> comment;
        try {
            manager.beginTransaction(itemDao);
            comment = commentDao.findByItemId(itemId, status);
            manager.endTransaction();
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

    /**
     * Find by comment map.
     *
     * @param comments the comments
     * @return the map
     * @throws ServiceException the service exception
     */
    @Override
    public Map<Long, Item> findByComment(List<Comment> comments) throws ServiceException {
        Map<Long, Item> items = new HashMap<>();
        try {
            manager.beginTransaction(itemDao);
            for (Comment comment : comments) {
                Optional<Item> optionalItem = itemDao.findByCommentId(comment.getCommentId());
                optionalItem.ifPresent(item -> items.put(comment.getCommentId(), item));
            }
            manager.endTransaction();
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

    /**
     * Count all item int.
     *
     * @return the int
     * @throws ServiceException the service exception
     */
    @Override
    public int countAllItem() throws ServiceException {
        int result;
        try {
            manager.beginTransaction(itemDao);
            result = itemDao.countAllItem();
            manager.endTransaction();
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
}