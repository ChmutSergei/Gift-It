package by.chmut.giftit.dao;

import by.chmut.giftit.entity.Comment;

import java.util.List;

public interface CommentDao extends Dao<Long, Comment> {

    int countCommentForItem(long itemId) throws DaoException;
    List<Comment> findByItemId(long id) throws DaoException;
    List<Comment> findByUserId(long id) throws DaoException;
}
