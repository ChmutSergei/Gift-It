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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * The Question service class.
 *
 * @author Sergei Chmut.
 */
public class QuestionServiceImpl implements QuestionService {

    /**
     * The constant logger.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * The Question dao.
     */
    private QuestionDao questionDao = DaoFactory.getInstance().getQuestionDao();
    /**
     * The Manager.
     */
    private TransactionManager manager = new TransactionManager();

    /**
     * Find by user id list.
     *
     * @param userId the user id
     * @return the list
     * @throws ServiceException the service exception
     */
    @Override
    public List<Question> findByUserId(long userId) throws ServiceException {
        List<Question> questions;
        try {
            manager.beginTransaction(questionDao);
            questions = questionDao.findByUserId(userId);
            manager.endTransaction();
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

    /**
     * Find actual question list.
     *
     * @return the list
     * @throws ServiceException the service exception
     */
    @Override
    public List<Question> findActualQuestion() throws ServiceException {
        List<Question> questions;
        try {
            manager.beginTransaction(questionDao);
            questions = questionDao.findUnanswered();
            manager.endTransaction();
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

    /**
     * Sets answer.
     *
     * @param questionId the question id
     * @param answer     the answer
     * @return the answer
     * @throws ServiceException the service exception
     */
    @Override
    public Question setAnswer(long questionId, String answer) throws ServiceException {
        Question result;
        try {
            manager.beginTransaction(questionDao);
            Optional<Question> optionalQuestion = questionDao.findEntity(questionId);
            Question question = optionalQuestion.get();
            question.setResponse(answer);
            question.setResponseDate(LocalDate.now());
            result = questionDao.update(question);
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
     * Find all question list.
     *
     * @return the list
     * @throws ServiceException the service exception
     */
    @Override
    public List<Question> findAllQuestion() throws ServiceException {
        List<Question> questions;
        try {
            manager.beginTransaction(questionDao);
            questions = questionDao.findAll();
            manager.endTransaction();
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
