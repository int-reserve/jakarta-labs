package edu.kpi.repository;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class JpaUtil {

    private EntityManagerFactory emf;

    @PostConstruct
    void init() {
        Map<String, Object> props = new HashMap<>();
        String host = env("DB_HOST", "localhost");
        String port = env("DB_PORT", "5432");
        String name = env("DB_NAME", "moviesdb");
        props.put("jakarta.persistence.jdbc.driver",   "org.postgresql.Driver");
        props.put("jakarta.persistence.jdbc.url",      "jdbc:postgresql://" + host + ":" + port + "/" + name);
        props.put("jakarta.persistence.jdbc.user",     env("DB_USER",     "postgres"));
        props.put("jakarta.persistence.jdbc.password", env("DB_PASSWORD", "postgres"));
        emf = Persistence.createEntityManagerFactory("jakartaLabsPU", props);
    }

    @PreDestroy
    void destroy() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    public EntityManager createEntityManager() {
        return emf.createEntityManager();
    }

    private static String env(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value != null && !value.isBlank()) ? value : defaultValue;
    }
}
