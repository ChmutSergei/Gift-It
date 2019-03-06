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

    @Override
    public List<Question> findActualQuestion() throws ServiceException {
        List<Question> questions;
        try {
            manager.beginTransaction(questionDao);
            questions = questionDao.findUnanswered();
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
            manager.endTransaction(questionDao);
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

    @Override
    public List<Question> findAllQuestion() throws ServiceException {
        List<Question> questions;
        try {
            manager.beginTransaction(questionDao);
            questions = questionDao.findAll();
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
