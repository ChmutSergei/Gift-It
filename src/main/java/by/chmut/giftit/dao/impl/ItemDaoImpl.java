package by.chmut.giftit.dao.impl;

import by.chmut.giftit.dao.DaoException;
import by.chmut.giftit.dao.ItemDao;
import by.chmut.giftit.entity.Item;
import by.chmut.giftit.entity.Order;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The Item dao class provides
 * manipulation of Item entity with database.
 *
 * @author Sergei Chmut.
 */
public class ItemDaoImpl implements ItemDao {

    /**
     * The constant SQL query SELECT_ALL_ITEMS.
     */
    private static final String SELECT_ALL_ITEMS =
            "SELECT id, name, type, description, active, cost, image " +
                    "FROM Items " +
                    "ORDER BY id DESC LIMIT ? OFFSET ?";
    /**
     * The constant SQL query SELECT_ITEM_BY_ID.
     */
    private static final String SELECT_ITEM_BY_ID =
            "SELECT id, name, type, description, active, cost, image " +
                    "FROM Items " +
                    "WHERE id = ?";
    /**
     * The constant SQL query SELECT_ITEM_BY_COMMENT_ID.
     */
    private static final String SELECT_ITEM_BY_COMMENT_ID =
            "SELECT i.id, i.name " +
                    "FROM Items i " +
                    "JOIN Comments c on i.id = c.item_id " +
                    "WHERE c.id = ?";
    /**
     * The constant SQL query SELECT_PAID_ITEMS.
     */
    private static final String SELECT_PAID_ITEMS =
            "SELECT i.id, i.name, i.type, i.description, i.active, i.cost, i.image " +
                    "FROM Orders o " +
                    "JOIN Carts c on o.id = c.order_id " +
                    "JOIN Items i on c.item_id = i.id " +
                    "WHERE (o.status = ? OR o.status = ?) AND o.user_id = ?";
    /**
     * The constant SQL query SELECT_ITEMS_FOR_ORDER.
     */
    private static final String SELECT_ITEMS_FOR_ORDER =
            "SELECT i.id, i.name, i.type, i.description, i.active, i.cost, c.count " +
                    "FROM Orders o " +
                    "JOIN Carts c on o.id = c.order_id " +
                    "JOIN Items i on c.item_id = i.id " +
                    "WHERE o.status = ? AND o.id = ?";
    /**
     * The constant SQL query COUNT_ITEM.
     */
    private static final String COUNT_ITEM =
            "SELECT count(*) " +
                    "FROM Items";
    /**
     * The constant SQL query DELETE_ITEM.
     */
    private static final String DELETE_ITEM =
            "DELETE FROM Items " +
                    "WHERE id=?";
    /**
     * The constant SQL query CREATE_ITEM.
     */
    private static final String CREATE_ITEM =
            "INSERT INTO Items(name, type, description, active, cost, image) " +
                    "VALUES(?,?,?,?,?,?)";
    /**
     * The constant SQL query UPDATE_ITEM.
     */
    private static final String UPDATE_ITEM =
            "UPDATE Items SET name=?, type=?, description=?, active=?, cost=? " +
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
     * Find list of all items from offset with limit.
     *
     * @param filePath the file path for item image
     * @param limit    the limit of item
     * @param offset   return item from offset
     * @return the list of items
     * @throws DaoException if find all items can't be handled
     */
    @Override
    public List<Item> findAllWithLimit(String filePath, int limit, int offset) throws DaoException {
        List<Item> items = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_ALL_ITEMS);
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Item item = makeFromResultSet(resultSet, filePath);
                items.add(item);
            }
        } catch (IOException | SQLException exception) {
            throw new DaoException("Error with get all items", exception);
        } finally {
            close(resultSet);
            close(statement);
        }
        return items;
    }

    /**
     * Find item by id.
     *
     * @param id   the item id
     * @param filePath the file path for item image
     * @return the optional item
     * @throws DaoException if find item can't be handled
     */
    @Override
    public Optional<Item> find(Long id, String filePath) throws DaoException {
        Item item = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_ITEM_BY_ID);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                item = makeFromResultSet(resultSet, filePath);
            }
        } catch (IOException | SQLException exception) {
            throw new DaoException("Error with get item by id", exception);
        } finally {
            close(resultSet);
            close(statement);
        }
        return Optional.ofNullable(item);
    }

    /**
     * Find list of paid items with such user id.
     *
     * @param userId   the user id
     * @param filePath the file path for item image
     * @return the list of items
     * @throws DaoException if find paid items can't be handled
     */
    @Override
    public List<Item> findPaidItems(long userId, String filePath) throws DaoException {
        List<Item> items = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_PAID_ITEMS);
            statement.setString(1, Order.OrderStatus.PAID.toString());
            statement.setString(2, Order.OrderStatus.DONE.toString());
            statement.setLong(3, userId);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Item item = makeFromResultSet(resultSet, filePath);
                items.add(item);
            }
        } catch (IOException | SQLException exception) {
            throw new DaoException("Error with get paid items", exception);
        } finally {
            close(resultSet);
            close(statement);
        }
        return items;
    }

    /**
     * Find item by comment id.
     *
     * @param commentId the comment id
     * @return the optional item
     * @throws DaoException if find items by comment id can't be handled
     */
    @Override
    public Optional<Item> findByCommentId(long commentId) throws DaoException {
        Item item = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_ITEM_BY_COMMENT_ID);
            statement.setLong(1, commentId);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                item = new Item();
                item.setItemId(resultSet.getLong(1));
                item.setItemName(resultSet.getString(2));
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with get item by id", exception);
        } finally {
            close(resultSet);
            close(statement);
        }
        return Optional.ofNullable(item);
    }

    /**
     * Find list of item with such order.
     *
     * @param orderId the order id
     * @return the list of items
     * @throws DaoException if find items for order can't be handled
     */
    @Override
    public List<Item> findForOrder(long orderId) throws DaoException {
        List<Item> items = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_ITEMS_FOR_ORDER);
            statement.setString(1, Order.OrderStatus.PAID.toString());
            statement.setLong(2, orderId);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Item item = new Item();
                item.setItemId(resultSet.getLong(1));
                item.setItemName(resultSet.getString(2));
                item.setType(resultSet.getString(3));
                item.setDescription(resultSet.getString(4));
                item.setActive(resultSet.getBoolean(5));
                item.setPrice(resultSet.getBigDecimal(6));
                item.setCount(resultSet.getBigDecimal(7));
                items.add(item);
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with get items for Order", exception);
        } finally {
            close(resultSet);
            close(statement);
        }
        return items;
    }

    /**
     * Count all item in database.
     *
     * @return the count of item
     * @throws DaoException if count all items can't be handled
     */
    @Override
    public int countAllItem() throws DaoException {
        int count = 0;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(COUNT_ITEM);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with get item by id", exception);
        } finally {
            close(resultSet);
            close(statement);
        }
        return count;
    }

    /**
     * Method make item from result set.
     *
     * @param resultSet the result set
     * @param filePath  the file path for item image
     * @return the item
     * @throws SQLException if item from resultSet can't be handled
     * @throws IOException  if io exception happens when making the item from resultSet
     */
    private Item makeFromResultSet(ResultSet resultSet, String filePath) throws SQLException, IOException {
        Item item = new Item();
        item.setItemId(resultSet.getLong(1));
        item.setItemName(resultSet.getString(2));
        item.setType(resultSet.getString(3));
        item.setDescription(resultSet.getString(4));
        item.setActive(resultSet.getBoolean(5));
        item.setPrice(resultSet.getBigDecimal(6));
        byte[] imageBlob = resultSet.getBytes(7);
        if (imageBlob != null) {
            File image = new File(filePath + item.getItemId() + ".jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(image);
            fileOutputStream.write(imageBlob);
            fileOutputStream.close();
            item.setImage(image);
        }
        return item;
    }

    /**
     * Find entity by id - unsupported.
     *
     * @param id entity's id
     * @return the optional Entity
     * @throws DaoException if try to use this method
     */
    @Override
    public Optional<Item> findEntity(Long id) throws DaoException {
        throw new DaoException("Unsupported operation exception!");
    }

    /**
     * Delete item by id.
     *
     * @param id item id
     * @return true if delete done otherwise false
     * @throws DaoException if delete can't be handled
     */
    @Override
    public boolean delete(Long id) throws DaoException {
        PreparedStatement statement = null;
        int result;
        try {
            statement = connection.prepareStatement(DELETE_ITEM);
            statement.setLong(1, id);
            result = statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DaoException("Error with delete item", exception);
        } finally {
            close(statement);
        }
        return result > 0;
    }

    /**
     * Add item to database.
     *
     * @param item the item
     * @return Item that was created
     * @throws DaoException if create entity can't be handled
     */
    @Override
    public Item create(Item item) throws DaoException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        File image = item.getImage();
        try (FileInputStream fis = new FileInputStream(image)) {
            statement = connection.prepareStatement(CREATE_ITEM, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, item.getItemName());
            statement.setString(2, item.getType());
            statement.setString(3, item.getDescription());
            statement.setBoolean(4, item.isActive());
            statement.setBigDecimal(5, item.getPrice());
            statement.setBinaryStream(6, fis, (int) image.length());
            int result = statement.executeUpdate();
            if (result != 1) {
                throw new DaoException("Error with creating item");
            }
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                item.setItemId(resultSet.getLong(1));
            }
        } catch (IOException | SQLException exception) {
            throw new DaoException("Error with creating item", exception);
        } finally {
            close(resultSet);
            close(statement);
        }
        return item;
    }

    /**
     * Update item in database.
     *
     * @param item the item
     * @return Item that was updated
     * @throws DaoException if update entity can't be handled
     */
    @Override
    public Item update(Item item) throws DaoException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(UPDATE_ITEM);
            statement.setLong(6, item.getItemId());
            statement.setString(1, item.getItemName());
            statement.setString(2, item.getType());
            statement.setString(3, item.getDescription());
            statement.setBoolean(4, item.isActive());
            statement.setBigDecimal(5, item.getPrice());
            int result = statement.executeUpdate();
            if (result != 1) {
                throw new DaoException("Error with update item");
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with update item", exception);
        } finally {
            close(statement);
        }
        return item;
    }

    /**
     * Find all - unsupported operation.
     *
     * @return the list
     * @throws DaoException if try to use this method
     */
    @Override
    public List<Item> findAll() throws DaoException {
        throw new DaoException("Unsupported operation exception!");
    }

    /**
     * Delete item.
     *
     * @param item the item
     * @return true if delete done otherwise false
     * @throws DaoException if delete can't be handled
     */
    @Override
    public boolean delete(Item item) throws DaoException {
        return delete(item.getItemId());
    }
}
