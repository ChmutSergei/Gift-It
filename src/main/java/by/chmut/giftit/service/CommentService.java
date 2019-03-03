package by.chmut.giftit.service;

import by.chmut.giftit.entity.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> find(long userId) throws ServiceException;
}
