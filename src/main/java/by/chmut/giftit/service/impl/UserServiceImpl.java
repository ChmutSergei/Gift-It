package by.chmut.giftit.service.impl;

import by.chmut.giftit.dao.DaoException;
import by.chmut.giftit.dao.DaoFactory;
import by.chmut.giftit.dao.TransactionManager;
import by.chmut.giftit.dao.UserDao;
import by.chmut.giftit.entity.Comment;
import by.chmut.giftit.entity.User;
import by.chmut.giftit.service.ServiceException;
import by.chmut.giftit.service.UserService;
import by.chmut.giftit.validator.UserValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static by.chmut.giftit.constant.AttributeName.*;

/**
 * The User service class implements business logic methods
 * for user entity.
 *
 * @author Sergei Chmut.
 */
public class UserServiceImpl implements UserService {

    /**
     * The logger for logging possible errors.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * The User dao provides access to the database for User entity.
     */
    private UserDao userDao = DaoFactory.getInstance().getUserDao();
    /**
     * The transaction manager provides preparation for conducting and confirming transactions.
     */
    private TransactionManager manager = new TransactionManager();

    /**
     * Search users by parameters. The method takes the parameters as a map,
     * receives a parameter for the search (checks for null) and sends it
     * to the method searchUsers to perform the corresponding database query.
     *
     * @param parametersSearch the parameters for search
     * @return the list of users
     * @throws ServiceException if an exception occurs while searching for a user
     */
    @Override
    public List<User> searchUserByParams(Map<String, String> parametersSearch) throws ServiceException {
        List<User> users;
        String searchType = parametersSearch.get(USER_SEARCH_TYPE_PARAMETER_NAME);
        if (searchType == null) {
            return Collections.emptyList();
        }
        try {
            manager.beginTransaction(userDao);
            users = searchUsers(searchType, parametersSearch);
            manager.endTransaction();
        } catch (DaoException exception) {
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            throw new ServiceException(exception);
        }
        return users;
    }

    /**
     * Processing user with different admin commands.
     * The method determines the transmitted command admin.
     * If you want to delete a user, a command is executed,
     * otherwise a user is searched from the database,
     * if found, a new state is set and updated in the database.
     *
     * @param typeCommand  the type command fo processing user
     * @param userId       the user id
     * @param blockedUntil user lock up date
     * @param newRole      the new role
     * @return true if command done otherwise false
     * @throws ServiceException if an exception occurs while processing user
     */
    @Override
    public boolean executeUserProcessingCommand(String typeCommand, long userId,
                                                LocalDate blockedUntil, String newRole) throws ServiceException {
        boolean result = false;
        try {
            manager.beginTransaction(userDao);
            if (typeCommand.equals(DELETE_USER_COMMAND_PARAMETER_NAME)) {
                result = userDao.delete(userId);
            } else {
                Optional<User> optionalUser = userDao.findEntity(userId);
                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();
                    switch (typeCommand) {
                        case BLOCKED_COMMAND_PARAMETER_NAME:
                            user.setBlockedUntil(blockedUntil);
                            break;
                        case UNLOCK_COMMAND_PARAMETER_NAME:
                            user.setBlockedUntil(LocalDate.now());
                            break;
                        case SET_ROLE_COMMAND_PARAMETER_NAME:
                            user.setRole(User.Role.valueOf(newRole));
                            break;
                        default:
                            throw new ServiceException("Impossible state - unsupported command user processing command");
                    }
                    userDao.update(user);
                    result = true;
                }
            }
            manager.endTransaction();
        } catch (DaoException exception) {
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            throw new ServiceException(exception);
        }
        return result;
    }

    /**
     * Find users after update.
     * The method receives updated data for each of the users transferred
     * in the form of a list and returns them to the new list.
     *
     * @param users the list of users who are updated
     * @return the list of users
     * @throws ServiceException if find user after update can't be handled
     */
    @Override
    public List<User> findUsersAfterUpdate(List<User> users) throws ServiceException {
        List<User> actualUsers = new ArrayList<>();
        try {
            manager.beginTransaction(userDao);
            for (User user : users) {
                Optional<User> optionalUser = userDao.findEntity(user.getUserId());
                actualUsers.add(optionalUser.get());
            }
            manager.endTransaction();
        } catch (DaoException exception) {
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            throw new ServiceException(exception);
        }
        return actualUsers;
    }

    /**
     * Find all users who left those comments.
     * The method, iterated over the list of transmitted comments,
     * receives for each comment of its author and returns
     * a map where each comment corresponds to a user.
     *
     * @param comments the comments
     * @return the map of users who left this comment
     * @throws ServiceException if find users by comments can't be handled
     */
    @Override
    public Map<Long, User> findByComment(List<Comment> comments) throws ServiceException {
        Map<Long, User> users = new HashMap<>();
        try {
            manager.beginTransaction(userDao);
            for (Comment comment : comments) {
                long userId = comment.getUserId();
                Optional<User> optionalUser = userDao.findEntity(userId);
                optionalUser.ifPresent(user -> users.put(comment.getCommentId(), user));
            }
            manager.endTransaction();
        } catch (DaoException exception) {
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            throw new ServiceException(exception);
        }
        return users;
    }

    /**
     * The method determines on what basis to
     * search for the user and gets the value from
     * the passed parameters. Calls the desired method
     * on User Dao and returns a list of search results.
     *
     * @param searchType       the search type
     * @param parametersSearch the map with parameters for search
     * @return the list of the users
     * @throws DaoException     if search users can't be handled
     * @throws ServiceException throw in an impossible state
     */
    private List<User> searchUsers(String searchType, Map<String, String> parametersSearch) throws DaoException, ServiceException {
        List<User> result;
        switch (searchType) {
            case USER_ID_PARAMETER_NAME:
                result = new ArrayList<>();
                String parameterId = parametersSearch.get(USER_ID_PARAMETER_NAME);
                long userId = (parameterId != null) ? Long.parseLong(parameterId) : 0;
                Optional<User> optionalUser = userDao.findEntity(userId);
                optionalUser.ifPresent(result::add);
                break;
            case USERNAME_PARAMETER_NAME:
                result = userDao.findByPartOfUsername(parametersSearch.get(USERNAME_PARAMETER_NAME));
                break;
            case INIT_DATE_PARAMETER_NAME:
                String date = parametersSearch.get(INIT_DATE_PARAMETER_NAME);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
                LocalDate initDate = date != null ? LocalDate.parse(date, formatter) : LocalDate.now();
                result = userDao.findByInitDate(initDate);
                break;
            default:
                throw new ServiceException("Impossible state - unsupported command when search User");
        }
        return result;
    }

    /**
     * Find user by id.
     *
     * @param userId the user id
     * @return the optional user
     * @throws ServiceException if find user by user id can't be handled
     */
    @Override
    public Optional<User> find(long userId) throws ServiceException {
        Optional<User> user;
        try {
            manager.beginTransaction(userDao);
            user = userDao.findEntity(userId);
            manager.endTransaction();
        } catch (DaoException exception) {
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            throw new ServiceException(exception);
        }
        return user;
    }

    /**
     * Find user by username.
     *
     * @param username the username
     * @return the optional user
     * @throws ServiceException if find user by username can't be handled
     */
    @Override
    public Optional<User> find(String username) throws ServiceException {
        Optional<User> user;
        try {
            manager.beginTransaction(userDao);
            user = userDao.findByUsername(username);
            manager.endTransaction();
        } catch (DaoException exception) {
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            throw new ServiceException(exception);
        }
        return user;
    }

    /**
     * Create new user and save.
     * The method creates a new user in the system.
     * The so-called double check of the transferred
     * data from the View level to correctness is preliminarily performed,
     * if all checks are passed, a new user is created and saved to the database
     *
     * @param userParameters the user's parameters
     * @return the new user
     * @throws ServiceException if the user can't be created
     */
    @Override
    public User create(Map<String, String> userParameters) throws ServiceException {
        for (Map.Entry entry : userParameters.entrySet()) {
            if (entry.getValue() == null) {
                throw new ServiceException("Error when add user - incorrect data");
            }
        }
        if (!userParameters.get(PASSWORD_PARAMETER_NAME).equals(userParameters.get(CONFIRM_PASSWORD_PARAMETER_NAME))) {
            throw new ServiceException("Error password and confirmation password do not match.");
        }
        if (!UserValidator.isValidParamsWithPatterns(userParameters)) {
            throw new ServiceException("Error when add user - incorrect data");
        }
        User newUser = createWithParameters(userParameters);
        try {
            manager.beginTransaction(userDao);
            newUser = userDao.create(newUser);
            manager.endTransaction();
        } catch (DaoException exception) {
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            throw new ServiceException(exception);
        }
        return newUser;
    }

    /**
     * Create new User object.
     *
     * @param userParameters the user parameters
     * @return the new User
     */
    private User createWithParameters(Map<String, String> userParameters) {
        User user = new User();
        String passHashed = BCrypt.hashpw(userParameters.get(PASSWORD_PARAMETER_NAME), BCrypt.gensalt());
        user.setUsername(userParameters.get(USERNAME_PARAMETER_NAME));
        user.setPassword(passHashed);
        user.setFirstName(userParameters.get(FIRST_NAME_PARAMETER_NAME));
        user.setLastName(userParameters.get(LAST_NAME_PARAMETER_NAME));
        user.setEmail(userParameters.get(EMAIL_PARAMETER_NAME));
        user.setPhone(userParameters.get(PHONE_PARAMETER_NAME));
        user.setAddress(userParameters.get(ADDRESS_PARAMETER_NAME));
        user.setAccount(BigDecimal.ZERO);
        user.setInitDate(LocalDate.now());
        user.setBlockedUntil(LocalDate.now());
        user.setRole(User.Role.USER);
        return user;
    }
}
