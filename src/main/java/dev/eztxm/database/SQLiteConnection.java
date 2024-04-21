package dev.eztxm.database;

import dev.eztxm.database.util.Arguments;

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
            e.fillInStackTrace();
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
            e.fillInStackTrace();
        }
        System.out.println("Connection checking stopped.");
    }

    public ResultSet query(String sql, Object... objects) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            setArguments(objects, preparedStatement);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.fillInStackTrace();
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
            e.fillInStackTrace();
        }
    }

    private void setArguments(Object[] objects, PreparedStatement preparedStatement) throws SQLException {
        Arguments.set(objects, preparedStatement);
    }

    public Connection getConnection() {
        return connection;
    }
}