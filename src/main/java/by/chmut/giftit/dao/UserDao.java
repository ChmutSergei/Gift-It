package by.chmut.giftit.dao;

import by.chmut.giftit.entity.User;

public interface UserDao extends Dao<Long, User> {

    User findEntityByUsername(String username) throws DaoException;
}
