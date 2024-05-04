package dev.eztxm.annotation;

import dev.eztxm.database.annotation.SQLDatabaseType;

public @interface SQLConnection {
    String host();
    int port() default 3306;
    String database();
    String username();
    String password();
    SQLDatabaseType type();
}
