package by.chmut.giftit.service;

import by.chmut.giftit.entity.Cart;
import by.chmut.giftit.entity.Item;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CartService {

    List<Cart> getCart(long userId) throws ServiceException;

    Optional<Cart> findCart(long cartId) throws ServiceException;

    Map<Long,Item> getItemsForCart(List<Cart> cartList, String realPath) throws ServiceException;

    Cart create(long itemId, long userId, BigDecimal count) throws ServiceException;

    boolean delete(long cartId) throws ServiceException;

    boolean deleteAll(long userId) throws ServiceException;
}
