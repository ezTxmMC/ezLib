package dev.eztxm.api;

import com.zaxxer.hikari.pool.HikariPool;

import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;

public interface SQLConnection {

    /**
     * Executes SQL queries that update entries, such as INSERT, UPDATE, and CREATE.
     *
     * @param sql     - Place your query.
     * @param objects - When dealing with variables such as a UUID that you prefer not to directly insert into the query,
     *                  you can employ "?" as a placeholder.
     *                  Arrange your variables in the same order as the question marks within the query.
     *                  This ensures smoother integration of your dynamic values into the query without compromising its integrity or security.
     */
    void put(String sql, Object... objects);

    /**
     * Asynchronously executes SQL queries that update entries, such as INSERT, UPDATE, and CREATE.
     *
     * @param sql     The SQL query to execute.
     * @param objects When dealing with variables such as a UUID that you prefer not to directly insert into the query,
     *                you can employ "?" as a placeholder.
     *                Arrange your variables in the same order as the question marks within the query.
     *                This ensures smoother integration of your dynamic values into the query without compromising its integrity or security.
     */
    CompletableFuture<Void> putAsync(String sql, Object... objects);

    /**
     * Executes SQL queries that retrieve data, such as SELECT statements.
     *
     * @param sql     The SQL query to execute.
     * @param objects When dealing with variables such as a UUID that you prefer not to directly insert into the query,
     *                you can employ "?" as a placeholder.
     *                Arrange your variables in the same order as the question marks within the query.
     *                This ensures smoother integration of your dynamic values into the query without compromising its integrity or security.
     */
    ResultSet query(String sql, Object... objects);

    /**
     * Asynchronously executes SQL queries that retrieve data, such as SELECT statements.
     *
     * @param sql     The SQL query to execute.
     * @param objects When dealing with variables such as a UUID that you prefer not to directly insert into the query,
     *                you can employ "?" as a placeholder.
     *                Arrange your variables in the same order as the question marks within the query.
     *                This ensures smoother integration of your dynamic values into the query without compromising its integrity or security.
     */
    CompletableFuture<ResultSet> queryAsync(String sql, Object... objects);

    /**
     * Closes the sql connection.
     */
    void close();


    /**
     * Asynchronously closes the sql connection.
     */
    CompletableFuture<Void> closeAsync();

    /**
     * Asynchronously gets HikariPool.
     */
    CompletableFuture<HikariPool> getPoolAsync();
}
