package by.chmut.giftit.dao;

import by.chmut.giftit.entity.Cart;

import java.util.List;

public interface CartDao extends Dao<Long, Cart> {

    List<Cart> findAll(long userId) throws DaoException;

    boolean deleteAll(long userId) throws DaoException;
}
