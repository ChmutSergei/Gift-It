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

public class CartServiceImpl implements CartService {

    private static final Logger logger = LogManager.getLogger();

    private CartDao cartDao = DaoFactory.getInstance().getCartDao();
    private ItemDao itemDao = DaoFactory.getInstance().getItemDao();
    private TransactionManager manager = new TransactionManager();

    @Override
    public List<Cart> getCart(long userId) throws ServiceException {
        List<Cart> carts;
        try {
            manager.beginTransaction(cartDao);
            carts = cartDao.findAllByUserId(userId);
            manager.endTransaction(cartDao);
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

    @Override
    public Optional<Cart> findCart(long cartId) throws ServiceException {
        Optional<Cart> cart;
        try {
            manager.beginTransaction(cartDao);
            cart = cartDao.findEntity(cartId);
            manager.endTransaction(cartDao);
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

    @Override
    public Map<Long, Item> getItemsForCart(List<Cart> cartList, String  pathForFile) throws ServiceException {
        Map<Long, Item> items = new HashMap<>();
        try {
            manager.beginTransaction(itemDao);
            for (Cart cart: cartList) {
                Optional<Item> optionalItem = itemDao.find(cart.getItemId(), pathForFile);
                optionalItem.ifPresent(item -> items.put(cart.getCartId(), item));
            }
            manager.endTransaction(itemDao);
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

    @Override
    public Cart create(long itemId, long userId, BigDecimal count) throws ServiceException {
        Cart cart = setParameters(itemId, userId, count);
        try {
            manager.beginTransaction(cartDao);
            cartDao.create(cart);
            manager.endTransaction(cartDao);
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

    @Override
    public boolean delete(long cartId) throws ServiceException {
        boolean result;
        try {
            manager.beginTransaction(cartDao);
            result = cartDao.delete(cartId);
            manager.endTransaction(cartDao);
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

    @Override
    public boolean deleteAll(long userId) throws ServiceException {
        boolean result;
        try {
            manager.beginTransaction(cartDao);
            result = cartDao.deleteAllByUserId(userId);
            manager.endTransaction(cartDao);
        } catch (DaoException exception) {
            throw new ServiceException(exception);
        }
        return result;
    }

    private Cart setParameters(long itemId, long userId, BigDecimal count) {
        Cart cart = new Cart();
        cart.setItemId(itemId);
        cart.setUserId(userId);
        cart.setCount(count);
        return cart;
    }
}
