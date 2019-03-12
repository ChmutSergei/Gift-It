package by.chmut.giftit.service;

import by.chmut.giftit.entity.Question;

import java.util.List;

public interface QuestionService {

    List<Question> findByUserId(long userId) throws ServiceException;

    List<Question> findActualQuestion() throws ServiceException;

    Question setAnswer(long questionId, String answer) throws ServiceException;

    List<Question> findAllQuestion() throws ServiceException;
}
