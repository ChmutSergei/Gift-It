package by.chmut.giftit.dao;

import by.chmut.giftit.entity.Comment;

import java.util.List;

/**
 * The interface Comment dao implements the access mechanism
 * required to work with the data source specifically for Comment entity.
 *
 * @author Sergei Chmut.
 */
public interface CommentDao extends Dao<Long, Comment> {

    /**
     * Count all comment for Item with such item id.
     *
     * @param itemId the item id
     * @return the count off all comment for Item with such item id
     * @throws DaoException if count comment can't be handled
     */
    int countCommentForItem(long itemId) throws DaoException;

    /**
     * Find comments by item id with such status.
     *
     * @param itemId the item id
     * @param status the comment status
     * @return the list of comments
     * @throws DaoException if find comments can't be handled
     */
    List<Comment> findByItemId(long itemId, Comment.CommentStatus status) throws DaoException;

    /**
     * Find comments by user id list.
     *
     * @param userId the user id
     * @return the list of comments
     * @throws DaoException if find comments can't be handled
     */
    List<Comment> findByUserId(long userId) throws DaoException;

    /**
     * Find list of new comments to moderate.
     *
     * @return the list of comments
     * @throws DaoException if find comments can't be handled
     */
    List<Comment> findToModerate() throws DaoException;
}
