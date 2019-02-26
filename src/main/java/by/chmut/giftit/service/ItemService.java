package by.chmut.giftit.service;

import by.chmut.giftit.entity.Item;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface ItemService {

    boolean create(Item item) throws ServiceException;

    Item find(Long id) throws ServiceException;

    Item update(Item item) throws ServiceException;

    boolean delete(Serializable id) throws ServiceException;

    boolean createItem(Map<String, Object> itemParameters) throws ServiceException;

    List<Item> findResultOfFilterItems(List<Integer> itemId, int limit, int offset, String pathForTempFiles) throws ServiceException;

    List<Item> findAll(String pathForTempFiles, int limit, int offset) throws ServiceException;

    Map<Long,Integer> findCommentCountForItem(List<Item> items) throws ServiceException;

}
