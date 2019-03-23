package by.chmut.giftit.service.impl;

import by.chmut.giftit.dao.*;
import by.chmut.giftit.entity.Comment;
import by.chmut.giftit.entity.Item;
import by.chmut.giftit.entity.Question;
import by.chmut.giftit.entity.User;
import by.chmut.giftit.service.AjaxService;
import by.chmut.giftit.service.ServiceException;
import by.chmut.giftit.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.Optional;

/**
 * The Ajax service class.
 *
 * @author Sergei Chmut.
 */
public class AjaxServiceImpl implements AjaxService {

    /**
     * The constant logger.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * The User service.
     */
    private UserService userService = new UserServiceImpl();
    /**
     * The User dao.
     */
    private UserDao userDao = DaoFactory.getInstance().getUserDao();
    /**
     * The Comment dao.
     */
    private CommentDao commentDao = DaoFactory.getInstance().getCommentDao();
    /**
     * The Question dao.
     */
    private QuestionDao questionDao = DaoFactory.getInstance().getQuestionDao();
    /**
     * The Item dao.
     */
    private ItemDao itemDao = DaoFactory.getInstance().getItemDao();
    /**
     * The Manager.
     */
    private TransactionManager manager = new TransactionManager();

    /**
     * Update user data boolean.
     *
     * @param user the user
     * @return the boolean
     */
    @Override
    public boolean updateUserData(User user) {
        boolean result = true;
        try {
            manager.beginTransaction(userDao);
            userDao.update(user);
            manager.endTransaction();
        } catch (DaoException exception) {
            logger.error("Error when try to update user phone and address");
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            result = false;
        }
        return result;
    }

    /**
     * Delete comment boolean.
     *
     * @param commentId the comment id
     * @return the boolean
     */
    @Override
    public boolean deleteComment(long commentId) {
        boolean result;
        try {
            manager.beginTransaction(commentDao);
            result = commentDao.delete(commentId);
            manager.endTransaction();
        } catch (DaoException e) {
            logger.error("Error when try to delete comment");
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            result = false;
        }
        return result;
    }

    /**
     * Add comment boolean.
     *
     * @param itemId         the item id
     * @param userId         the user id
     * @param commentMessage the comment message
     * @return the boolean
     */
    @Override
    public boolean addComment(long itemId, long userId, String commentMessage) {
        boolean result = true;
        Comment comment = new Comment();
        comment.setItemId(itemId);
        comment.setUserId(userId);
        comment.setDate(LocalDate.now());
        comment.setMessage(commentMessage);
        comment.setCommentStatus(Comment.CommentStatus.NEW);
        try {
            manager.beginTransaction(commentDao);
            comment = commentDao.create(comment);
            manager.endTransaction();
            if (comment.getCommentId() == 0) {
                result = false;
            }
        } catch (DaoException e) {
            logger.error("Error when try to add comment");
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            result = false;
        }
        return result;
    }

    /**
     * Change item status boolean.
     *
     * @param itemId   the item id
     * @param filePath the file path
     * @return the boolean
     */
    @Override
    public boolean changeItemStatus(long itemId, String filePath) {
        boolean result = true;
        try {
            Optional<Item> optionalItem = itemDao.find(itemId, filePath);
            if (!optionalItem.isPresent()) {
                return false;
            }
            Item item = optionalItem.get();
            boolean status = item.isActive();
            item.setActive(!status);
            manager.beginTransaction(itemDao);
            itemDao.update(item);
            manager.endTransaction();
        } catch (DaoException e) {
            logger.error("Error when try to change status");
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            result = false;
        }
        return result;
    }

    /**
     * Accept question boolean.
     *
     * @param userId  the user id
     * @param message the message
     * @return the boolean
     */
    @Override
    public boolean acceptQuestion(long userId, String message) {
        boolean result = true;
        try {
            Question question = new Question();
            question.setUserId(userId);
            question.setRequestDate(LocalDate.now());
            question.setRequest(message);
            manager.beginTransaction(questionDao);
            questionDao.create(question);
            manager.endTransaction();
        } catch (DaoException exception) {
            logger.error("Error when try to accept question");
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            result = false;
        }
        return result;
    }

    /**
     * Check username on exist boolean.
     *
     * @param username the username
     * @return the boolean
     */
    @Override
    public boolean checkUsernameOnExist(String username) {
        boolean result = true;
        try {
            Optional<User> user = userService.find(username);
            if (!user.isPresent()) {
                result = false;
            }
        } catch (ServiceException exception) {
            logger.error(exception);
            result = false;
        }
        return result;
    }

    /**
     * Accept comment boolean.
     *
     * @param commentId the comment id
     * @return the boolean
     */
    @Override
    public boolean acceptComment(long commentId) {
        boolean result = true;
        try {
            manager.beginTransaction(commentDao);
            Optional<Comment> optionalComment = commentDao.findEntity(commentId);
            if (optionalComment.isPresent()) {
                Comment comment = optionalComment.get();
                comment.setCommentStatus(Comment.CommentStatus.ACTIVE);
                commentDao.update(comment);
            } else {
                result = false;
            }
            manager.endTransaction();
        } catch (DaoException exception) {
            logger.error("Error when try to accept comment");
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            result = false;
        }
        return result;
    }
}
