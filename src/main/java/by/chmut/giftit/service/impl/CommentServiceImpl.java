package by.chmut.giftit.service.impl;

import by.chmut.giftit.dao.*;
import by.chmut.giftit.entity.Comment;
import by.chmut.giftit.entity.User;
import by.chmut.giftit.service.CommentService;
import by.chmut.giftit.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static by.chmut.giftit.constant.AttributeName.*;

/**
 * The Comment service class implements business logic methods
 * for comment entity.
 *
 * @author Sergei Chmut.
 */
public class CommentServiceImpl implements CommentService {

    /**
     * The logger for logging possible errors.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * The Comment dao provides access to the database for Comment entity.
     */
    private CommentDao commentDao = DaoFactory.getInstance().getCommentDao();
    /**
     * The User dao provides access to the database for User entity.
     */
    private UserDao userDao = DaoFactory.getInstance().getUserDao();
    /**
     * The transaction manager provides preparation for conducting and confirming transactions.
     */
    private TransactionManager manager = new TransactionManager();

    /**
     * Find comments by user id.
     *
     * @param userId the user id
     * @return the list of comments
     * @throws ServiceException if find by user id can't be handled
     */
    @Override
    public List<Comment> findByUserId(long userId) throws ServiceException {
        List<Comment> comments;
        try {
            manager.beginTransaction(commentDao);
            comments = commentDao.findByUserId(userId);
            manager.endTransaction();
        } catch (DaoException exception) {
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            throw new ServiceException(exception);
        }
        return comments;
    }

    /**
     * Find new comments for moderating.
     *
     * @return the list of comments
     * @throws ServiceException if find comment to moderate can't be handled
     */
    @Override
    public List<Comment> findCommentToModerate() throws ServiceException {
        List<Comment> comments;
        try {
            manager.beginTransaction(commentDao);
            comments = commentDao.findToModerate();
            manager.endTransaction();
        } catch (DaoException exception) {
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            throw new ServiceException(exception);
        }
        return comments;
    }

    /**
     * Moderating comment on moderator command.
     * The method defines an administrative command -
     * delete a comment or delete and block a user (2 options by date).
     * If there is a lock, performs a call to
     * the database with the date saved blocked until.
     * Returns true if successful.
     *
     * @param moderatorCommand the moderator command
     * @param commentId        the comment id
     * @param users            the users
     * @return true if command done otherwise false
     * @throws ServiceException if moderate can't be handled
     */
    @Override
    public boolean moderate(String moderatorCommand, String commentId, Map<Long, User> users) throws ServiceException {
        boolean result = true;
        long id = commentId != null ? Long.parseLong(commentId) : 0;
        if (moderatorCommand == null || id == 0) {
            return false;
        }
        int countDayForBlock = executeModeratorCommand(moderatorCommand);
        try {
            manager.beginTransaction(commentDao, userDao);
            Optional<Comment> optionalComment = commentDao.findEntity(id);
            Comment comment = optionalComment.get();
            comment.setCommentStatus(Comment.CommentStatus.BLOCKED);
            commentDao.update(comment);
            if (countDayForBlock > 0) {
                User user = users.get(id);
                result = userDao.blockForDays(user.getUserId(), countDayForBlock);
            }
            manager.endTransaction();
        } catch (DaoException exception) {
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            throw new ServiceException(exception);
        }
        return result;
    }

    /**
     * Execute moderator command.
     *
     * @param moderatorCommand the moderator command
     * @return the count day for blocking (if zero - blocked not required)
     * @throws ServiceException the service exception
     */
    private int executeModeratorCommand(String moderatorCommand) throws ServiceException {
        int result;
        switch (moderatorCommand) {
            case MODERATE_DELETE_COMMAND:
                result = 0;
                break;
            case MODERATE_DELETE_BLOCK_LOW_COMMAND:
                result = LOW_LEVEL_BLOCKING_DAYS;
                break;
            case MODERATE_DELETE_BLOCK_HIGH_COMMAND:
                result = HIGH_LEVEL_BLOCKING_DAYS;
                break;
            default:
                throw new ServiceException("Error - impossible state: incorrect moderator command");
        }
        return result;
    }
}
