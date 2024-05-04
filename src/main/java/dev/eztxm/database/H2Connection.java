package dev.eztxm.database;

import com.zaxxer.hikari.pool.HikariPool;
import dev.eztxm.database.util.Arguments;
import dev.eztxm.api.SQLConnection;
import dev.eztxm.database.util.SQLDatabaseConnection;
import lombok.Getter;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Getter
public class H2Connection implements SQLConnection {
    private final HikariPool pool;
    private final ExecutorService service;

    public H2Connection(String path, String fileName) {
        if (!new File(path).exists()) new File(path).mkdirs();
        SQLDatabaseConnection databaseConnection = new SQLDatabaseConnection();
        databaseConnection.create("h2", path, fileName);
        pool = databaseConnection.connect();
        service = databaseConnection.newCachedThread();
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

    @Override
    public CompletableFuture<HikariPool> getPoolAsync() {
        return CompletableFuture.supplyAsync(this::getPool, service);
    }

    private void setArguments(Object[] objects, PreparedStatement preparedStatement) throws SQLException {
        Arguments.set(objects, preparedStatement);
    }
}
