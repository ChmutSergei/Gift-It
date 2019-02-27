package by.chmut.giftit.service;

import by.chmut.giftit.entity.Comment;
import by.chmut.giftit.entity.Item;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface ItemService {

    Item find(Long id, String pathForFile) throws ServiceException;

    Item update(Item item) throws ServiceException;

    boolean delete(Serializable id) throws ServiceException;

    Item create(Map<String, Object> itemParameters) throws ServiceException;

    List<Item> findResultOfFilterItems(List<Integer> itemId, int limit, int offset, String pathForTempFiles) throws ServiceException;

    List<Item> findAll(String pathForTempFiles, int limit, int offset) throws ServiceException;

    Map<Long,Integer> findCommentCountForItem(List<Item> items) throws ServiceException;

    Comment findCommentOnItem(long id);
}
