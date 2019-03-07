package by.chmut.giftit.service;

import by.chmut.giftit.entity.User;

public interface AjaxService {

    boolean updateUserData(User user);
    boolean deleteComment(long commentId);
    boolean addComment(long itemId, long userId, String commentMessage);
    boolean changeItemStatus(long itemId, String filePath);
    boolean acceptQuestion(long userId, String message);
    boolean checkUsernameOnExist(String username);
}
