package by.chmut.giftit.dao;

import by.chmut.giftit.entity.Order;

import java.util.List;

/**
 * The interface Order dao.
 *
 * @author Sergei Chmut.
 */
public interface OrderDao extends Dao<Long, Order> {

    /**
     * Find paid order list.
     *
     * @return the list
     * @throws DaoException the dao exception
     */
    List<Order> findPaidOrder() throws DaoException;

    /**
     * Sets paid status.
     *
     * @param orderId the order id
     * @return the paid status
     * @throws DaoException the dao exception
     */
    boolean setPaidStatus(long orderId) throws DaoException;
}