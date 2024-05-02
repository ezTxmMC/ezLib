package dev.eztxm.database.util;

import com.zaxxer.hikari.pool.HikariPool;

import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;

public interface SQLConnection {
    void put(String sql, Object... objects);
    CompletableFuture<Void> putAsync(String sql, Object... objects);
    ResultSet query(String sql, Object... objects);
    CompletableFuture<ResultSet> queryAsync(String sql, Object... objects);
    void close();
    CompletableFuture<Void> closeAsync();
    CompletableFuture<HikariPool> getPoolAsync();
}
