package dev.eztxm.database.util;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Arguments {

    public static void set(Object[] objects, PreparedStatement preparedStatement) throws SQLException {
        for (int i = 0; i < objects.length; i++) {
            Object object = objects[i];
            if (object instanceof String) {
                preparedStatement.setString(i + 1, (String)object);
            }
            if (object instanceof Integer) {
                preparedStatement.setInt(i + 1, (Integer) object);
            }
            if (object instanceof Date) {
                preparedStatement.setDate(i + 1, (Date)object);
            }
            if (object instanceof Timestamp) {
                preparedStatement.setTimestamp(i + 1, (Timestamp)object);
            }
            if (object instanceof Boolean) {
                preparedStatement.setBoolean(i + 1, (Boolean) object);
            }
            if (object instanceof Float) {
                preparedStatement.setFloat(i + 1, (Float) object);
            }
            if (object instanceof Double) {
                preparedStatement.setDouble(i + 1, (Double) object);
            }
            if (object instanceof Long) {
                preparedStatement.setLong(i + 1, (Long) object);
            }
        }
    }
}
