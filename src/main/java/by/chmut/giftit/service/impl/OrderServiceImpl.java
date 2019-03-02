package by.chmut.giftit.service.impl;

import by.chmut.giftit.dao.DaoException;
import by.chmut.giftit.dao.DaoFactory;
import by.chmut.giftit.dao.OrderDao;
import by.chmut.giftit.dao.TransactionManager;
import by.chmut.giftit.entity.Item;
import by.chmut.giftit.entity.Order;
import by.chmut.giftit.service.OrderService;
import by.chmut.giftit.service.ServiceException;

import java.util.List;

public class OrderServiceImpl implements OrderService {

    private OrderDao orderDao = DaoFactory.getInstance().getOrderDao();
    private TransactionManager manager = new TransactionManager();

    @Override
    public List<Item> findPaidItems(long userId) throws ServiceException {
        List<Item> items;
        try {
            manager.beginTransaction(orderDao);
//            List<Order> orders = orderDao.findPaidOrders(userId);
            manager.endTransaction(orderDao);
        } catch (DaoException exception) {
            throw new ServiceException(exception);
        }
        return null;
    }

}
