package by.chmut.giftit.validator;

import by.chmut.giftit.entity.User;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordValidator {

    public static boolean validateUser(User user, String password) {
        return BCrypt.checkpw(password, user.getPassword());
    }
}
