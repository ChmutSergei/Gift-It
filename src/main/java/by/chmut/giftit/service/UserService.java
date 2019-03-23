package by.chmut.giftit.service;

import by.chmut.giftit.entity.Comment;
import by.chmut.giftit.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The interface UserService provides service
 * methods associated with User entity.
 *
 * @author Sergei Chmut.
 */
public interface UserService {

    /**
     * Find user by username.
     *
     * @param username the username
     * @return the optional user
     * @throws ServiceException if find user by username can't be handled
     */
    Optional<User> find(String username) throws ServiceException;

    /**
     * Find user by id.
     *
     * @param userId the user id
     * @return the optional user
     * @throws ServiceException if find user by user id can't be handled
     */
    Optional<User> find(long userId) throws ServiceException;

    /**
     * Create new user and save.
     *
     * @param parameters the user's parameters
     * @return the new user
     * @throws ServiceException if the user can't be created
     */
    User create(Map<String, String> parameters) throws ServiceException;

    /**
     * Search users by params list.
     *
     * @param parametersSearch the parameters for search
     * @return the list of users
     * @throws ServiceException if an exception occurs while searching for a user
     */
    List<User> searchUserByParams(Map<String, String> parametersSearch) throws ServiceException;

    /**
     * Processing user with different admin commands.
     *
     * @param typeCommand  the type command fo processing user
     * @param userId       the user id
     * @param blockedUntil user lock up date
     * @param newRole      the new role
     * @return true if command done otherwise false
     * @throws ServiceException if an exception occurs while processing user
     */
    boolean executeUserProcessingCommand(String typeCommand, long userId, LocalDate blockedUntil, String newRole) throws ServiceException;

    /**
     * Find users after update.
     *
     * @param users the list of users who are updated
     * @return the list of users
     * @throws ServiceException if find user after update can't be handled
     */
    List<User> findUsersAfterUpdate(List<User> users) throws ServiceException;

    /**
     * Find all users who left those comments.
     *
     * @param comments the comments
     * @return the map of users who left this comment
     * @throws ServiceException if find users by comments can't be handled
     */
    Map<Long, User> findByComment(List<Comment> comments) throws ServiceException;

}
