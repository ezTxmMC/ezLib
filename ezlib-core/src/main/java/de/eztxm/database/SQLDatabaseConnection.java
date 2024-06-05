package de.eztxm.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.pool.HikariPool;
import de.eztxm.logger.SimpleLogger;
import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class SQLDatabaseConnection {
    private HikariPool pool;
    private HikariConfig config;

    public SQLDatabaseConnection create(String jdbcDriver, String host, int port, String database, String username, String password) {
        this.config = new HikariConfig();
        config.setConnectionTimeout(7500L);
        config.setMaximumPoolSize(8);
        config.setMinimumIdle(1);
        config.setJdbcUrl(String.format("jdbc:" + jdbcDriver + "://%s:%s/%s?autoReconnect=true", host, port, database));
        config.setUsername(username);
        config.setPassword(password);
        return this;
    }

    public SQLDatabaseConnection create(String jdbcDriver, String path, String file) {
        this.config = new HikariConfig();
        config.setConnectionTimeout(7500L);
        config.setMaximumPoolSize(8);
        config.setMinimumIdle(1);
        config.setJdbcUrl(String.format("jdbc:" + jdbcDriver + ":%s/%s", System.getProperty("user.dir") + "/" + path, file));
        return this;
    }

    public ExecutorService newCachedThread() {
        return Executors.newCachedThreadPool();
    }

    public HikariPool connect() {
        pool = new HikariPool(config);
        try (Connection connection = pool.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT 1");
            statement.setQueryTimeout(15);
            statement.executeQuery();
            new SimpleLogger("ezLib").info("Successfully to connect to database.");
        } catch (SQLException e) {
            new SimpleLogger("ezLib").error("Can't connect to the database, check your inputs or your database:\n" + e.getMessage());
        }
        return pool;
    }
}
