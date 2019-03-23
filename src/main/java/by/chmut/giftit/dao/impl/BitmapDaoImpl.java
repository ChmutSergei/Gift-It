package by.chmut.giftit.dao.impl;

import by.chmut.giftit.dao.BitmapDao;
import by.chmut.giftit.dao.DaoException;
import by.chmut.giftit.entity.Bitmap;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The Bitmap dao class provides that bitmap is loaded from the database
 * and changes are saved when adding a new item.
 * A number of CRUD Dao interface methods are prohibited..
 *
 * @author Sergei Chmut.
 */
public class BitmapDaoImpl implements BitmapDao {

    /**
     * The constant SQL query SELECT_BITMAP.
     */
    private static final String SELECT_BITMAP = "SELECT criteria_key, data FROM Bitmaps";
    /**
     * The constant SQL query UPDATE_BITMAP.
     */
    private static final String UPDATE_BITMAP = "UPDATE Bitmaps SET data=? WHERE criteria_key=?";

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
     * Find all bitmap in database.
     *
     * @return the list of bitmap
     * @throws DaoException if find all can't be handled
     */
    @Override
    public List<Bitmap> findAll() throws DaoException {
        List<Bitmap> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_BITMAP);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Bitmap bitmap = makeFromResultSet(resultSet);
                result.add(bitmap);
            }
        } catch (SQLException exception) {
            throw new DaoException("Error with find all bitmaps", exception);
        } finally {
            close(resultSet);
            close(statement);
        }
        return result;
    }

    /**
     * Update bitmap with new data.
     *
     * @param bitmap new bitmap
     * @return stored bitmap
     * @throws DaoException if update can't be handled
     */
    @Override
    public Bitmap update(Bitmap bitmap) throws DaoException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(UPDATE_BITMAP);
            statement.setString(2, bitmap.getName());
            String json = new Gson().toJson(bitmap.getData());
            statement.setBytes(1, json.getBytes());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DaoException("Error with update Bitmap", exception);
        } finally {
            close(statement);
        }
        return null;
    }


    /**
     * Make bitmap from result set.
     *
     * @param resultSet the ResultSet
     * @return the bitmap
     * @throws SQLException if ResultSet can't be handled
     */
    private Bitmap makeFromResultSet(ResultSet resultSet) throws SQLException {
        Bitmap bitmap = new Bitmap();
        bitmap.setName(resultSet.getString(1));
        String json = new String(resultSet.getBytes(2));
        Gson gson = new Gson();
        bitmap.setData(gson.fromJson(json, int[].class));
        return bitmap;
    }

    /**
     * Find entity optional unsupported operation.
     *
     * @param id the id
     * @return the optional
     * @throws DaoException if you try to use this method
     */
    @Override
    public Optional<Bitmap> findEntity(Integer id) throws DaoException {
        throw new DaoException("Unsupported operation exception!");
    }

    /**
     * Delete by id unsupported operation.
     *
     * @param id the id
     * @return the boolean
     * @throws DaoException if you try to use this method
     */
    @Override
    public boolean delete(Integer id) throws DaoException {
        throw new DaoException("Unsupported operation exception!");
    }

    /**
     * Delete unsupported operation.
     *
     * @param entity the entity
     * @return the boolean
     * @throws DaoException if you try to use this method
     */
    @Override
    public boolean delete(Bitmap entity) throws DaoException {
        throw new DaoException("Unsupported operation exception!");
    }

    /**
     * Create bitmap unsupported operation.
     *
     * @param entity the entity
     * @return the bitmap
     * @throws DaoException if you try to use this method
     */
    @Override
    public Bitmap create(Bitmap entity) throws DaoException {
        throw new DaoException("Unsupported operation exception!");
    }

}
