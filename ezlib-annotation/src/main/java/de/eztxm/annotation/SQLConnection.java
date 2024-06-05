package de.eztxm.annotation;

import de.eztxm.annotation.database.SQLDatabaseType;

public @interface SQLConnection {
    String host();
    int port() default 3306;
    String database();
    String username();
    String password();
    SQLDatabaseType type();
}
