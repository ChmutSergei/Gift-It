package by.chmut.giftit.dao;

import by.chmut.giftit.entity.Item;

import java.util.List;
import java.util.Optional;

/**
 * The interface Item dao.
 *
 * @author Sergei Chmut.
 */
public interface ItemDao extends Dao<Long, Item> {

    /**
     * Find all with limit list.
     *
     * @param filePath the file path
     * @param limit    the limit
     * @param offset   the offset
     * @return the list
     * @throws DaoException the dao exception
     */
    List<Item> findAllWithLimit(String filePath, int limit, int offset) throws DaoException;

    /**
     * Find optional.
     *
     * @param itemId   the item id
     * @param filePath the file path
     * @return the optional
     * @throws DaoException the dao exception
     */
    Optional<Item> find(Long itemId, String filePath) throws DaoException;

    /**
     * Find paid items list.
     *
     * @param userId   the user id
     * @param filePath the file path
     * @return the list
     * @throws DaoException the dao exception
     */
    List<Item> findPaidItems(long userId, String filePath) throws DaoException;

    /**
     * Find by comment id optional.
     *
     * @param commentId the comment id
     * @return the optional
     * @throws DaoException the dao exception
     */
    Optional<Item> findByCommentId(long commentId) throws DaoException;

    /**
     * Find for order list.
     *
     * @param orderId the order id
     * @return the list
     * @throws DaoException the dao exception
     */
    List<Item> findForOrder(long orderId) throws DaoException;

    /**
     * Count all item int.
     *
     * @return the int
     * @throws DaoException the dao exception
     */
    int countAllItem() throws DaoException;
}