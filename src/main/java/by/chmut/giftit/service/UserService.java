package by.chmut.giftit.service;

import by.chmut.giftit.entity.User;

import javax.servlet.http.Cookie;
import java.util.Map;

public interface UserService {

    User find(String username) throws ServiceException;
    User find(Cookie cookie) throws ServiceException;
    User create(Map<String, String> parameters) throws ServiceException;

    boolean validateUser(User user, String password);

//    Cookie addRememberMe(User user) throws ServiceException;
}
