package by.chmut.giftit.dao.impl;

import by.chmut.giftit.dao.DaoException;
import by.chmut.giftit.dao.QuestionDao;
import by.chmut.giftit.entity.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDaoImpl implements QuestionDao {

    private static final String SELECT_ALL_QUESTIONS = "SELECT id, user_id, request, response, " +
            "request_date, response_date FROM Questions";
    private static final String SELECT_QUESTION_BY_ID = "SELECT id, user_id, request, response, " +
            "request_date, response_date FROM Questions WHERE id = ?";
    private static final String DELETE_QUESTION = "DELETE FROM Questions WHERE id=?";
    private static final String CREATE_QUESTION = "INSERT INTO Questions(user_id, request, response, " +
            "request_date, response_date) VALUES(?,?,?,?,?)";
    private static final String UPDATE_QUESTION = "UPDATE Questions SET user_id=?, request=?, response=?, " +
            "request_date=?, response_date=? WHERE id=?";

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Question> findAll() throws DaoException {
        List<Question> questions = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_ALL_QUESTIONS);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Question question = makeFromResultSet(resultSet);
                questions.add(question);
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with get all questions", exception);
        } finally {
            close(statement);
            close(resultSet);
        }
        return questions;
    }

    @Override
    public Question findEntity(Long id) throws DaoException {
        Question question = new Question();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_QUESTION_BY_ID);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                question = makeFromResultSet(resultSet);
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with get question by id", exception);
        } finally {
            close(statement);
            close(resultSet);
        }
        return question;
    }

    private Question makeFromResultSet(ResultSet resultSet) throws SQLException {
        Question question = new Question();
        question.setQuestionId(resultSet.getLong(1));
        question.setUserId(resultSet.getLong(2));
        question.setRequest(resultSet.getString(4));
        question.setResponse(resultSet.getString(5));
        question.setRequestDate(resultSet.getDate(6).toLocalDate());
        question.setResponseDate(resultSet.getDate(7).toLocalDate());
        return question;
    }

    @Override
    public boolean delete(Long id) throws DaoException {
        PreparedStatement statement = null;
        int result;
        try {
            statement = connection.prepareStatement(DELETE_QUESTION);
            statement.setLong(1, id);
            result = statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DaoException("Error with delete question", exception);
        } finally {
            close(statement);
        }
        return result > 0;
    }

    @Override
    public boolean delete(Question question) throws DaoException {
        return delete(question.getQuestionId());
    }

    @Override
    public Question create(Question question) throws DaoException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(CREATE_QUESTION, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, question.getUserId());
            statement.setString(2, question.getRequest());
            statement.setString(3, question.getResponse());
            statement.setDate(4, Date.valueOf(question.getRequestDate()));
            statement.setDate(5, Date.valueOf(question.getResponseDate()));
            int result = statement.executeUpdate();
            if (result != 1) {
                throw new DaoException("Error with creating question");
            }
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                question.setQuestionId(resultSet.getLong(1));
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with creating question", exception);
        } finally {
            close(resultSet);
            close(statement);
        }
        return question;
    }

    @Override
    public Question update(Question question) throws DaoException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(UPDATE_QUESTION);
            statement.setLong(6, question.getQuestionId());
            statement.setLong(1, question.getUserId());
            statement.setString(2, question.getRequest());
            statement.setString(3, question.getResponse());
            statement.setDate(4, Date.valueOf(question.getRequestDate()));
            statement.setDate(5, Date.valueOf(question.getResponseDate()));
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DaoException("Error with update question", exception);
        } finally {
            close(statement);
        }
        return question;
    }
}

