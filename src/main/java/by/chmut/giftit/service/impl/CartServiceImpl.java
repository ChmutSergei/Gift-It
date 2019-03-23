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
 * The Cart service class.
 *
 * @author Sergei Chmut.
 */
public class CartServiceImpl implements CartService {

    /**
     * The constant logger.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * The Cart dao.
     */
    private CartDao cartDao = DaoFactory.getInstance().getCartDao();
    /**
     * The Item dao.
     */
    private ItemDao itemDao = DaoFactory.getInstance().getItemDao();
    /**
     * The Manager.
     */
    private TransactionManager manager = new TransactionManager();

    /**
     * Find cart by user id list.
     *
     * @param userId the user id
     * @return the list
     * @throws ServiceException the service exception
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
     * Find cart optional.
     *
     * @param cartId the cart id
     * @return the optional
     * @throws ServiceException the service exception
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
     * Find items for cart map.
     *
     * @param cartList    the cart list
     * @param pathForFile the path for file
     * @return the map
     * @throws ServiceException the service exception
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
     * Create cart.
     *
     * @param itemId the item id
     * @param userId the user id
     * @param count  the count
     * @return the cart
     * @throws ServiceException the service exception
     */
    @Override
    public Cart create(long itemId, long userId, BigDecimal count) throws ServiceException {
        Cart cart = setParameters(itemId, userId, count);
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
     * Delete boolean.
     *
     * @param cartId the cart id
     * @return the boolean
     * @throws ServiceException the service exception
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
     * Delete all boolean.
     *
     * @param userId the user id
     * @return the boolean
     * @throws ServiceException the service exception
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
     * Sets parameters.
     *
     * @param itemId the item id
     * @param userId the user id
     * @param count  the count
     * @return the parameters
     */
    private Cart setParameters(long itemId, long userId, BigDecimal count) {
        Cart cart = new Cart();
        cart.setItemId(itemId);
        cart.setUserId(userId);
        cart.setCount(count);
        return cart;
    }
}
