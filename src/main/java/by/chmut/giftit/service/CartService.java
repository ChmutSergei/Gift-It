package by.chmut.giftit.service;

import by.chmut.giftit.entity.Cart;
import by.chmut.giftit.entity.Item;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The interface CartService provides service
 * methods associated with Cart entity.
 *
 * @author Sergei Chmut.
 */
public interface CartService {

    /**
     * Find list of carts by user id.
     *
     * @param userId the user id
     * @return the list of carts
     * @throws ServiceException if find cart by user id can't be handled
     */
    List<Cart> findCartByUserId(long userId) throws ServiceException;

    /**
     * Find cart by id.
     *
     * @param cartId the cart id
     * @return the optional cart
     * @throws ServiceException if find cart by id can't be handled
     */
    Optional<Cart> findCart(long cartId) throws ServiceException;

    /**
     * Find items for given carts.
     *
     * @param cartList the list of carts
     * @param realPath the real path
     * @return the items for cart
     * @throws ServiceException if find item for cart can't be handled
     */
    Map<Long,Item> findItemsForCart(List<Cart> cartList, String realPath) throws ServiceException;

    /**
     * Create new cart and save.
     *
     * @param itemId the item id
     * @param userId the user id
     * @param count  the count of items
     * @return the created cart
     * @throws ServiceException if cart can't be created
     */
    Cart create(long itemId, long userId, BigDecimal count) throws ServiceException;

    /**
     * Delete cart by id.
     *
     * @param cartId the cart id
     * @return true if success otherwise false
     * @throws ServiceException if delete cart can't be handled
     */
    boolean delete(long cartId) throws ServiceException;

    /**
     * Delete all cart for given user.
     *
     * @param userId the user id
     * @return true if done otherwise false
     * @throws ServiceException if an exception occurs while deleting all cart on user id
     */
    boolean deleteAll(long userId) throws ServiceException;
}
