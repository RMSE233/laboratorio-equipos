package com.example.laboratorioequipos.dao;

import com.example.laboratorioequipos.database.JPAUtil;
import com.example.laboratorioequipos.model.TipoEquipo;
import jakarta.persistence.EntityManager;
import java.util.List;

public class TipoEquipoDAO {
    public List<TipoEquipo> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT t FROM TipoEquipo t ORDER BY t.nombre ASC", TipoEquipo.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}