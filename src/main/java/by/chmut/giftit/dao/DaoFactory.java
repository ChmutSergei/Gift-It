package by.chmut.giftit.dao;

import by.chmut.giftit.dao.impl.UserDaoImpl;

public class DaoFactory {

    private static final DaoFactory INSTANCE = new DaoFactory();
    private static final UserDao userDao = new UserDaoImpl();

    private DaoFactory() {
    }

    public static DaoFactory getInstance() {
        return INSTANCE;
    }

    public UserDao getUserDao() {
        return userDao;
    }

}
