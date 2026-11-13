package com.example.laboratorioequipos.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public class JPAUtil {
    private static final String PERSISTENCE_UNIT_NAME = "LaboratorioPU";
    private static EntityManagerFactory emf;

    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        if (emf == null || !emf.isOpen()) {
            Map<String, Object> propiedades = new HashMap<>();


            String password = System.getenv("BD_PASSWORD");
            if (password != null && !password.isBlank()) {
                propiedades.put("jakarta.persistence.jdbc.password", password);
            } else {
                System.err.println("Aviso: la variable de entorno BD_PASSWORD no está definida.");
            }

            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, propiedades);
        }
        return emf;
    }

    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    public static synchronized void shutdown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
        emf = null;
    }
}