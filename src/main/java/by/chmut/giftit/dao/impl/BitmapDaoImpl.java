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

import static by.chmut.giftit.constant.AttributeName.ALL_PARAMETER_NAME;

public class BitmapDaoImpl implements BitmapDao {

    private static final String SELECT_BITMAP = "SELECT criteria_key, data FROM Bitmaps";
    private static final String UPDATE_BITMAP = "UPDATE Bitmaps SET data=? WHERE criteria_key=?";

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Bitmap> findAll() throws DaoException {
        List<Bitmap> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_BITMAP);
            resultSet = statement.executeQuery();
            int sizeArray = 0;
            while (resultSet.next()) {
                Bitmap bitmap = makeFromResultSet(resultSet);
                result.add(bitmap);
                sizeArray = bitmap.getData().length;
            }
            Bitmap bitmapAllCriteria = createBitmapAllCriteria(sizeArray);
            result.add(bitmapAllCriteria);
        } catch (SQLException exception) {
            throw new DaoException("Error with find all bitmaps", exception);
        } finally {
            close(resultSet);
            close(statement);
        }
        return result;
    }

    private Bitmap createBitmapAllCriteria(int sizeArray) {
        int[] data = new int[sizeArray];
        for (int i = 0; i < sizeArray; i++) {
            data[i] = 1;
        }
        Bitmap bitmapAllCriteria = new Bitmap();
        bitmapAllCriteria.setName(ALL_PARAMETER_NAME);
        bitmapAllCriteria.setData(data);
        return bitmapAllCriteria;
    }

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


    private Bitmap makeFromResultSet(ResultSet resultSet) throws SQLException {
        Bitmap bitmap = new Bitmap();
        bitmap.setName(resultSet.getString(1));
        String json = new String(resultSet.getBytes(2));
        Gson gson = new Gson();
        bitmap.setData(gson.fromJson(json, int[].class));
        return bitmap;
    }

    @Override
    public Bitmap findEntity(Integer id) throws DaoException {
        throw new DaoException("Not supported operation exception!");
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        throw new DaoException("Not supported operation exception!");
    }

    @Override
    public boolean delete(Bitmap entity) throws DaoException {
        throw new DaoException("Not supported operation exception!");
    }

    @Override
    public Bitmap create(Bitmap entity) throws DaoException {
        throw new DaoException("Not supported operation exception!");
    }

}
