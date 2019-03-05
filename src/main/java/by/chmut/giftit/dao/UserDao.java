package by.chmut.giftit.dao;

import by.chmut.giftit.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserDao extends Dao<Long, User> {

    Optional<User> findByUsername(String username) throws DaoException;

    List<User> findByPartOfUsername(String username) throws DaoException;

    List<User> findByInitDate(LocalDate initDate) throws DaoException;
}
