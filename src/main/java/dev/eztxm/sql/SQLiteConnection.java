package dev.eztxm.sql;

import java.io.File;
import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;

public class SQLiteConnection {
    private Connection connection;
    private Timer timer;
    private final String path;
    private final String fileName;

    public SQLiteConnection(String path, String fileName) {
        this.path = path;
        this.fileName = fileName;
        if (!new File(path).exists()) new File(path).mkdirs();
        connect();
        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                checkConnection();
            }
        }, 10000L, 5000L);
    }

    private void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + path + "/" + fileName);
            System.out.println("Successfully to connect to database.");
        } catch (SQLException e) {
            System.out.println("Failed to connect to database.");
        }
    }

    public void createTable(String tableName, String columns) {
        put("CREATE TABLE IF NOT EXISTS " + tableName + " (" + columns + ")");
    }

    public void update(String tableName, String column, Object value, String condition) {
        put("UPDATE " + tableName + " SET " + column + " = " + value + " WHERE " + condition);
    }

    public void insertInto(String tableName, String values) {
        put("INSERT INTO " + tableName + " VALUES (" + values + ")");
    }

    public void deleteFrom(String tableName, String condition) {
        put("DELETE FROM " + tableName + " WHERE " + condition);
    }

    public ResultSet select(String tableName, String columns, String condition, boolean needsCondition) {
        return query("SELECT " + columns + " FROM " + tableName + (needsCondition ? " WHERE " + condition : ""));
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkConnection() {
        if (connection == null) {
            System.out.println("Connection lost. Reconnecting...");
            reconnect();
            return;
        }
        try {
            if (!connection.isValid(1)) {
                System.out.println("Connection lost. Reconnecting...");
                reconnect();
            }
        } catch (SQLException e) {
            System.out.println("Connection lost. Reconnecting...");
            reconnect();
        }
    }

    private void reconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            connection = null;
        }
        connect();
    }

    public void stopChecking() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Connection checking stopped.");
    }

    public ResultSet query(String sql, Object... objects) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            setArguments(objects, preparedStatement);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void put(String sql, Object... objects) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            setArguments(objects, preparedStatement);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setArguments(Object[] objects, PreparedStatement preparedStatement) throws SQLException {
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

    public Connection getConnection() {
        return connection;
    }
}
