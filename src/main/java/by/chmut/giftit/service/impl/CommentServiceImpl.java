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

public class CommentServiceImpl implements CommentService {

    private static final Logger logger = LogManager.getLogger();

    private CommentDao commentDao = DaoFactory.getInstance().getCommentDao();
    private UserDao userDao = DaoFactory.getInstance().getUserDao();
    private TransactionManager manager = new TransactionManager();

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
