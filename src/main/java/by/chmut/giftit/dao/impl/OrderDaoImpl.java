package by.chmut.giftit.dao.impl;

import by.chmut.giftit.dao.DaoException;
import by.chmut.giftit.dao.OrderDao;
import by.chmut.giftit.entity.Order;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The Order dao class provides
 * manipulation of Order entity with database.
 *
 * @author Sergei Chmut.
 */
public class OrderDaoImpl implements OrderDao {

    /**
     * The constant SQL query SELECT_ALL_ORDERS.
     */
    private static final String SELECT_ALL_ORDERS =
            "SELECT id, user_id, details, status, init_date, issue_date " +
                    "FROM Orders";
    /**
     * The constant SQL query SELECT_ORDERS_BY_STATUS.
     */
    private static final String SELECT_ORDERS_BY_STATUS =
            "SELECT id, user_id, details, status, init_date, issue_date " +
                    "FROM Orders " +
                    "WHERE status = ?";
    /**
     * The constant SQL query SELECT_ORDER_BY_ID.
     */
    private static final String SELECT_ORDER_BY_ID =
            "SELECT id, user_id, details, status, init_date, issue_date " +
                    "FROM Orders " +
                    "WHERE id = ?";
    /**
     * The constant SQL query DELETE_ORDER.
     */
    private static final String DELETE_ORDER =
            "DELETE FROM Orders " +
                    "WHERE id=?";
    /**
     * The constant SQL query CREATE_ORDER.
     */
    private static final String CREATE_ORDER =
            "INSERT INTO Orders(user_id, details, status, init_date, issue_date) " +
                    "VALUES(?,?,?,?,?)";
    /**
     * The constant SQL query UPDATE_ORDER.
     */
    private static final String UPDATE_ORDER =
            "UPDATE Orders SET user_id=?, details=?, status=?, init_date=?, issue_date=? " +
                    "WHERE id=?";
    /**
     * The constant SQL query UPDATE_ORDER_SET_PAID_STATUS.
     */
    private static final String UPDATE_ORDER_SET_PAID_STATUS =
            "UPDATE Orders SET status=? " +
                    "WHERE id= ?";

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
     * Find all order from the database.
     *
     * @return the list of order
     * @throws DaoException if find all can't be handled
     */
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

    /**
     * Find order by id.
     *
     * @param id order id
     * @return the optional Order
     * @throws DaoException if find entity can't be handled
     */
    @Override
    public Optional<Order> findEntity(Long id) throws DaoException {
        Order order = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_ORDER_BY_ID);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                order = makeFromResultSet(resultSet);
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with get order by id", exception);
        } finally {
            close(statement);
            close(resultSet);
        }
        return Optional.ofNullable(order);
    }

    /**
     * Method make order from result set.
     *
     * @param resultSet the result set
     * @return the order
     * @throws SQLException if order from resultSet can't be handled
     */
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

    /**
     * Delete order by id.
     *
     * @param id order id
     * @return true if delete done otherwise false
     * @throws DaoException if delete can't be handled
     */
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

    /**
     * Delete order.
     *
     * @param order the order
     * @return true if delete done otherwise false
     * @throws DaoException if delete can't be handled
     */
    @Override
    public boolean delete(Order order) throws DaoException {
        return delete(order.getOrderId());
    }

    /**
     * Add order to database.
     *
     * @param order the order
     * @return Order that was created
     * @throws DaoException if create order can't be handled
     */
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
            LocalDate date = order.getIssueDate();
            Date issueDate = date != null ? Date.valueOf(date) : null;
            statement.setDate(5, issueDate);
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

    /**
     * Update order in database.
     *
     * @param order the order
     * @return Order that was updated
     * @throws DaoException if update order can't be handled
     */
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

    /**
     * Find list of all orders with Paid status.
     *
     * @return the list of orders
     * @throws DaoException if find paid orders can't be handled
     */
    @Override
    public List<Order> findPaidOrder() throws DaoException {
        List<Order> orders = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_ORDERS_BY_STATUS);
            statement.setString(1, Order.OrderStatus.PAID.name());
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

    /**
     * Sets paid status for order with such id.
     *
     * @param orderId the order id
     * @return true with success otherwise false
     * @throws DaoException if set paid for order can't be handled
     */
    @Override
    public boolean setPaidStatus(long orderId) throws DaoException {
        boolean result = false;
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(UPDATE_ORDER_SET_PAID_STATUS);
            statement.setLong(2, orderId);
            statement.setString(1, Order.OrderStatus.PAID.name());
            int rows = statement.executeUpdate();
            if (rows == 1) {
                result = true;
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with update order", exception);
        } finally {
            close(statement);
        }
        return result;
    }
}
