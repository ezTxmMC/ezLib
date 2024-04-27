package dev.eztxm.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.pool.HikariPool;
import dev.eztxm.database.util.Arguments;
import lombok.Getter;

import java.io.File;
import java.sql.*;
import java.util.Timer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SQLiteConnection {

    private Timer timer;
    @Getter
    private HikariPool pool;
    private final HikariConfig config;
    private final ExecutorService service;

    public SQLiteConnection(String path, String fileName) {
        if (!new File(path).exists()) new File(path).mkdirs();
        this.config = new HikariConfig();
        config.setConnectionTimeout(7500L);
        config.setMaximumPoolSize(8);
        config.setMinimumIdle(1);
        config.setJdbcUrl(String.format("jdbc:sqlite:%s/%s", path, fileName));
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
        this.pool = new HikariPool(config);
        try (Connection connection = pool.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT 1"); /* ping */
            statement.setQueryTimeout(15);
            statement.executeQuery();
            System.out.println("Successfully to connect to database.");
        } catch (SQLException e) {
            System.out.println("Can't connect to the database, check your inputs or your database:\n");
            e.fillInStackTrace();
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

    public CompletableFuture<HikariPool> getPoolAsync() {
        return CompletableFuture.supplyAsync(this::getPool, service);
    }
}