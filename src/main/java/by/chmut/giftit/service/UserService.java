package by.chmut.giftit.service;

import by.chmut.giftit.entity.User;

import javax.servlet.http.Cookie;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {

    Optional<User> find(String username) throws ServiceException;
    Optional<User> find(long userId) throws ServiceException;
    Optional<User> find(Cookie cookie) throws ServiceException;
    User create(Map<String, String> parameters) throws ServiceException;
    boolean validateUser(User user, String password);

    List<User> searchUserByParams(Map<String, String> parametersSearch) throws ServiceException;

    boolean executeUserProcessingCommand(String typeCommand, long userId, LocalDate blockedUntil, String newRole) throws ServiceException;

    List<User> findUsersAfterUpdate(List<User> users);

//    Cookie addRememberMe(User user) throws ServiceException;
}
