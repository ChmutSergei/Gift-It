package by.chmut.giftit.service;

import by.chmut.giftit.entity.User;

/**
 * The interface AjaxService provides service
 * methods for Ajax commands.
 *
 * @author Sergei Chmut.
 */
public interface AjaxService {

    /**
     * Update user data.
     *
     * @param user the user
     * @return true if success otherwise false
     */
    boolean updateUserData(User user);

    /**
     * Delete comment by id.
     *
     * @param commentId the comment id
     * @return true if success otherwise false
     */
    boolean deleteComment(long commentId);

    /**
     * Add new comment.
     *
     * @param itemId         the item id
     * @param userId         the user id
     * @param commentMessage the comment message
     * @return true if success otherwise false
     */
    boolean addComment(long itemId, long userId, String commentMessage);

    /**
     * Change item status.
     *
     * @param itemId   the item id
     * @param filePath the file path
     * @return true if success otherwise false
     */
    boolean changeItemStatus(long itemId, String filePath);

    /**
     * Accept user's question.
     *
     * @param userId  the user id
     * @param message the message
     * @return true if success otherwise false
     */
    boolean acceptQuestion(long userId, String message);

    /**
     * Check username on exist.
     *
     * @param username the username
     * @return true if success otherwise false
     */
    boolean checkUsernameOnExist(String username);

    /**
     * Accept comment by id.
     *
     * @param commentId the comment id
     * @return true if success otherwise false
     */
    boolean acceptComment(long commentId);
}
