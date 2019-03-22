package by.chmut.giftit.dao;

import by.chmut.giftit.entity.Cart;

import java.util.List;

/**
 * The interface Cart dao implements the access mechanism
 * required to work with the data source specifically for Cart entity.
 *
 * @author Sergei Chmut.
 */
public interface CartDao extends Dao<Long, Cart> {

    /**
     * Find list of all Cart by user id .
     *
     * @param userId the user id
     * @return the list of Cart
     * @throws DaoException if find all by user id can't be handled
     */
    List<Cart> findAllByUserId(long userId) throws DaoException;

    /**
     * Delete all Carts with such user id.
     *
     * @param userId the user id
     * @return true if success otherwise false
     * @throws DaoException if delete all by user id can't be handled
     */
    boolean deleteAllByUserId(long userId) throws DaoException;

    /**
     * Sets order id in Cart with such cart id.
     *
     * @param cartId  the cart where order id will be set
     * @param orderId the order id to be installed
     * @return true if done otherwise false
     * @throws DaoException if set order id can't be handled
     */
    boolean setOrderId(long cartId, long orderId) throws DaoException;
}
