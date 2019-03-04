package by.chmut.giftit.dao;

import by.chmut.giftit.entity.Order;

import java.util.List;

public interface OrderDao extends Dao<Long, Order> {

    List<Order> findPaidOrder() throws DaoException;

}