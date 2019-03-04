package by.chmut.giftit.service;

import by.chmut.giftit.entity.Item;
import by.chmut.giftit.entity.Order;
import by.chmut.giftit.entity.User;

import java.util.List;
import java.util.Map;

public interface OrderService {

    List<Order> findPaidOrder() throws ServiceException;

    Map<Long,User> findUserForOrders(List<Order> orders) throws ServiceException;

    Map<Long, List<Item>> findItemForOrders(List<Order> orders) throws ServiceException;
}
