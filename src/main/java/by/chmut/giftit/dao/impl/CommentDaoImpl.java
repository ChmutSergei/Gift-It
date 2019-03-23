package by.chmut.giftit.dao.impl;

import by.chmut.giftit.dao.CommentDao;
import by.chmut.giftit.dao.DaoException;
import by.chmut.giftit.entity.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The Comment dao class provides
 * manipulation of Comment entity with database.
 *
 * @author Sergei Chmut.
 */
public class CommentDaoImpl implements CommentDao {

    /**
     * The constant SQL query SELECT_ALL_COMMENTS.
     */
    private static final String SELECT_ALL_COMMENTS =
            "SELECT id, user_id, item_id, message, date, status " +
                    "FROM Comments";
    /**
     * The constant SQL query SELECT_COMMENTS_FOR_MODERATOR.
     */
    private static final String SELECT_COMMENTS_FOR_MODERATOR =
            "SELECT id, user_id, item_id, message, date, status " +
                    "FROM Comments " +
                    "WHERE status = 'NEW'";
    /**
     * The constant SQL query SELECT_COMMENT_BY_ID.
     */
    private static final String SELECT_COMMENT_BY_ID =
            "SELECT id, user_id, item_id, message, date, status " +
                    "FROM Comments " +
                    "WHERE id = ?";
    /**
     * The constant SQL query SELECT_COMMENTS_BY_ITEM_ID.
     */
    private static final String SELECT_COMMENTS_BY_ITEM_ID =
            "SELECT id, user_id, item_id, message, date, status " +
                    "FROM Comments " +
                    "WHERE item_id = ? AND status = ?";
    /**
     * The constant SQL query SELECT_COMMENTS_BY_USER_ID.
     */
    private static final String SELECT_COMMENTS_BY_USER_ID =
            "SELECT id, user_id, item_id, message, date, status " +
                    "FROM Comments " +
                    "WHERE user_id = ?";
    /**
     * The constant SQL query DELETE_COMMENT.
     */
    private static final String DELETE_COMMENT =
            "DELETE FROM Comments " +
                    "WHERE id=?";
    /**
     * The constant SQL query CREATE_COMMENT.
     */
    private static final String CREATE_COMMENT =
            "INSERT INTO Comments(user_id, item_id, message, date, status) " +
                    "VALUES(?,?,?,?,?)";
    /**
     * The constant SQL query UPDATE_COMMENT.
     */
    private static final String UPDATE_COMMENT =
            "UPDATE Comments SET user_id=?, item_id=?, message=?, date=?, status=? " +
                    "WHERE id=?";
    /**
     * The constant SQL query COUNT_COMMENT.
     */
    private static final String COUNT_COMMENT =
            "SELECT COUNT(id) " +
                    "FROM Comments " +
                    "WHERE item_id = ? AND status = ?";

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
     * Find all Comment from the database.
     *
     * @return the list of Comment
     * @throws DaoException if find all can't be handled
     */
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

    /**
     * Find comment by id.
     *
     * @param id comment id
     * @return the optional Entity
     * @throws DaoException if find comment can't be handled
     */
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

    /**
     * Find comments by item id with such status.
     *
     * @param itemId the item id
     * @param status the comment status
     * @return the list of comments
     * @throws DaoException if find comments can't be handled
     */
    @Override
    public List<Comment> findByItemId(long itemId, Comment.CommentStatus status) throws DaoException {
        List<Comment> comments = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_COMMENTS_BY_ITEM_ID);
            statement.setLong(1, itemId);
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

    /**
     * Find comments by user id list.
     *
     * @param userId the user id
     * @return the list of comments
     * @throws DaoException if find comments can't be handled
     */
    @Override
    public List<Comment> findByUserId(long userId) throws DaoException {
        List<Comment> comments = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_COMMENTS_BY_USER_ID);
            statement.setLong(1, userId);
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

    /**
     * Find list of new comments to moderate.
     *
     * @return the list of comments
     * @throws DaoException if find comments can't be handled
     */
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

    /**
     * Method make comment from result set.
     *
     * @param resultSet the result set
     * @return the comment
     * @throws SQLException if comment from resultSet can't be handled
     */
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

    /**
     * Delete comment by id.
     *
     * @param id the id
     * @return true if the cart was deleted otherwise false
     * @throws DaoException if delete by id can't be handled
     */
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

    /**
     * Delete comment.
     *
     * @param comment the comment
     * @return true if the comment was deleted otherwise false
     * @throws DaoException if delete can't be handled
     */
    @Override
    public boolean delete(Comment comment) throws DaoException {
        return delete(comment.getCommentId());
    }

    /**
     * Add entity to database.
     *
     * @param comment the comment
     * @return Comment that was created
     * @throws DaoException if create comment can't be handled
     */
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

    /**
     * Update comment in database..
     *
     * @param comment the comment
     * @return Comment that was updated
     * @throws DaoException if update comment can't be handled
     */
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

    /**
     * Count all comment for Item with such item id.
     *
     * @param itemId the item id
     * @return the count off all comment for Item with such item id
     * @throws DaoException if count comment can't be handled
     */
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
