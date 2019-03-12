package by.chmut.giftit.dao;

import by.chmut.giftit.entity.Cart;
import by.chmut.giftit.entity.Item;

import java.util.List;

public interface CartDao extends Dao<Long, Cart> {

    List<Cart> findAllByUserId(long userId) throws DaoException;

    boolean deleteAllByUserId(long userId) throws DaoException;

    boolean setOrderId(long cartId, long orderId) throws DaoException;
}
