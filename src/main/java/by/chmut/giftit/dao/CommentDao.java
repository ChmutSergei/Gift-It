package by.chmut.giftit.dao;

import by.chmut.giftit.entity.Comment;

public interface CommentDao extends Dao<Long, Comment> {

    int countCommentForItem(long itemId) throws DaoException;
}
