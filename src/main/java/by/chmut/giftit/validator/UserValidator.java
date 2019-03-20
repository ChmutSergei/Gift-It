package by.chmut.giftit.validator;

import by.chmut.giftit.entity.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Map;
import java.util.regex.Pattern;

import static by.chmut.giftit.constant.AttributeName.*;

/**
 * The User validator class provides validation of user data.
 *
 * @author Sergei Chmut.
 */
public class UserValidator {

    /**
     * The constant REGEX_USERNAME.
     */
    private static final String REGEX_USERNAME = "^[\\w_]{4,20}$";
    /**
     * The constant REGEX_NAME.
     */
    private static final String REGEX_NAME = "^[\\wА-Яа-я]{4,20}$";
    /**
     * The constant REGEX_PASSWORD.
     */
    private static final String REGEX_PASSWORD = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}$";
    /**
     * The constant REGEX_EMAIL.
     */
    private static final String REGEX_EMAIL = "^([\\w\\._]+)@([\\w\\._]+)\\.([a-z]{2,6})$";
    /**
     * The constant REGEX_PHONE.
     */
    private static final String REGEX_PHONE = "^\\+375 ?(44|29|25|17|33) ?(\\d ?){7}$";
    /**
     * The constant REGEX_ADDRESS.
     */
    private static final String REGEX_ADDRESS = "^.{10,80}$";
    /**
     * The constant EMAIL_MAX_LENGTH.
     */
    private static final int EMAIL_MAX_LENGTH = 30;

    /**
     * Method to verify the entered password with
     * the reference in the database
     *
     * @param user     the user for validation
     * @param password the user's password that was entered
     * @return {@code true} if the given password
     * equivalent to the password from database, {@code false} otherwise
     */
    public static boolean validatePassword(User user, String password) {
        return BCrypt.checkpw(password, user.getPassword());
    }

    /**
     * Method verifies to valid user's data to the patterns.
     *
     * @param userParameters the map of the user's data
     * @return {@code true} if the given data
     * valid to the patterns, {@code false} otherwise
     */
    public static boolean isValidParamsWithPatterns(Map<String, String> userParameters) {
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
