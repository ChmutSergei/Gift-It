package by.chmut.giftit.dao.impl;

import by.chmut.giftit.dao.CommentDao;
import by.chmut.giftit.dao.DaoException;
import by.chmut.giftit.entity.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDaoImpl implements CommentDao {

    private static final String SELECT_ALL_COMMENTS = "SELECT id, user_id, message, date FROM Comments";
    private static final String SELECT_COMMENT_BY_ID = "SELECT id, user_id, message, date FROM Comments WHERE id = ?";
    private static final String DELETE_COMMENT = "DELETE FROM Comments WHERE id=?";
    private static final String CREATE_COMMENT = "INSERT INTO Comments(user_id, message, date) VALUES(?,?,?)";
    private static final String UPDATE_COMMENT = "UPDATE Comments SET user_id=?, message=?, date=? WHERE id=?";

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Comment> findAll() throws DaoException {
        List<Comment> comments = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_ALL_COMMENTS);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Comment comment = makeFromResultSet(resultSet);
                comments.add(comment);
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with get all comments", exception);
        } finally {
            close(statement);
            close(resultSet);
        }
        return comments;
    }

    @Override
    public Comment findEntity(Long id) throws DaoException {
        Comment comment = new Comment();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_COMMENT_BY_ID);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                comment = makeFromResultSet(resultSet);
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with get comment by id", exception);
        } finally {
            close(statement);
            close(resultSet);
        }
        return comment;
    }

    private Comment makeFromResultSet(ResultSet resultSet) throws SQLException {
        Comment comment = new Comment();
        comment.setCommentId(resultSet.getLong(1));
        comment.setUserId(resultSet.getLong(2));
        comment.setMessage(resultSet.getString(3));
        comment.setDate(resultSet.getDate(4).toLocalDate());
        return comment;
    }

    @Override
    public boolean delete(Long id) throws DaoException {
        PreparedStatement statement = null;
        int result;
        try {
            statement = connection.prepareStatement(DELETE_COMMENT);
            statement.setLong(1, id);
            result = statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DaoException("Error with delete comment", exception);
        } finally {
            close(statement);
        }
        return result > 0;
    }

    @Override
    public boolean delete(Comment comment) throws DaoException {
        return delete(comment.getCommentId());
    }

    @Override
    public boolean create(Comment comment) throws DaoException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int result;
        try {
            statement = connection.prepareStatement(CREATE_COMMENT, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, comment.getUserId());
            statement.setString(2, comment.getMessage());
            statement.setDate(3, Date.valueOf(comment.getDate()));
            result = statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                comment.setCommentId(resultSet.getLong(1));
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with creating comment", exception);
        } finally {
            close(resultSet);
            close(statement);
        }
        return result > 0;
    }

    @Override
    public Comment update(Comment comment) throws DaoException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(UPDATE_COMMENT);
            statement.setLong(4, comment.getCommentId());
            statement.setLong(1, comment.getUserId());
            statement.setString(2, comment.getMessage());
            statement.setDate(3, Date.valueOf(comment.getDate()));
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DaoException("Error with update comment", exception);
        } finally {
            close(statement);
        }
        return comment;
    }

}
