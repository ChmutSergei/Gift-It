package by.chmut.giftit.service;

import by.chmut.giftit.criteria.Criteria;
import by.chmut.giftit.entity.Item;

import java.io.Serializable;
import java.util.List;

public interface ItemService {

    boolean create(Item item) throws ServiceException;

    Item find(Long id) throws ServiceException;

    Item update(Item item) throws ServiceException;

    boolean delete(Serializable id) throws ServiceException;

    List<Item> find(List<Criteria> searchCriteria) throws ServiceException;

}
