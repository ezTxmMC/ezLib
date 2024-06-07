package de.eztxm.database;

import lombok.Getter;
import redis.clients.jedis.JedisPool;

@Getter
public class RedisConnection {
    private final JedisPool pool;

    public RedisConnection(String host, int port) {
        this.pool = new JedisPool(host, port);
    }
}
