package by.chmut.giftit.service;

import by.chmut.giftit.entity.Comment;
import by.chmut.giftit.entity.User;

import java.util.List;
import java.util.Map;

/**
 * The interface Comment service provides service
 * methods associated with Comment entity.
 *
 * @author Sergei Chmut.
 */
public interface CommentService {

    /**
     * Find comments by user id.
     *
     * @param userId the user id
     * @return the list of comments
     * @throws ServiceException if find by user id can't be handled
     */
    List<Comment> findByUserId(long userId) throws ServiceException;

    /**
     * Find new comments for moderating.
     *
     * @return the list of comments
     * @throws ServiceException if find comment to moderate can't be handled
     */
    List<Comment> findCommentToModerate() throws ServiceException;

    /**
     * Moderating comment on moderator command.
     *
     * @param moderatorCommand the moderator command
     * @param commentId        the comment id
     * @param users            the users
     * @return true if command done otherwise false
     * @throws ServiceException if moderate can't be handled
     */
    boolean moderate(String moderatorCommand, String commentId, Map<Long, User> users) throws ServiceException;
}
