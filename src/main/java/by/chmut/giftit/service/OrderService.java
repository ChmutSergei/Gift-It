package by.chmut.giftit.service;

import by.chmut.giftit.entity.Cart;
import by.chmut.giftit.entity.Item;
import by.chmut.giftit.entity.Order;
import by.chmut.giftit.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * The interface OrderService provides service
 * methods associated with Order entity.
 *
 * @author Sergei Chmut.
 */
public interface OrderService {

    /**
     * Find paid orders.
     *
     * @return the list of orders
     * @throws ServiceException if find paid order can't be handled
     */
    List<Order> findPaidOrder() throws ServiceException;

    /**
     * Find all users for list of orders.
     *
     * @param orders the list of orders
     * @return the map of users
     * @throws ServiceException if users for orders can't be handled
     */
    Map<Long, User> findUserForOrders(List<Order> orders) throws ServiceException;

    /**
     * Find all items for list of orders.
     *
     * @param orders the list of orders
     * @return the map of items
     * @throws ServiceException if find item for order can't be handled
     */
    Map<Long, List<Item>> findItemForOrders(List<Order> orders) throws ServiceException;

    /**
     * Payment order.
     *
     * @param paymentParameters the payment parameters
     * @param totalSum          the total sum
     * @param order             the order
     * @return true if success otherwise false
     * @throws ServiceException if payment can't be handled
     */
    boolean payment(Map<String, String> paymentParameters, BigDecimal totalSum, Order order) throws ServiceException;

    /**
     * Create new order and save.
     *
     * @param user         the user
     * @param carts        the list of carts
     * @param orderDetails the order details
     * @return the order
     * @throws ServiceException if order can't be created
     */
    Order create(User user, List<Cart> carts, String orderDetails) throws ServiceException;
}
