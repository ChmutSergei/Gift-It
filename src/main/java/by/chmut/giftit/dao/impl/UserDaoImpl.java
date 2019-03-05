package by.chmut.giftit.dao.impl;

import by.chmut.giftit.dao.DaoException;
import by.chmut.giftit.dao.UserDao;
import by.chmut.giftit.entity.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

    private static final String SELECT_ALL_USERS = "SELECT id, username, password, first_name, last_name, email, " +
            "phone, address, account, init_date, blocked_until, role FROM Users";
    private static final String SELECT_USER_BY_USERNAME = "SELECT id, username, password, first_name, last_name, " +
            "email, phone, address, account, init_date, blocked_until, role FROM Users WHERE username = ?";
    private static final String SELECT_USER_BY_PART_OF_USERNAME = "SELECT id, username, password, first_name, last_name, " +
            "email, phone, address, account, init_date, blocked_until, role FROM Users WHERE username LIKE ?";
    private static final String SELECT_USER_BY_INIT_DATE = "SELECT id, username, password, first_name, last_name, " +
            "email, phone, address, account, init_date, blocked_until, role FROM Users WHERE init_date >= ?";
    private static final String SELECT_USER_BY_ID = "SELECT id, username, password, first_name, last_name, " +
            "email, phone, address, account, init_date, blocked_until, role FROM Users WHERE id = ?";
    private static final String DELETE_USER = "DELETE FROM Users WHERE id=?";
    private static final String CREATE_USER = "INSERT INTO Users(username, password, first_name, last_name, email, " +
            "phone, address, account, init_date, blocked_until, role) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
    private static final String UPDATE_USER = "UPDATE Users SET username=?, password=?, first_name=?, last_name=?, " +
            "email=?, phone=?, address=?, account=?, init_date=?, blocked_until=?, role=? WHERE id=?";

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<User> findAll() throws DaoException {
        List<User> users = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_ALL_USERS);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = makeFromResultSet(resultSet);
                users.add(user);
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with get all Users", exception);
        } finally {
            close(statement);
            close(resultSet);
        }
        return users;
    }

    public Optional<User> findByUsername(String username) throws DaoException {
        Optional<User> user = Optional.empty();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_USER_BY_USERNAME);
            statement.setString(1, username);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = Optional.of(makeFromResultSet(resultSet));
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with get user by username", exception);
        } finally {
            close(statement);
            close(resultSet);
        }
        return user;
    }

    @Override
    public List<User> findByPartOfUsername(String part) throws DaoException {
        List<User> users = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_USER_BY_PART_OF_USERNAME);
            part = "%" + part + "%";
            statement.setString(1, part);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = makeFromResultSet(resultSet);
                users.add(user);
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with get user by part of username", exception);
        } finally {
            close(statement);
            close(resultSet);
        }
        return users;
    }

    @Override
    public List<User> findByInitDate(LocalDate initDate) throws DaoException {
        List<User> users = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_USER_BY_INIT_DATE);
            statement.setDate(1, Date.valueOf(initDate));
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = makeFromResultSet(resultSet);
                users.add(user);
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with get users by init date", exception);
        } finally {
            close(statement);
            close(resultSet);
        }
        return users;
    }

    @Override
    public Optional<User> findEntity(Long id) throws DaoException {
        Optional<User> user = Optional.empty();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_USER_BY_ID);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = Optional.of(makeFromResultSet(resultSet));
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with get user by id", exception);
        } finally {
            close(statement);
            close(resultSet);
        }
        return user;
    }

    private User makeFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setUserId(resultSet.getLong(1));
        user.setUsername(resultSet.getString(2));
        user.setPassword(resultSet.getString(3));
        user.setFirstName(resultSet.getString(4));
        user.setLastName(resultSet.getString(5));
        user.setEmail(resultSet.getString(6));
        user.setPhone(resultSet.getString(7));
        user.setAddress(resultSet.getString(8));
        user.setAccount(resultSet.getBigDecimal(9));
        user.setInitDate(resultSet.getDate(10).toLocalDate());
        user.setBlockedUntil(resultSet.getDate(11).toLocalDate());
        user.setRole(User.Role.valueOf(resultSet.getString(12)));
        return user;
    }

    @Override
    public boolean delete(Long id) throws DaoException {
        PreparedStatement statement = null;
        int result;
        try {
            statement = connection.prepareStatement(DELETE_USER);
            statement.setLong(1, id);
            result = statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DaoException("Error with delete user", exception);
        } finally {
            close(statement);
        }
        return result > 0;
    }

    @Override
    public boolean delete(User user) throws DaoException {
        return delete(user.getUserId());
    }

    @Override
    public User create(User user) throws DaoException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(CREATE_USER, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getLastName());
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getPhone());
            statement.setString(7, user.getAddress());
            statement.setBigDecimal(8, user.getAccount());
            statement.setDate(9, Date.valueOf(user.getInitDate()));
            statement.setDate(10, Date.valueOf(user.getBlockedUntil()));
            statement.setString(11, user.getRole().toString());
            int result = statement.executeUpdate();
            if (result != 1) {
                throw new DaoException("Error with creating user");
            }
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setUserId(resultSet.getLong(1));
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with creating user", exception);
        } finally {
            close(resultSet);
            close(statement);
        }
        return user;
    }

    @Override
    public User update(User user) throws DaoException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(UPDATE_USER);
            statement.setLong(12, user.getUserId());
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getLastName());
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getPhone());
            statement.setString(7, user.getAddress());
            statement.setBigDecimal(8, user.getAccount());
            statement.setDate(9, Date.valueOf(user.getInitDate()));
            statement.setDate(10, Date.valueOf(user.getBlockedUntil()));
            statement.setString(11, user.getRole().toString());
            int result = statement.executeUpdate();
            if (result != 1) {
                throw new DaoException("Error with update user");
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with update User", exception);
        } finally {
            close(statement);
        }
        return user;
    }
}
