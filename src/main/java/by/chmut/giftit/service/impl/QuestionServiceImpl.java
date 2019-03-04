package by.chmut.giftit.service.impl;

import by.chmut.giftit.dao.DaoException;
import by.chmut.giftit.dao.DaoFactory;
import by.chmut.giftit.dao.QuestionDao;
import by.chmut.giftit.dao.TransactionManager;
import by.chmut.giftit.entity.Question;
import by.chmut.giftit.service.QuestionService;
import by.chmut.giftit.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class QuestionServiceImpl implements QuestionService {

    private static final Logger logger = LogManager.getLogger();

    private QuestionDao questionDao = DaoFactory.getInstance().getQuestionDao();
    private TransactionManager manager = new TransactionManager();

    @Override
    public List<Question> find(long userId) throws ServiceException {
        List<Question> questions;
        try {
            manager.beginTransaction(questionDao);
            questions = questionDao.find(userId);
            manager.endTransaction(questionDao);
        } catch (DaoException exception) {
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            throw new ServiceException(exception);
        }
        return questions;
    }
}
