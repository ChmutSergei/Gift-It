package by.chmut.giftit.service;

import by.chmut.giftit.entity.Comment;
import by.chmut.giftit.entity.Item;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The interface ItemService provides service
 * methods associated with Item entity.
 *
 * @author Sergei Chmut.
 */
public interface ItemService {

    /**
     * Find item by id.
     *
     * @param id          the item id
     * @param pathForFile the path for image file
     * @return the optional
     * @throws ServiceException if find by id can't be handled
     */
    Optional<Item> find(Long id, String pathForFile) throws ServiceException;

    /**
     * Create item and save in database.
     *
     * @param itemParameters the item parameters
     * @return the item
     * @throws ServiceException if item can't be created
     */
    Item create(Map<String, Object> itemParameters) throws ServiceException;

    /**
     * Find results user's search by the list of item id with limit count on the page.
     *
     * @param itemId           the item id
     * @param limit            the limit of items
     * @param offset           the offset in results of search
     * @param pathForTempFiles the path for temp image files
     * @return the list of items
     * @throws ServiceException if find results with limit can't be handled
     */
    List<Item> findResultsSearchWithLimit(List<Integer> itemId, int limit, int offset, String pathForTempFiles) throws ServiceException;

    /**
     * Find all items with limit count on the page.
     *
     * @param pathForTempFiles the path for temp image files
     * @param limit            the limit of items
     * @param offset           the offset in results of search
     * @return the list of items
     * @throws ServiceException if find all items can't be handled
     */
    List<Item> findAllWithLimit(String pathForTempFiles, int limit, int offset) throws ServiceException;

    /**
     * Find list of paid items.
     *
     * @param userId   the user id
     * @param filePath the file path for image files
     * @return the list of items
     * @throws ServiceException if find paid items can't be handled
     */
    List<Item> findPaidItems(long userId, String filePath) throws ServiceException;

    /**
     * Find count comments for each item.
     *
     * @param items the list of items
     * @return the map of pair - itemId:count
     * @throws ServiceException if find count of comments for item can't be handled
     */
    Map<Long, Integer> findCountCommentsForItem(List<Item> items) throws ServiceException;

    /**
     * Find comments for item with the given status.
     *
     * @param itemId     the item id
     * @param status the comment status for search
     * @return the list of comments
     * @throws ServiceException if find comments for item can't be handled
     */
    List<Comment> findCommentOnItem(long itemId, Comment.CommentStatus status) throws ServiceException;

    /**
     * Find items for those comments.
     *
     * @param comments the list of comments
     * @return the map of items
     * @throws ServiceException if find items by comment can't be handled
     */
    Map<Long, Item> findByComment(List<Comment> comments) throws ServiceException;

    /**
     * Count all items.
     *
     * @return the count
     * @throws ServiceException if count all items can't be handled
     */
    int countAllItem() throws ServiceException;
}
