package by.chmut.giftit.dao;

import by.chmut.giftit.entity.Question;

import java.util.List;

/**
 * The interface Question dao.
 *
 * @author Sergei Chmut.
 */
public interface QuestionDao extends Dao<Long, Question> {

    /**
     * Find by user id list.
     *
     * @param userId the user id
     * @return the list
     * @throws DaoException the dao exception
     */
    List<Question> findByUserId(long userId) throws DaoException;

    /**
     * Find unanswered list.
     *
     * @return the list
     * @throws DaoException the dao exception
     */
    List<Question> findUnanswered() throws DaoException;

}
