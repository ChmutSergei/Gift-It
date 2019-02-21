package by.chmut.giftit.Validator;

import by.chmut.giftit.entity.User;
import org.mindrot.jbcrypt.BCrypt;

public class Validator {

    public static boolean validateUser(User user, String password) {
        return BCrypt.checkpw(password, user.getPassword());
    }
}
