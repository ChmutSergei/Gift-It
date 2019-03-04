package by.chmut.giftit.dao.impl;

import by.chmut.giftit.dao.DaoException;
import by.chmut.giftit.dao.OrderDao;
import by.chmut.giftit.entity.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDaoImpl implements OrderDao {

    private static final String SELECT_ALL_ORDERS = "SELECT id, user_id, details, status, " +
            "init_date, issue_date FROM Orders";
    private static final String SELECT_PAID_ORDERS = "SELECT id, user_id, details, status, " +
            "init_date, issue_date FROM Orders WHERE status = 'PAID'";
    private static final String SELECT_ORDER_BY_ID = "SELECT id, user_id, details, status, " +
            "init_date, issue_date FROM Orders WHERE id = ?";
    private static final String DELETE_ORDER = "DELETE FROM Orders WHERE id=?";
    private static final String CREATE_ORDER = "INSERT INTO Orders(user_id, details, status, " +
            "init_date, issue_date) VALUES(?,?,?,?,?,?)";
    private static final String UPDATE_ORDER = "UPDATE Orders SET user_id=?, details=?, status=?, " +
            "init_date=?, issue_date=? WHERE id=?";

    private Connection connection;
    public Connection getConnection() {
        return connection;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Order> findAll() throws DaoException {
        List<Order> orders = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_ALL_ORDERS);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Order order = makeFromResultSet(resultSet);
                orders.add(order);
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with get all orders", exception);
        } finally {
            close(resultSet);
            close(statement);

        }
        return orders;
    }

    @Override
    public Optional<Order> findEntity(Long id) throws DaoException {
        Optional<Order> order = Optional.empty();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_ORDER_BY_ID);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                order = Optional.of(makeFromResultSet(resultSet));
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with get order by id", exception);
        } finally {
            close(statement);
            close(resultSet);
        }
        return order;
    }

    private Order makeFromResultSet(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setOrderId(resultSet.getLong(1));
        order.setUserId(resultSet.getLong(2));
        order.setDetails(resultSet.getString(3));
        order.setOrderStatus(Order.OrderStatus.valueOf(resultSet.getString(4)));
        order.setInitDate(resultSet.getDate(5).toLocalDate());
        Date date = resultSet.getDate(6);
        if (date != null) {
            order.setIssueDate(date.toLocalDate());
        }
        return order;
    }

    @Override
    public boolean delete(Long id) throws DaoException {
        PreparedStatement statement = null;
        int result;
        try {
            statement = connection.prepareStatement(DELETE_ORDER);
            statement.setLong(1, id);
            result = statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DaoException("Error with delete order", exception);
        } finally {
            close(statement);
        }
        return result > 0;
    }

    @Override
    public boolean delete(Order order) throws DaoException {
        return delete(order.getOrderId());
    }

    @Override
    public Order create(Order order) throws DaoException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(CREATE_ORDER, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, order.getUserId());
            statement.setString(2, order.getDetails());
            statement.setString(3, order.getOrderStatus().toString());
            statement.setDate(4, Date.valueOf(order.getInitDate()));
            statement.setDate(5, Date.valueOf(order.getIssueDate()));
            int result = statement.executeUpdate();
            if (result != 1) {
                throw new DaoException("Error with creating order");
            }
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                order.setOrderId(resultSet.getLong(1));
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with creating order", exception);
        } finally {
            close(resultSet);
            close(statement);
        }
        return order;
    }

    @Override
    public Order update(Order order) throws DaoException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(UPDATE_ORDER);
            statement.setLong(6, order.getOrderId());
            statement.setLong(1, order.getUserId());
            statement.setString(2, order.getDetails());
            statement.setString(3, order.getOrderStatus().toString());
            statement.setDate(4, Date.valueOf(order.getInitDate()));
            statement.setDate(5, Date.valueOf(order.getIssueDate()));
            int result = statement.executeUpdate();
            if (result != 1) {
                throw new DaoException("Error with update order");
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with update order", exception);
        } finally {
            close(statement);
        }
        return order;
    }

    @Override
    public List<Order> findPaidOrder() throws DaoException {
        List<Order> orders = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_PAID_ORDERS);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Order order = makeFromResultSet(resultSet);
                orders.add(order);
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with get paid orders", exception);
        } finally {
            close(resultSet);
            close(statement);
        }
        return orders;
    }
}
