package by.chmut.giftit.service.impl;

import by.chmut.giftit.dao.*;
import by.chmut.giftit.entity.Cart;
import by.chmut.giftit.entity.Item;
import by.chmut.giftit.service.CartService;
import by.chmut.giftit.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The Cart service class implements business logic methods
 * for user entity.
 *
 * @author Sergei Chmut.
 */
public class CartServiceImpl implements CartService {

    /**
     * The logger for logging possible errors.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * The Cart dao provides access to the database for Cart entity.
     */
    private CartDao cartDao = DaoFactory.getInstance().getCartDao();
    /**
     * The Item dao provides access to the database for Item entity.
     */
    private ItemDao itemDao = DaoFactory.getInstance().getItemDao();
    /**
     * The transaction manager provides preparation for conducting and confirming transactions.
     */
    private TransactionManager manager = new TransactionManager();

    /**
     * Find list of carts by user id.
     *
     * @param userId the user id
     * @return the list of carts
     * @throws ServiceException if find cart by user id can't be handled
     */
    @Override
    public List<Cart> findCartByUserId(long userId) throws ServiceException {
        List<Cart> carts;
        try {
            manager.beginTransaction(cartDao);
            carts = cartDao.findAllByUserId(userId);
            manager.endTransaction();
        } catch (DaoException exception) {
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            throw new ServiceException(exception);
        }
        return carts;
    }

    /**
     * Find cart by id.
     *
     * @param cartId the cart id
     * @return the optional cart
     * @throws ServiceException if find cart by id can't be handled
     */
    @Override
    public Optional<Cart> findCart(long cartId) throws ServiceException {
        Optional<Cart> cart;
        try {
            manager.beginTransaction(cartDao);
            cart = cartDao.findEntity(cartId);
            manager.endTransaction();
        } catch (DaoException exception) {
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            throw new ServiceException(exception);
        }
        return cart;
    }

    /**
     * Find items for given carts.
     * The method, iterated over the list of transmitted carts,
     * receives for each cart of its items and returns
     * a map where each cart corresponds to a item.
     *
     * @param cartList the list of carts
     * @param pathForFile the path for file
     * @return the items for cart
     * @throws ServiceException if find item for cart can't be handled
     */
    @Override
    public Map<Long, Item> findItemsForCart(List<Cart> cartList, String pathForFile) throws ServiceException {
        Map<Long, Item> items = new HashMap<>();
        try {
            manager.beginTransaction(itemDao);
            for (Cart cart : cartList) {
                Optional<Item> optionalItem = itemDao.find(cart.getItemId(), pathForFile);
                optionalItem.ifPresent(item -> items.put(cart.getCartId(), item));
            }
            manager.endTransaction();
        } catch (DaoException exception) {
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            throw new ServiceException(exception);
        }
        return items;
    }

    /**
     * Create new cart and save.
     *
     * @param itemId the item id
     * @param userId the user id
     * @param count  the count of items
     * @return the created cart
     * @throws ServiceException if cart can't be created
     */
    @Override
    public Cart create(long itemId, long userId, BigDecimal count) throws ServiceException {
        Cart cart = createWithParameters(itemId, userId, count);
        try {
            manager.beginTransaction(cartDao);
            cartDao.create(cart);
            manager.endTransaction();
        } catch (DaoException exception) {
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            throw new ServiceException(exception);
        }
        return cart;
    }

    /**
     * Delete cart by id.
     *
     * @param cartId the cart id
     * @return true if success otherwise false
     * @throws ServiceException if delete cart can't be handled
     */
    @Override
    public boolean delete(long cartId) throws ServiceException {
        boolean result;
        try {
            manager.beginTransaction(cartDao);
            result = cartDao.delete(cartId);
            manager.endTransaction();
        } catch (DaoException exception) {
            try {
                manager.rollback();
            } catch (DaoException rollbackException) {
                logger.error(rollbackException);
            }
            throw new ServiceException(exception);
        }
        return result;
    }

    /**
     * Delete all cart for given user.
     *
     * @param userId the user id
     * @return true if done otherwise false
     * @throws ServiceException if an exception occurs while deleting all cart on user id
     */
    @Override
    public boolean deleteAll(long userId) throws ServiceException {
        boolean result;
        try {
            manager.beginTransaction(cartDao);
            result = cartDao.deleteAllByUserId(userId);
            manager.endTransaction();
        } catch (DaoException exception) {
            throw new ServiceException(exception);
        }
        return result;
    }

    /**
     * Create new Cart object
     *
     * @param itemId the item id
     * @param userId the user id
     * @param count  the count of item in this cart
     * @return the new Cart
     */
    private Cart createWithParameters(long itemId, long userId, BigDecimal count) {
        Cart cart = new Cart();
        cart.setItemId(itemId);
        cart.setUserId(userId);
        cart.setCount(count);
        return cart;
    }
}
