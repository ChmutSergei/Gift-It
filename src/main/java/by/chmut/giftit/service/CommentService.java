package by.chmut.giftit.service;

import by.chmut.giftit.entity.Comment;
import by.chmut.giftit.entity.User;

import java.util.List;
import java.util.Map;

public interface CommentService {

    List<Comment> find(long userId) throws ServiceException;

    List<Comment> findCommentToModerate() throws ServiceException;

    boolean moderate(String moderatorCommand, String commentId, Map<Long,User> users) throws ServiceException;
}
