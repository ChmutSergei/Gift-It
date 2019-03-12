package by.chmut.giftit.service.impl;

import by.chmut.giftit.dao.DaoException;
import by.chmut.giftit.dao.DaoFactory;
import by.chmut.giftit.dao.TransactionManager;
import by.chmut.giftit.dao.UserDao;
import by.chmut.giftit.entity.Comment;
import by.chmut.giftit.entity.User;
import by.chmut.giftit.service.ServiceException;
import by.chmut.giftit.service.UserService;
import by.chmut.giftit.validator.PasswordValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.http.Cookie;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

import static by.chmut.giftit.constant.AttributeName.*;

public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger();
    private static final String REGEX_USERNAME = "^[\\w_]{4,20}$";
    private static final String REGEX_NAME = "^[\\wА-Яа-я]{4,20}$";
    private static final String REGEX_PASSWORD = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}$";
    private static final String REGEX_EMAIL = "^([\\w\\._]+)@([\\w\\._]+)\\.([a-z]{2,6})$";
    private static final String REGEX_PHONE = "^\\+375 ?(44|29|25|17|33) ?(\\d ?){7}$";
    private static final String REGEX_ADDRESS = "^.{10,80}$";
    private static final int EMAIL_MAX_LENGTH = 30;

    private UserDao userDao = DaoFactory.getInstance().getUserDao();
    private TransactionManager manager = new TransactionManager();

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
            manager.endTransaction(userDao);
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
            manager.endTransaction(userDao);
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

    @Override
    public List<User> findUsersAfterUpdate(List<User> users) {
        List<User> actualUsers = new ArrayList<>();
        try {
            manager.beginTransaction(userDao);
            for (User user : users) {
                Optional<User> optionalUser = userDao.findEntity(user.getUserId());
                actualUsers.add(optionalUser.get());
            }
            manager.endTransaction(userDao);
        } catch (DaoException exception) {
            exception.printStackTrace();
        }
        return actualUsers;
    }

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
            manager.endTransaction(userDao);
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

    @Override
    public Optional<User> find(long userId) throws ServiceException {
        Optional<User> user;
        try {
            manager.beginTransaction(userDao);
            user = userDao.findEntity(userId);
            manager.endTransaction(userDao);
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

    @Override
    public Optional<User> find(String username) throws ServiceException {
        Optional<User> user;
        try {
            manager.beginTransaction(userDao);
            user = userDao.findByUsername(username);
            manager.endTransaction(userDao);
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
        if (!isValidParamsWithPatterns(userParameters)) {
            throw new ServiceException("Error when add user - incorrect data");
        }
        User newUser = setParameter(userParameters);
        try {
            manager.beginTransaction(userDao);
            newUser = userDao.create(newUser);
            manager.endTransaction(userDao);
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

    private User setParameter(Map<String, String> userParameters) {
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

    private boolean isValidParamsWithPatterns(Map<String, String> userParameters) {
        if (!Pattern.matches(REGEX_USERNAME, userParameters.get(USERNAME_PARAMETER_NAME))) {
            return false;
        }
        if (!Pattern.matches(REGEX_PASSWORD, userParameters.get(PASSWORD_PARAMETER_NAME))) {
            return false;
        }
        if (!Pattern.matches(REGEX_NAME, userParameters.get(FIRST_NAME_PARAMETER_NAME))) {
            return false;
        }
        if (!Pattern.matches(REGEX_NAME, userParameters.get(LAST_NAME_PARAMETER_NAME))) {
            return false;
        }
        if (!Pattern.matches(REGEX_EMAIL, userParameters.get(EMAIL_PARAMETER_NAME)) ||
                userParameters.get(EMAIL_PARAMETER_NAME).length() > EMAIL_MAX_LENGTH) {
            return false;
        }
        if (!Pattern.matches(REGEX_PHONE, userParameters.get(PHONE_PARAMETER_NAME))) {
            return false;
        }
        if (!Pattern.matches(REGEX_ADDRESS, userParameters.get(ADDRESS_PARAMETER_NAME))) {
            return false;
        }
        return true;
    }
}
