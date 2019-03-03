package by.chmut.giftit.service;

import by.chmut.giftit.entity.Question;

import java.util.List;

public interface QuestionService {

    List<Question> find(long userId) throws ServiceException;
}
