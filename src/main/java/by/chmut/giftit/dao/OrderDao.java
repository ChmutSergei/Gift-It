package by.chmut.giftit.dao;

import by.chmut.giftit.entity.Order;

import java.util.List;

/**
 * The interface Order dao implements the access mechanism
 * required to work with the data source specifically for Order entity.
 *
 * @author Sergei Chmut.
 */
public interface OrderDao extends Dao<Long, Order> {

    /**
     * Find list of all orders with Paid status.
     *
     * @return the list of orders
     * @throws DaoException if find paid orders can't be handled
     */
    List<Order> findPaidOrder() throws DaoException;

    /**
     * Sets paid status for order with such id.
     *
     * @param orderId the order id
     * @return true with success otherwise false
     * @throws DaoException if set paid for order can't be handled
     */
    boolean setPaidStatus(long orderId) throws DaoException;
}