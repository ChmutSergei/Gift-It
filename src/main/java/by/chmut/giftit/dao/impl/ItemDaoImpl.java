package by.chmut.giftit.dao.impl;

import by.chmut.giftit.dao.DaoException;
import by.chmut.giftit.dao.ItemDao;
import by.chmut.giftit.entity.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDaoImpl implements ItemDao {

    private static final String SELECT_ALL_ITEMS = "SELECT id, name, type, description, cost FROM Items";
    private static final String SELECT_ITEM_BY_ID = "SELECT id, name, type, description, cost FROM Items WHERE id = ?";
    private static final String DELETE_ITEM = "DELETE FROM Items WHERE id=?";
    private static final String CREATE_ITEM = "INSERT INTO Items(name, type, description, cost) VALUES(?,?,?,?)";
    private static final String UPDATE_ITEM = "UPDATE Items SET name=?, type=?, description=?, cost=? WHERE id=?";

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Item> findAll() throws DaoException {
        List<Item> items = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_ALL_ITEMS);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Item item = makeFromResultSet(resultSet);
                items.add(item);
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with get all items", exception);
        } finally {
            close(statement);
            close(resultSet);
        }
        return items;
    }

    @Override
    public Item findEntity(Long id) throws DaoException {
        Item item = new Item();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_ITEM_BY_ID);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                item = makeFromResultSet(resultSet);
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with get item by id", exception);
        } finally {
            close(statement);
            close(resultSet);
        }
        return item;
    }

    private Item makeFromResultSet(ResultSet resultSet) throws SQLException {
        Item item = new Item();
        item.setItemId(resultSet.getLong(1));
        item.setItemName(resultSet.getString(2));
        item.setType(resultSet.getString(3));
        item.setDescription(resultSet.getString(4));
        item.setCost(resultSet.getBigDecimal(5));
        return item;
    }

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

    @Override
    public boolean delete(Item item) throws DaoException {
        return delete(item.getItemId());
    }

    @Override
    public boolean create(Item item) throws DaoException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int result;
        try {
            statement = connection.prepareStatement(CREATE_ITEM, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, item.getItemName());
            statement.setString(2, item.getType());
            statement.setString(3, item.getDescription());
            statement.setBigDecimal(4, item.getCost());
            result = statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                item.setItemId(resultSet.getLong(1));
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with creating item", exception);
        } finally {
            close(resultSet);
            close(statement);
        }
        return result > 0;
    }

    @Override
    public Item update(Item item) throws DaoException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(UPDATE_ITEM);
            statement.setLong(5, item.getItemId());
            statement.setString(1, item.getItemName());
            statement.setString(2, item.getType());
            statement.setString(3, item.getDescription());
            statement.setBigDecimal(4, item.getCost());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DaoException("Error with update item", exception);
        } finally {
            close(statement);
        }
        return item;
    }
}
