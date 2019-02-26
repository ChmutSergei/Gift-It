package by.chmut.giftit.dao.impl;

import by.chmut.giftit.dao.DaoException;
import by.chmut.giftit.dao.ItemDao;
import by.chmut.giftit.entity.Item;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDaoImpl implements ItemDao {

    private static final String SELECT_ITEMS_TYPE = "SELECT id, name, type, description, active, cost FROM Items WHERE type = ?";
    private static final String SELECT_ITEMS_PRICE = "SELECT id, name, type, description, active, cost FROM Items WHERE price = ?";
    private static final String SELECT_ITEMS_TYPE_AND_PRICE = "SELECT id, name, type, description, active, cost FROM " +
            "Items WHERE type = ? AND price = ?";
    private static final String SELECT_ITEM_BY_ID = "SELECT id, name, type, description, active, cost, image FROM Items WHERE id = ?";
    private static final String DELETE_ITEM = "DELETE FROM Items WHERE id=?";
    private static final String CREATE_ITEM = "INSERT INTO Items(name, type, description, active, cost, image) VALUES(?,?,?,?,?,?)";
    private static final String UPDATE_ITEM = "UPDATE Items SET name=?, type=?, description=?, active=?, cost=?, image=? WHERE id=?";
    private static final String SELECT_ALL_ITEMS = "SELECT id, name, type, description, active, cost, image FROM Items " +
            "ORDER BY id DESC LIMIT ? OFFSET ?";

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Item> findAll(String filePath, int limit, int offset) throws DaoException {
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

    @Override
    public Item findEntity(Long id, String filePath) throws DaoException {
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
        return item;
    }

    private Item makeFromResultSet(ResultSet resultSet, String filePath) throws SQLException, IOException {
        Item item = new Item();
        item.setItemId(resultSet.getLong(1));
        item.setItemName(resultSet.getString(2));
        item.setType(resultSet.getString(3));
        item.setDescription(resultSet.getString(4));
        item.setActive(resultSet.getBoolean(5));
        item.setCost(resultSet.getBigDecimal(6));
        byte[] imageBlob = resultSet.getBytes(7);
        File image = new File(filePath + item.getItemId() + ".jpg");
        FileOutputStream fileOutputStream = new FileOutputStream(image);
        fileOutputStream.write(imageBlob);
        fileOutputStream.close();
        item.setImage(image);
        return item;
    }

    @Override
    public List<Item> findAll() throws DaoException {

        return null;
    }

    @Override
    public Item findEntity(Long id) throws DaoException {
        return null;
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
        File image = item.getImage();
        try (FileInputStream fis = new FileInputStream(image)) {
            statement = connection.prepareStatement(CREATE_ITEM, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, item.getItemName());
            statement.setString(2, item.getType());
            statement.setString(3, item.getDescription());
            statement.setBoolean(4, item.isActive());
            statement.setBigDecimal(5, item.getCost());
            statement.setBinaryStream(6, fis, (int) image.length());
            result = statement.executeUpdate();
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
        return result > 0;
    }

    @Override
    public Item update(Item item) throws DaoException {
        PreparedStatement statement = null;
        File image = item.getImage();
        try (FileInputStream fis = new FileInputStream(image)) {
            statement = connection.prepareStatement(UPDATE_ITEM);
            statement.setLong(7, item.getItemId());
            statement.setString(1, item.getItemName());
            statement.setString(2, item.getType());
            statement.setString(3, item.getDescription());
            statement.setBoolean(4, item.isActive());
            statement.setBigDecimal(5, item.getCost());
            statement.setBinaryStream(6, fis, (int) image.length());
            statement.executeUpdate();
        } catch (IOException | SQLException exception) {
            throw new DaoException("Error with update item", exception);
        } finally {
            close(statement);
        }
        return item;
    }
}
