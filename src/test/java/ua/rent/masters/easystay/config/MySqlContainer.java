package ua.rent.masters.easystay.config;

import org.testcontainers.containers.MySQLContainer;

public class MySqlContainer extends MySQLContainer<MySqlContainer> {
    private static final String DB_IMAGE = "mysql:8";
    private static MySqlContainer instance;

    public MySqlContainer() {
        super(DB_IMAGE);
    }

    public static synchronized MySqlContainer getInstance() {
        if (instance == null) {
            instance = new MySqlContainer();
        }
        return instance;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("TEST_DB_URL", instance.getJdbcUrl());
        System.setProperty("TEST_DB_USER", instance.getUsername());
        System.setProperty("TEST_DB_PASSWORD", instance.getPassword());
    }
}
