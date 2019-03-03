package by.chmut.giftit.service.impl;

import by.chmut.giftit.dao.CommentDao;
import by.chmut.giftit.dao.DaoException;
import by.chmut.giftit.dao.DaoFactory;
import by.chmut.giftit.dao.TransactionManager;
import by.chmut.giftit.entity.Comment;
import by.chmut.giftit.service.CommentService;
import by.chmut.giftit.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class CommentServiceImpl implements CommentService {

    private static final Logger logger = LogManager.getLogger();

    private CommentDao commentDao = DaoFactory.getInstance().getCommentDao();
    private TransactionManager manager = new TransactionManager();

    @Override
    public List<Comment> find(long userId) throws ServiceException {
        List<Comment> comments;
        try {
            manager.beginTransaction(commentDao);
            comments = commentDao.findByUserId(userId);
            manager.endTransaction(commentDao);
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
}
