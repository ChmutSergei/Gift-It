package by.chmut.giftit.service;

import by.chmut.giftit.entity.Comment;
import by.chmut.giftit.entity.Item;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ItemService {

    Optional<Item> find(Long id, String pathForFile) throws ServiceException;

    Item create(Map<String, Object> itemParameters) throws ServiceException;

    List<Item> findByIdWithLimit(List<Integer> itemId, int limit, int offset, String pathForTempFiles) throws ServiceException;

    List<Item> findAllWithLimit(String pathForTempFiles, int limit, int offset) throws ServiceException;

    List<Item> findPaidItems(long userId, String filePath) throws ServiceException;

    Map<Long,Integer> findCountCommentsForItem(List<Item> items) throws ServiceException;

    List<Comment> findCommentOnItem(long id, Comment.CommentStatus status) throws ServiceException;

    Map<Long, Item> findByComment(List<Comment> comments) throws ServiceException;
}
