package by.chmut.giftit.dao;

import by.chmut.giftit.dao.impl.*;

/**
 * The Dao factory class represents access to shared facilities Dao.
 *
 * @author Sergei Chmut.
 */
public class DaoFactory {

    private static final DaoFactory INSTANCE = new DaoFactory();
    private final UserDao userDao = new UserDaoImpl();
    private final CartDao cartDao = new CartDaoImpl();
    private final ItemDao itemDao = new ItemDaoImpl();
    private final OrderDao orderDao = new OrderDaoImpl();
    private final QuestionDao questionDao = new QuestionDaoImpl();
    private final CommentDao commentDao = new CommentDaoImpl();
    private final BitmapDao bitmapDao = new BitmapDaoImpl();

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
