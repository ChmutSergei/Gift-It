package by.chmut.giftit.dao;

import by.chmut.giftit.entity.Item;

import java.util.List;

public interface ItemDao extends Dao<Long, Item> {

    List<Item> findAll(String filePath, int limit, int offset) throws DaoException;

    Item findEntity(Long id, String filePath) throws DaoException;
}