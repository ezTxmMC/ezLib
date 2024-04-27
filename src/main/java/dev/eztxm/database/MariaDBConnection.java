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

        this.service = Executors.newCachedThreadPool();
    }

    public ResultSet query(String sql, Object... objects) {
        try (Connection connection = pool.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            setArguments(objects, preparedStatement);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return null;
    }

    public void put(String sql, Object... objects) {
        try (Connection connection = pool.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            setArguments(objects, preparedStatement);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
    }

    public CompletableFuture<ResultSet> queryAsync(String sql, Object... objects) {
        return CompletableFuture.supplyAsync(() -> query(sql, objects), service);
    }

    public CompletableFuture<Void> putAsync(String sql, Object... objects) {
        return CompletableFuture.runAsync(() -> put(sql, objects), service);
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
            System.out.println("Successfully to connect to database.");
        } catch (SQLException e) {
            System.out.println("Failed to connect to database.");
        }
    }

    public void close() {
        try {
            pool.shutdown();
        } catch (InterruptedException e) {
            e.fillInStackTrace();
        }
    }

    public CompletableFuture<Void> closeAsync() {
        return CompletableFuture.runAsync(this::close, service);
    }

    private void setArguments(Object[] objects, PreparedStatement preparedStatement) throws SQLException {
        Arguments.set(objects, preparedStatement);
    }

    public Connection getConnection() {
        return connection;
    }
}