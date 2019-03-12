package by.chmut.giftit.dao;

import by.chmut.giftit.entity.Comment;

import java.util.List;

public interface CommentDao extends Dao<Long, Comment> {

    int countCommentForItem(long itemId) throws DaoException;

    List<Comment> findByItemId(long itemId, Comment.CommentStatus status) throws DaoException;

    List<Comment> findByUserId(long userId) throws DaoException;

    List<Comment> findToModerate() throws DaoException;
}
