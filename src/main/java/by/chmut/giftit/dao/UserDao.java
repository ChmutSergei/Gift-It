package by.chmut.giftit.dao;

import by.chmut.giftit.entity.User;

import java.util.Optional;

public interface UserDao extends Dao<Long, User> {

    Optional<User> findByUsername(String username) throws DaoException;
}
