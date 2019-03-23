package by.chmut.giftit.service;

import by.chmut.giftit.entity.Question;

import java.util.List;

/**
 * The interface QuestionService provides service
 * methods associated with Question entity.
 *
 * @author Sergei Chmut.
 */
public interface QuestionService {

    /**
     * Find all the questions asked by this user.
     *
     * @param userId the user id
     * @return the list of question
     * @throws ServiceException if find questions by user id can't be handled
     */
    List<Question> findByUserId(long userId) throws ServiceException;

    /**
     * Find all new questions without answer.
     *
     * @return the list of question
     * @throws ServiceException if an exception occurs while find actual question
     */
    List<Question> findActualQuestion() throws ServiceException;

    /**
     * Sets answer on the question.
     *
     * @param questionId the question id
     * @param answer     the answer
     * @return the updated question
     * @throws ServiceException if sets answer can't be handled
     */
    Question setAnswer(long questionId, String answer) throws ServiceException;

    /**
     * Find all the questions asked.
     *
     * @return the list of question
     * @throws ServiceException if find all questions can't be handled
     */
    List<Question> findAllQuestion() throws ServiceException;
}
