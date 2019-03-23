package by.chmut.giftit.dao.impl;

import by.chmut.giftit.dao.DaoException;
import by.chmut.giftit.dao.QuestionDao;
import by.chmut.giftit.entity.Question;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The Question dao class provides
 * manipulation of Question entity with database.
 *
 * @author Sergei Chmut.
 */
public class QuestionDaoImpl implements QuestionDao {

    /**
     * The constant SQL query SELECT_ALL_QUESTIONS.
     */
    private static final String SELECT_ALL_QUESTIONS =
            "SELECT id, user_id, request, response, request_date, response_date " +
                    "FROM Questions";
    /**
     * The constant SQL query SELECT_UNANSWERED_QUESTIONS.
     */
    private static final String SELECT_UNANSWERED_QUESTIONS =
            "SELECT id, user_id, request, response, request_date, response_date " +
                    "FROM Questions " +
                    "WHERE response IS NULL";
    /**
     * The constant SQL query SELECT_QUESTION_BY_ID.
     */
    private static final String SELECT_QUESTION_BY_ID =
            "SELECT id, user_id, request, response, request_date, response_date " +
                    "FROM Questions " +
                    "WHERE id = ?";
    /**
     * The constant SQL query SELECT_QUESTION_BY_USER_ID.
     */
    private static final String SELECT_QUESTION_BY_USER_ID =
            "SELECT id, user_id, request, response, request_date, response_date " +
                    "FROM Questions " +
                    "WHERE user_id = ?";
    /**
     * The constant SQL query DELETE_QUESTION.
     */
    private static final String DELETE_QUESTION =
            "DELETE FROM Questions " +
                    "WHERE id=?";
    /**
     * The constant SQL query CREATE_QUESTION.
     */
    private static final String CREATE_QUESTION =
            "INSERT INTO Questions(user_id, request, response, request_date, response_date) " +
                    "VALUES(?,?,?,?,?)";
    /**
     * The constant SQL query UPDATE_QUESTION.
     */
    private static final String UPDATE_QUESTION =
            "UPDATE Questions SET user_id=?, request=?, response=?, request_date=?, response_date=? " +
                    "WHERE id=?";

    /**
     * The Connection instance for working with database.
     */
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Find all question from the database.
     *
     * @return the list of question
     * @throws DaoException if find all can't be handled
     */
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

    /**
     * Find question by id.
     *
     * @param id question id
     * @return the optional question
     * @throws DaoException if find question can't be handled
     */
    @Override
    public Optional<Question> findEntity(Long id) throws DaoException {
        Question question = null;
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
        return Optional.ofNullable(question);
    }

    /**
     * Method make question from result set.
     *
     * @param resultSet the result set
     * @return the question
     * @throws SQLException if question from resultSet can't be handled
     */
    private Question makeFromResultSet(ResultSet resultSet) throws SQLException {
        Question question = new Question();
        question.setQuestionId(resultSet.getLong(1));
        question.setUserId(resultSet.getLong(2));
        question.setRequest(resultSet.getString(3));
        question.setResponse(resultSet.getString(4));
        question.setRequestDate(resultSet.getDate(5).toLocalDate());
        Date responseDate = resultSet.getDate(6);
        if (responseDate != null) {
            question.setResponseDate(responseDate.toLocalDate());
        }
        return question;
    }

    /**
     * Delete question by id.
     *
     * @param id question id
     * @return true if delete done otherwise false
     * @throws DaoException if delete can't be handled
     */
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

    /**
     * Delete question.
     *
     * @param question the question
     * @return true if delete done otherwise false
     * @throws DaoException if delete can't be handled
     */
    @Override
    public boolean delete(Question question) throws DaoException {
        return delete(question.getQuestionId());
    }

    /**
     * Add question to database.
     *
     * @param question the question
     * @return question that was created
     * @throws DaoException if create question can't be handled
     */
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
            LocalDate responseLocalDate = question.getResponseDate();
            Date responseDate = responseLocalDate != null ? Date.valueOf(responseLocalDate) : null;
            statement.setDate(5, responseDate);
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

    /**
     * Update question in database.
     *
     * @param question the question
     * @return question that was updated
     * @throws DaoException if update question can't be handled
     */
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
            int result = statement.executeUpdate();
            if (result != 1) {
                throw new DaoException("Error with update question");
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with update question", exception);
        } finally {
            close(statement);
        }
        return question;
    }

    /**
     * Find all questions with such user id.
     *
     * @param userId the user id
     * @return the list of question
     * @throws DaoException if find question by user id can't be handled
     */
    @Override
    public List<Question> findByUserId(long userId) throws DaoException {
        List<Question> questions = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_QUESTION_BY_USER_ID);
            statement.setLong(1, userId);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Question question = makeFromResultSet(resultSet);
                questions.add(question);
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with get questions on userId", exception);
        } finally {
            close(statement);
            close(resultSet);
        }
        return questions;
    }

    /**
     * Find all unanswered questions.
     *
     * @return the list of question
     * @throws DaoException if find unanswered question can't be handled
     */
    @Override
    public List<Question> findUnanswered() throws DaoException {
        List<Question> questions = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_UNANSWERED_QUESTIONS);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Question question = makeFromResultSet(resultSet);
                questions.add(question);
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with get unanswered questions", exception);
        } finally {
            close(statement);
            close(resultSet);
        }
        return questions;
    }
}

