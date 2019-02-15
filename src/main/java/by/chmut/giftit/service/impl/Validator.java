package by.chmut.giftit.service.impl;

import by.chmut.giftit.entity.User;
import org.mindrot.jbcrypt.BCrypt;

class Validator {

    public static boolean validateUser(User user, String password) {
        return BCrypt.checkpw(password, user.getPassword());
    }
}
