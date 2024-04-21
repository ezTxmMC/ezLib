package dev.eztxm.database;

import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;

public class MariaDBConnection {
    private Connection connection;
    private Timer timer;
    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;

    public MariaDBConnection(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        connect();
        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                checkConnection();
            }
        }, 10000L, 5000L);
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
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

    }

    public Connection getConnection() {
        return connection;
    }
}