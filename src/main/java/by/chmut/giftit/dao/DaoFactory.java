package by.chmut.giftit.dao;

import by.chmut.giftit.dao.impl.*;

public class DaoFactory {

    private static final DaoFactory INSTANCE = new DaoFactory();
    private static final UserDao userDao = new UserDaoImpl();
    private static final CartDao cartDao = new CartDaoImpl();
    private static final ItemDao itemDao = new ItemDaoImpl();
    private static final OrderDao orderDao = new OrderDaoImpl();
    private static final QuestionDao questionDao = new QuestionDaoImpl();
    private static final CommentDao commentDao = new CommentDaoImpl();
    private static final BitmapDao bitmapDao = new BitmapDaoImpl();

    private DaoFactory() {
    }

    public static DaoFactory getInstance() {
        return INSTANCE;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public CartDao getCartDao() {
        return cartDao;
    }

    public ItemDao getItemDao() {
        return itemDao;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

    public QuestionDao getQuestionDao() {
        return questionDao;
    }

    public CommentDao getCommentDao() {
        return commentDao;
    }

    public BitmapDao getBitmapDao() {
        return bitmapDao;
    }
}
