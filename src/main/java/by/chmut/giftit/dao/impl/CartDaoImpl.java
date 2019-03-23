package by.chmut.giftit.dao.impl;

import by.chmut.giftit.dao.CartDao;
import by.chmut.giftit.dao.DaoException;
import by.chmut.giftit.entity.Cart;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The Cart dao class provides
 * manipulation of Cart entity with database.
 *
 * @author Sergei Chmut.
 */
public class CartDaoImpl implements CartDao {

    /**
     * The constant SQL query SELECT_ALL_CARTS.
     */
    private static final String SELECT_ALL_CARTS =
            "SELECT id, user_id, item_id, order_id, count " +
                    "FROM Carts";
    /**
     * The constant SQL query SELECT_ALL_CARTS_ON_USER_ID.
     */
    private static final String SELECT_ALL_CARTS_ON_USER_ID =
            "SELECT id, user_id, item_id, order_id, count " +
                    "FROM Carts " +
                    "WHERE user_id = ? AND order_id IS NULL";
    /**
     * The constant SQL query SELECT_CART_BY_ID.
     */
    private static final String SELECT_CART_BY_ID =
            "SELECT id, user_id, item_id, order_id, count " +
                    "FROM Carts " +
                    "WHERE id = ?";
    /**
     * The constant SQL query DELETE_CART.
     */
    private static final String DELETE_CART =
            "DELETE FROM Carts " +
                    "WHERE id = ?";
    /**
     * The constant SQL query DELETE_ALL_CART_BY_USER_ID.
     */
    private static final String DELETE_ALL_CART_BY_USER_ID =
            "DELETE FROM Carts " +
                    "WHERE user_id = ?";
    /**
     * The constant SQL query CREATE_CART.
     */
    private static final String CREATE_CART =
            "INSERT INTO Carts(user_id, item_id, count) " +
                    "VALUES(?,?,?)";
    /**
     * The constant SQL query UPDATE_CART.
     */
    private static final String UPDATE_CART =
            "UPDATE Carts SET user_id=?, item_id=?, order_id=? count=? " +
                    "WHERE id = ?";
    /**
     * The constant SQL query UPDATE_CART_SET_ORDER_ID.
     */
    private static final String UPDATE_CART_SET_ORDER_ID =
            "UPDATE Carts SET order_id=? " +
                    "WHERE id = ?";

    /**
     * The Connection instance for working with database.
     */
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Find all carts.
     *
     * @return the list of cart
     * @throws DaoException if find all can't be handled
     */
    @Override
    public List<Cart> findAll() throws DaoException {
        List<Cart> carts = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_ALL_CARTS);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Cart cart = makeFromResultSet(resultSet);
                carts.add(cart);
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with get all carts", exception);
        } finally {
            close(statement);
            close(resultSet);
        }
        return carts;
    }

    /**
     * Find cart by id.
     *
     * @param id the cart id
     * @return the optional cart
     * @throws DaoException if find cart can't be handled
     */
    @Override
    public Optional<Cart> findEntity(Long id) throws DaoException {
        Cart cart = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_CART_BY_ID);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cart = makeFromResultSet(resultSet);
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with get cart by id", exception);
        } finally {
            close(statement);
            close(resultSet);
        }
        return Optional.ofNullable(cart);
    }

    /**
     * Method make cart from result set.
     *
     * @param resultSet the result set
     * @return the cart
     * @throws SQLException if cart from resultSet can't be handled
     */
    private Cart makeFromResultSet(ResultSet resultSet) throws SQLException {
        Cart cart = new Cart();
        cart.setCartId(resultSet.getLong(1));
        cart.setUserId(resultSet.getLong(2));
        cart.setItemId(resultSet.getLong(3));
        cart.setOrderId(resultSet.getLong(4));
        cart.setCount(resultSet.getBigDecimal(5));
        return cart;
    }

    /**
     * Delete cart by id.
     *
     * @param id the id
     * @return true if the cart was deleted otherwise false
     * @throws DaoException if delete by id can't be handled
     */
    @Override
    public boolean delete(Long id) throws DaoException {
        PreparedStatement statement = null;
        int result;
        try {
            statement = connection.prepareStatement(DELETE_CART);
            statement.setLong(1, id);
            result = statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DaoException("Error with delete cart", exception);
        } finally {
            close(statement);
        }
        return result > 0;
    }

    /**
     * Delete cart.
     *
     * @param cart the cart
     * @return true if the cart was deleted otherwise false
     * @throws DaoException if delete can't be handled
     */
    @Override
    public boolean delete(Cart cart) throws DaoException {
        return delete(cart.getCartId());
    }

    /**
     * Delete all carts with such user id.
     *
     * @param userId the user id
     * @return true if success otherwise false
     * @throws DaoException if delete all cart by user id can't be handled
     */
    @Override
    public boolean deleteAllByUserId(long userId) throws DaoException {
        PreparedStatement statement = null;
        int result;
        try {
            statement = connection.prepareStatement(DELETE_ALL_CART_BY_USER_ID);
            statement.setLong(1, userId);
            result = statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DaoException("Error with delete all cart on userId", exception);
        } finally {
            close(statement);
        }
        return result > 0;
    }

    /**
     * Add new cart to database.
     *
     * @param cart the cart
     * @return the new cart
     * @throws DaoException if create cart can't be handled
     */
    @Override
    public Cart create(Cart cart) throws DaoException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(CREATE_CART, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, cart.getUserId());
            statement.setLong(2, cart.getItemId());
            statement.setBigDecimal(3, cart.getCount());
            int result = statement.executeUpdate();
            if (result != 1) {
                throw new DaoException("Error with creating cart");
            }
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                cart.setCartId(resultSet.getLong(1));
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with creating cart", exception);
        } finally {
            close(resultSet);
            close(statement);
        }
        return cart;
    }

    /**
     * Update cart in database.
     *
     * @param cart the updated cart
     * @return the cart
     * @throws DaoException if update cart can't be handled
     */
    @Override
    public Cart update(Cart cart) throws DaoException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(UPDATE_CART);
            statement.setLong(4, cart.getCartId());
            statement.setLong(1, cart.getUserId());
            statement.setLong(2, cart.getItemId());
            statement.setLong(3, cart.getOrderId());
            statement.setBigDecimal(4, cart.getCount());
            int result = statement.executeUpdate();
            if (result != 1) {
                throw new DaoException("Error with update cart");
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with update cart", exception);
        } finally {
            close(statement);
        }
        return cart;
    }

    /**
     * Find list of cart with such user id.
     *
     * @param userId the user id
     * @return the list of cart
     * @throws DaoException if find all Cart by user id can't be handled
     */
    @Override
    public List<Cart> findAllByUserId(long userId) throws DaoException {
        List<Cart> carts = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_ALL_CARTS_ON_USER_ID);
            statement.setLong(1, userId);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Cart cart = makeFromResultSet(resultSet);
                carts.add(cart);
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with get all carts on userId", exception);
        } finally {
            close(statement);
            close(resultSet);
        }
        return carts;
    }

    /**
     * Sets order id in Cart with such cart id.
     *
     * @param cartId  the cart where order id will be set
     * @param orderId the order id to be installed
     * @return true if done otherwise false
     * @throws DaoException if set order id can't be handled
     */
    @Override
    public boolean setOrderId(long cartId, long orderId) throws DaoException {
        boolean result = false;
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(UPDATE_CART_SET_ORDER_ID);
            statement.setLong(2, cartId);
            statement.setLong(1, orderId);
            int rows = statement.executeUpdate();
            if (rows == 1) {
                result = true;
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with update cart - set order id", exception);
        } finally {
            close(statement);
        }
        return result;
    }
}
