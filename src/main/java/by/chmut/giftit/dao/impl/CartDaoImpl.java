package by.chmut.giftit.dao.impl;

import by.chmut.giftit.dao.CartDao;
import by.chmut.giftit.dao.DaoException;
import by.chmut.giftit.entity.Cart;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDaoImpl implements CartDao {

    private static final String SELECT_ALL_CARTS = "SELECT id, user_id, item_id, count FROM Carts";
    private static final String SELECT_CART_BY_ID = "SELECT id, user_id, item_id, count FROM Carts WHERE id = ?";
    private static final String DELETE_CART = "DELETE FROM Carts WHERE id=?";
    private static final String CREATE_CART = "INSERT INTO Carts(user_id, item_id, count) VALUES(?,?,?)";
    private static final String UPDATE_CART = "UPDATE Carts SET user_id=?, item_id=?, count=? WHERE id=?";

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

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

    @Override
    public Cart findEntity(Long id) throws DaoException {
        Cart cart = new Cart();
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
        return cart;
    }

    private Cart makeFromResultSet(ResultSet resultSet) throws SQLException {
        Cart cart = new Cart();
        cart.setCartId(resultSet.getLong(1));
        cart.setUserId(resultSet.getLong(2));
        cart.setItemId(resultSet.getLong(3));
        cart.setCount(resultSet.getBigDecimal(4));
        return cart;
    }

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

    @Override
    public boolean delete(Cart cart) throws DaoException {
        return delete(cart.getCartId());
    }

    @Override
    public boolean create(Cart cart) throws DaoException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int result;
        try {
            statement = connection.prepareStatement(CREATE_CART, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, cart.getUserId());
            statement.setLong(2, cart.getItemId());
            statement.setBigDecimal(3, cart.getCount());
            result = statement.executeUpdate();
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
        return result > 0;
    }

    @Override
    public Cart update(Cart cart) throws DaoException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(UPDATE_CART);
            statement.setLong(4, cart.getCartId());
            statement.setLong(1, cart.getUserId());
            statement.setLong(2, cart.getItemId());
            statement.setBigDecimal(3, cart.getCount());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DaoException("Error with update cart", exception);
        } finally {
            close(statement);
        }
        return cart;
    }
}
