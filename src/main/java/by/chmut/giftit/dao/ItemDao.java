package by.chmut.giftit.dao;

import by.chmut.giftit.entity.Item;

import java.util.List;
import java.util.Optional;

/**
 * The interface Item dao implements the access mechanism
 * required to work with the data source specifically for Item entity.
 *
 * @author Sergei Chmut.
 */
public interface ItemDao extends Dao<Long, Item> {

    /**
     * Find list of all items from offset with limit.
     *
     * @param filePath the file path for item image
     * @param limit    the limit of item
     * @param offset   return item from offset
     * @return the list of items
     * @throws DaoException if find all items can't be handled
     */
    List<Item> findAllWithLimit(String filePath, int limit, int offset) throws DaoException;

    /**
     * Find item by id.
     *
     * @param itemId   the item id
     * @param filePath the file path for item image
     * @return the optional item
     * @throws DaoException if find item can't be handled
     */
    Optional<Item> find(Long itemId, String filePath) throws DaoException;

    /**
     * Find list of paid items with such user id.
     *
     * @param userId   the user id
     * @param filePath the file path for item image
     * @return the list of items
     * @throws DaoException if find paid items can't be handled
     */
    List<Item> findPaidItems(long userId, String filePath) throws DaoException;

    /**
     * Find item by comment id.
     *
     * @param commentId the comment id
     * @return the optional item
     * @throws DaoException if find items by comment id can't be handled
     */
    Optional<Item> findByCommentId(long commentId) throws DaoException;

    /**
     * Find list of item with such order.
     *
     * @param orderId the order id
     * @return the list of items
     * @throws DaoException if find items for order can't be handled
     */
    List<Item> findForOrder(long orderId) throws DaoException;

    /**
     * Count all item in database.
     *
     * @return the count of item
     * @throws DaoException if count all items can't be handled
     */
    int countAllItem() throws DaoException;
}