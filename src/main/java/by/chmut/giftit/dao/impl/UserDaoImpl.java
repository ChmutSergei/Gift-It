package by.chmut.giftit.dao.impl;

import by.chmut.giftit.dao.DaoException;
import by.chmut.giftit.dao.UserDao;
import by.chmut.giftit.entity.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The User dao class provides
 * manipulation of User entity with database.
 *
 * @author Sergei Chmut.
 */
public class UserDaoImpl implements UserDao {

    /**
     * The constant SQL query SELECT_ALL_USERS.
     */
    private static final String SELECT_ALL_USERS =
            "SELECT id, username, password, first_name, last_name, email, phone, address, account, init_date, blocked_until, role " +
                    "FROM Users";
    /**
     * The constant SQL query SELECT_USER_BY_USERNAME.
     */
    private static final String SELECT_USER_BY_USERNAME =
            "SELECT id, username, password, first_name, last_name, email, phone, address, account, init_date, blocked_until, role " +
                    "FROM Users " +
                    "WHERE username = ?";
    /**
     * The constant SQL query SELECT_USER_BY_PART_OF_USERNAME.
     */
    private static final String SELECT_USER_BY_PART_OF_USERNAME =
            "SELECT id, username, password, first_name, last_name, email, phone, address, account, init_date, blocked_until, role " +
                    "FROM Users " +
                    "WHERE username LIKE ?";
    /**
     * The constant SQL query SELECT_USER_BY_INIT_DATE.
     */
    private static final String SELECT_USER_BY_INIT_DATE =
            "SELECT id, username, password, first_name, last_name, email, phone, address, account, init_date, blocked_until, role " +
                    "FROM Users " +
                    "WHERE init_date >= ?";
    /**
     * The constant SQL query SELECT_USER_BY_ID.
     */
    private static final String SELECT_USER_BY_ID =
            "SELECT id, username, password, first_name, last_name, email, phone, address, account, init_date, blocked_until, role " +
                    "FROM Users " +
                    "WHERE id = ?";
    /**
     * The constant SQL query DELETE_USER.
     */
    private static final String DELETE_USER =
            "DELETE FROM Users " +
                    "WHERE id=?";
    /**
     * The constant SQL query CREATE_USER.
     */
    private static final String CREATE_USER =
            "INSERT INTO Users(username, password, first_name, last_name, email, phone, address, account, init_date, blocked_until, role) " +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
    /**
     * The constant SQL query UPDATE_USER.
     */
    private static final String UPDATE_USER =
            "UPDATE Users SET username=?, password=?, first_name=?, last_name=?, email=?, phone=?, address=?, account=?, " +
                    "init_date=?, blocked_until=?, role=? " +
                    "WHERE id=?";
    /**
     * The constant SQL query UPDATE_USER_BLOCK_FOR_DAYS.
     */
    private static final String UPDATE_USER_BLOCK_FOR_DAYS =
            "UPDATE Users SET blocked_until=? " +
                    "WHERE id=?";


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
     * Find all user from the database.
     *
     * @return the list of User
     * @throws DaoException if find all can't be handled
     */
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

    /**
     * Find user by username.
     *
     * @param username the username for search
     * @return the optional user
     * @throws DaoException if find user by username can't be handled
     */
    public Optional<User> findByUsername(String username) throws DaoException {
        User user = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_USER_BY_USERNAME);
            statement.setString(1, username);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user = makeFromResultSet(resultSet);
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with get user by username", exception);
        } finally {
            close(statement);
            close(resultSet);
        }
        return Optional.ofNullable(user);
    }

    /**
     * Find all users with such part of username.
     *
     * @param part the part of username for search
     * @return the list of users
     * @throws DaoException if find user by part of username can't be handled
     */
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
            while (resultSet.next()) {
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

    /**
     * Find all users with such init date.
     *
     * @param initDate the date when user init
     * @return the list of users
     * @throws DaoException if find user by init date can't be handled
     */
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

    /**
     * Block user with such id for a given number of days.
     *
     * @param userId           the user id
     * @param countDayForBlock the count day for block
     * @return true if set block otherwise false
     * @throws DaoException if block user can't be handled
     */
    @Override
    public boolean blockForDays(long userId, int countDayForBlock) throws DaoException {
        boolean result = true;
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(UPDATE_USER_BLOCK_FOR_DAYS);
            statement.setLong(2, userId);
            LocalDate blockedUntil = LocalDate.now().plusDays(countDayForBlock);
            statement.setDate(1, Date.valueOf(blockedUntil));
            int rows = statement.executeUpdate();
            if (rows != 1) {
                result = false;
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with update User", exception);
        } finally {
            close(statement);
        }
        return result;
    }

    /**
     * Find user by id.
     *
     * @param id user id
     * @return the optional User
     * @throws DaoException if find user can't be handled
     */
    @Override
    public Optional<User> findEntity(Long id) throws DaoException {
        User user = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_USER_BY_ID);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user = makeFromResultSet(resultSet);
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with get user by id", exception);
        } finally {
            close(statement);
            close(resultSet);
        }
        return Optional.ofNullable(user);
    }

    /**
     * Method make user from result set.
     *
     * @param resultSet the result set
     * @return the user
     * @throws SQLException if user from resultSet can't be handled
     */
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

    /**
     * Delete user by id.
     *
     * @param id user id
     * @return true if delete done otherwise false
     * @throws DaoException if delete can't be handled
     */
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

    /**
     * Delete user.
     *
     * @param user the user
     * @return true if delete done otherwise false
     * @throws DaoException if delete can't be handled
     */
    @Override
    public boolean delete(User user) throws DaoException {
        return delete(user.getUserId());
    }

    /**
     * Add user to database.
     *
     * @param user the user
     * @return User that was created
     * @throws DaoException if create user can't be handled
     */
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

    /**
     * Update user in database.
     *
     * @param user the user
     * @return user that was updated
     * @throws DaoException if update user can't be handled
     */
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
