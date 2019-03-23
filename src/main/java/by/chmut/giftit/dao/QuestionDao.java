package by.chmut.giftit.dao;

import by.chmut.giftit.entity.Question;

import java.util.List;

/**
 * The interface Question dao implements the access mechanism
 * required to work with the data source specifically for Question entity.
 *
 * @author Sergei Chmut.
 */
public interface QuestionDao extends Dao<Long, Question> {

    /**
     * Find all questions with such user id.
     *
     * @param userId the user id
     * @return the list of question
     * @throws DaoException if find question by user id can't be handled
     */
    List<Question> findByUserId(long userId) throws DaoException;

    /**
     * Find all unanswered questions.
     *
     * @return the list of question
     * @throws DaoException if find unanswered question can't be handled
     */
    List<Question> findUnanswered() throws DaoException;

}
