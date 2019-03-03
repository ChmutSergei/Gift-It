package by.chmut.giftit.dao;

import by.chmut.giftit.entity.Question;

import java.util.List;

public interface QuestionDao extends Dao<Long, Question> {

    List<Question> find(long userId) throws DaoException;
}
