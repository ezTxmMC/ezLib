package de.eztxm.ezlib.database;

import de.eztxm.ezlib.object.ObjectConverter;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Arguments {

    public static void set(Object[] objects, PreparedStatement preparedStatement) throws SQLException {
        for (int i = 0; i < objects.length; i++) {
            Object object = objects[i];
            ObjectConverter converter = new ObjectConverter(object);
            if (object instanceof String) {
                preparedStatement.setString(i + 1, converter.asString());
                continue;
            }
            if (object instanceof Integer) {
                preparedStatement.setInt(i + 1, converter.asInteger());
                continue;
            }
            if (object instanceof Date) {
                preparedStatement.setDate(i + 1, (Date) object);
                continue;
            }
            if (object instanceof Timestamp) {
                preparedStatement.setTimestamp(i + 1, (Timestamp) object);
                continue;
            }
            if (object instanceof Boolean) {
                preparedStatement.setBoolean(i + 1, converter.asBoolean());
                continue;
            }
            if (object instanceof Float) {
                preparedStatement.setFloat(i + 1, converter.asFloat());
                continue;
            }
            if (object instanceof Double) {
                preparedStatement.setDouble(i + 1, converter.asDouble());
                continue;
            }
            if (object instanceof Long) {
                preparedStatement.setLong(i + 1, converter.asLong());
                continue;
            }
            System.out.println("Error while converting object " + object + " to type " + object.getClass());
        }
    }
}
