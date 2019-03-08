package by.chmut.giftit.dao.impl;

import by.chmut.giftit.dao.CommentDao;
import by.chmut.giftit.dao.DaoException;
import by.chmut.giftit.entity.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommentDaoImpl implements CommentDao { //TODO исправить выборку с учетом статуса

    private static final String SELECT_ALL_COMMENTS = "SELECT id, user_id, item_id, message, date, status FROM Comments";
    private static final String SELECT_COMMENTS_FOR_MODERATOR = "SELECT id, user_id, item_id, message, date, status " +
            "FROM Comments WHERE status = 'NEW'";
    private static final String SELECT_COMMENT_BY_ID = "SELECT id, user_id, item_id, message, date, status FROM Comments WHERE id = ?";
    private static final String SELECT_COMMENTS_BY_ITEM_ID = "SELECT id, user_id, item_id, message, date, status " +
            "FROM Comments WHERE item_id = ? AND status = ?";
    private static final String SELECT_COMMENTS_BY_USER_ID = "SELECT id, user_id, item_id, message, date, status " +
            "FROM Comments WHERE user_id = ?";
    private static final String DELETE_COMMENT = "DELETE FROM Comments WHERE id=?";
    private static final String CREATE_COMMENT = "INSERT INTO Comments(user_id, item_id, message, date, status) VALUES(?,?,?,?,?)";
    private static final String UPDATE_COMMENT = "UPDATE Comments SET user_id=?, item_id=?, message=?, date=?, status=? WHERE id=?";
    private static final String COUNT_COMMENT = "SELECT COUNT(id) FROM Comments WHERE item_id = ? AND status = ?";

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
    public Optional<Comment> findEntity(Long id) throws DaoException {
        Comment comment = null;
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
        return Optional.ofNullable(comment);
    }

    @Override
    public List<Comment> findByItemId(long id, Comment.CommentStatus status) throws DaoException {
        List<Comment> comments = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_COMMENTS_BY_ITEM_ID);
            statement.setLong(1, id);
            statement.setString(2, status.name());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Comment comment = makeFromResultSet(resultSet);
                comments.add(comment);
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with get all comments by item id", exception);
        } finally {
            close(statement);
            close(resultSet);
        }
        return comments;
    }

    @Override
    public List<Comment> findByUserId(long id) throws DaoException {
        List<Comment> comments = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_COMMENTS_BY_USER_ID);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Comment comment = makeFromResultSet(resultSet);
                comments.add(comment);
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with get all comments by user id", exception);
        } finally {
            close(statement);
            close(resultSet);
        }
        return comments;
    }

    @Override
    public List<Comment> findToModerate() throws DaoException {
        List<Comment> comments = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_COMMENTS_FOR_MODERATOR);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Comment comment = makeFromResultSet(resultSet);
                comments.add(comment);
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with find comments for moderate", exception);
        } finally {
            close(statement);
            close(resultSet);
        }
        return comments;
    }

    private Comment makeFromResultSet(ResultSet resultSet) throws SQLException {
        Comment comment = new Comment();
        comment.setCommentId(resultSet.getLong(1));
        comment.setUserId(resultSet.getLong(2));
        comment.setItemId(resultSet.getLong(3));
        comment.setMessage(resultSet.getString(4));
        comment.setDate(resultSet.getDate(5).toLocalDate());
        comment.setCommentStatus(Comment.CommentStatus.valueOf(resultSet.getString(6)));
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
    public Comment create(Comment comment) throws DaoException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(CREATE_COMMENT, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, comment.getUserId());
            statement.setLong(2, comment.getItemId());
            statement.setString(3, comment.getMessage());
            statement.setDate(4, Date.valueOf(comment.getDate()));
            statement.setString(5, comment.getCommentStatus().name());
            int result = statement.executeUpdate();
            if (result != 1) {
                throw new DaoException("Error with creating comment");
            }
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
        return comment;
    }

    @Override
    public Comment update(Comment comment) throws DaoException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(UPDATE_COMMENT);
            statement.setLong(6, comment.getCommentId());
            statement.setLong(1, comment.getUserId());
            statement.setLong(2, comment.getItemId());
            statement.setString(3, comment.getMessage());
            statement.setDate(4, Date.valueOf(comment.getDate()));
            statement.setString(5, comment.getCommentStatus().name());
            int result = statement.executeUpdate();
            if (result != 1) {
                throw new DaoException("Error with update comment");
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with update comment", exception);
        } finally {
            close(statement);
        }
        return comment;
    }

    @Override
    public int countCommentForItem(long itemId) throws DaoException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int result = 0;
        try {
            statement = connection.prepareStatement(COUNT_COMMENT);
            statement.setLong(1, itemId);
            statement.setString(2, Comment.CommentStatus.ACTIVE.name());
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            throw new DaoException("Error when try count comment for Item", exception);
        } finally {
            close(resultSet);
            close(statement);
        }
        return result;
    }
}
