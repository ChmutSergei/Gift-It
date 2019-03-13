package by.chmut.giftit.validator;

import by.chmut.giftit.entity.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Map;
import java.util.regex.Pattern;

import static by.chmut.giftit.constant.AttributeName.*;

public class UserValidator {

    private static final String REGEX_USERNAME = "^[\\w_]{4,20}$";
    private static final String REGEX_NAME = "^[\\wА-Яа-я]{4,20}$";
    private static final String REGEX_PASSWORD = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}$";
    private static final String REGEX_EMAIL = "^([\\w\\._]+)@([\\w\\._]+)\\.([a-z]{2,6})$";
    private static final String REGEX_PHONE = "^\\+375 ?(44|29|25|17|33) ?(\\d ?){7}$";
    private static final String REGEX_ADDRESS = "^.{10,80}$";
    private static final int EMAIL_MAX_LENGTH = 30;

    public static boolean validatePassword(User user, String password) {
        return BCrypt.checkpw(password, user.getPassword());
    }

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
