package edu.kpi.repository;

import org.flywaydb.core.Flyway;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Runs Flyway database migrations on application startup.
 *
 * Uses the same DB_* environment variables as docker-compose to build a direct
 * JDBC URL.  This avoids any dependency on the GlassFish JNDI bootstrap order
 * and works identically in Docker and in any environment where those variables
 * are set.  For local development without env vars the defaults below apply.
 */
@WebListener
public class FlywayMigrationListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String host = env("DB_HOST",     "localhost");
        String port = env("DB_PORT",     "5432");
        String name = env("DB_NAME",     "moviesdb");
        String user = env("DB_USER",     "postgres");
        String pass = env("DB_PASSWORD", "postgres");

        String url = "jdbc:postgresql://" + host + ":" + port + "/" + name;

        Flyway.configure()
                .dataSource(url, user, pass)
                .locations("classpath:db/migration")
                .load()
                .migrate();
    }

    private static String env(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value != null && !value.isBlank()) ? value : defaultValue;
    }
}
