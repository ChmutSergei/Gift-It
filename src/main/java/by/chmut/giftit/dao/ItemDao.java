package by.chmut.giftit.dao;

import by.chmut.giftit.entity.Item;

import java.util.List;
import java.util.Optional;

public interface ItemDao extends Dao<Long, Item> {//TODO  про одно название

    List<Item> findAll(String filePath, int limit, int offset) throws DaoException;

    Optional<Item> find(Long id, String filePath) throws DaoException;

    List<Item> findPaidItems(long userId, String filePath) throws DaoException;

    Optional<Item> find(long commentId) throws DaoException;

    List<Item> findForOrder(long orderId) throws DaoException;
}