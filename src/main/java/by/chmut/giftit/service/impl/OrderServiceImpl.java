package by.chmut.giftit.service.impl;

import by.chmut.giftit.dao.*;
import by.chmut.giftit.entity.Item;
import by.chmut.giftit.entity.Order;
import by.chmut.giftit.entity.User;
import by.chmut.giftit.service.OrderService;
import by.chmut.giftit.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LogManager.getLogger();

    private OrderDao orderDao = DaoFactory.getInstance().getOrderDao();
    private UserDao userDao = DaoFactory.getInstance().getUserDao();
    private ItemDao itemDao = DaoFactory.getInstance().getItemDao();
    private CartDao cartDao = DaoFactory.getInstance().getCartDao();
    private TransactionManager manager = new TransactionManager();

    @Override
    public List<Order> findPaidOrder() throws ServiceException {
        List<Order> paidOrders;
        try {
            manager.beginTransaction(orderDao);
            paidOrders = orderDao.findPaidOrder();
            manager.endTransaction(orderDao);
        } catch (DaoException exception) {
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            throw new ServiceException(exception);
        }
        return paidOrders;
    }

    @Override
    public Map<Long, User> findUserForOrders(List<Order> orders) throws ServiceException {
        Map<Long, User> users = new HashMap<>();
        try {
            manager.beginTransaction(userDao);
            for (Order order:orders) {
                Optional<User> optionalUser = userDao.findEntity(order.getUserId());
                optionalUser.ifPresent(user -> users.put(order.getOrderId(), user));
            }
            manager.endTransaction(userDao);
        } catch (DaoException exception) {
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            throw new ServiceException(exception);
        }
        return users;
    }

    @Override
    public Map<Long, List<Item>> findItemForOrders(List<Order> orders) throws ServiceException {
        Map<Long, List<Item>> items = new HashMap<>();
        try {
            manager.beginTransaction(itemDao);
            for (Order order:orders) {
                List<Item> results = itemDao.findForOrder(order.getOrderId());
                items.put(order.getOrderId(), results);
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
        return items;
    }

}
