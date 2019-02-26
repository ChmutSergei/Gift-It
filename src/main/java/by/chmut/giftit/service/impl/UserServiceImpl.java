package by.chmut.giftit.service.impl;

import by.chmut.giftit.dao.DaoException;
import by.chmut.giftit.dao.DaoFactory;
import by.chmut.giftit.dao.TransactionManager;
import by.chmut.giftit.dao.UserDao;
import by.chmut.giftit.entity.User;
import by.chmut.giftit.service.ServiceException;
import by.chmut.giftit.service.UserService;
import by.chmut.giftit.validator.PasswordValidator;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.http.Cookie;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import static by.chmut.giftit.constant.AttributeName.*;

public class UserServiceImpl implements UserService {

    private static final String REGEX_USERNAME = "^[\\w_]{4,20}$";
    private static final String REGEX_NAME = "^[\\wА-Яа-я]{4,20}$";
    private static final String REGEX_PASSWORD = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}$";
    private static final String REGEX_EMAIL = "^([\\w\\._]+)@([\\w\\._]+)\\.([a-z]{2,6})$";
    private static final String REGEX_PHONE = "^\\+375 ?(44|29|25|17|33) ?(\\d ?){7}$";
    private static final String REGEX_ADDRESS = "^.{10,80}$";
    private static final int EMAIL_MAX_LENGTH = 30;

    private DaoFactory factory = DaoFactory.getInstance();
    private UserDao userDao = factory.getUserDao();
    private TransactionManager manager = new TransactionManager();


    @Override
    public boolean validateUser(User user, String password) {
        return PasswordValidator.validateUser(user, password);
    }

    //TODO
    @Override
    public User find(String username) throws ServiceException {
        User user;
        try {
            manager.beginTransaction(userDao);
            user = userDao.findEntityByUsername(username);
            manager.endTransaction(userDao);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return user;
    }

    @Override
    public User find(Cookie cookie) throws ServiceException {
        return null;
    }

    @Override
    public User create(Map<String, String> userParameters) throws ServiceException {
        for (Entry entry : userParameters.entrySet()) {
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
            newUser = userDao.createUser(newUser);
            manager.endTransaction(userDao);
        } catch (DaoException exception) {
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

    //TODO
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
