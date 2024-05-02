package dev.eztxm.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.pool.HikariPool;
import dev.eztxm.database.util.Arguments;
import dev.eztxm.database.util.SQLConnection;
import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class MariaDBConnection implements SQLConnection {
    @Getter
    private HikariPool pool;
    private final HikariConfig config;
    private final ExecutorService service;

    public MariaDBConnection(String host, int port, String database, String username, String password) {

        this.config = new HikariConfig();
        config.setConnectionTimeout(7500L);
        config.setMaximumPoolSize(8);
        config.setMinimumIdle(1);
        config.setJdbcUrl(String.format("jdbc:mariadb://%s:%s/%s?autoReconnect=true", host, port, database));
        config.setUsername(username);
        config.setPassword(password);
        connect();

        this.service = Executors.newCachedThreadPool();
    }

    @Override
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

    @Override
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

    @Override
    public CompletableFuture<ResultSet> queryAsync(String sql, Object... objects) {
        return CompletableFuture.supplyAsync(() -> query(sql, objects), service);
    }

    @Override
    public CompletableFuture<Void> putAsync(String sql, Object... objects) {
        return CompletableFuture.runAsync(() -> put(sql, objects), service);
    }

    private void connect() {
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

    @Override
    public void close() {
        try {
            pool.shutdown();
        } catch (InterruptedException e) {
            e.fillInStackTrace();
        }
    }

    @Override
    public CompletableFuture<Void> closeAsync() {
        return CompletableFuture.runAsync(this::close, service);
    }

    private void setArguments(Object[] objects, PreparedStatement preparedStatement) throws SQLException {
        Arguments.set(objects, preparedStatement);
    }

    @Override
    public CompletableFuture<HikariPool> getPoolAsync() {
        return CompletableFuture.supplyAsync(this::getPool, service);
    }
}