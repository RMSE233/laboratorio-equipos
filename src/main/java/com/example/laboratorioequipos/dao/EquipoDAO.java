package com.example.laboratorioequipos.dao;

import com.example.laboratorioequipos.database.JPAUtil;
import com.example.laboratorioequipos.model.Equipo;
import com.example.laboratorioequipos.model.TipoEquipo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class EquipoDAO {

    public void guardar(Equipo equipo) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            TipoEquipo tipoManaged = em.getReference(TipoEquipo.class, equipo.getTipoEquipo().getIdTipo());
            equipo.setTipoEquipo(tipoManaged);
            em.persist(equipo);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void actualizar(Equipo equipo) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(equipo);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void eliminar(Integer idEquipo) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Equipo equipo = em.find(Equipo.class, idEquipo);
            if (equipo != null) {
                em.remove(equipo);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Equipo> buscarConFiltros(String nombreParcial, TipoEquipo tipoFiltro) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            StringBuilder jpql = new StringBuilder(
                    "SELECT e FROM Equipo e JOIN FETCH e.tipoEquipo WHERE 1=1"
            );

            if (nombreParcial != null && !nombreParcial.isBlank()) {
                jpql.append(" AND LOWER(e.nombre) LIKE LOWER(:nombre)");
            }
            if (tipoFiltro != null) {
                jpql.append(" AND e.tipoEquipo.idTipo = :idTipo");
            }
            jpql.append(" ORDER BY e.idEquipo ASC");

            TypedQuery<Equipo> query = em.createQuery(jpql.toString(), Equipo.class);

            if (nombreParcial != null && !nombreParcial.isBlank()) {
                query.setParameter("nombre", "%" + nombreParcial.trim() + "%");
            }
            if (tipoFiltro != null) {
                query.setParameter("idTipo", tipoFiltro.getIdTipo());
            }

            return query.getResultList();
        } finally {
            em.close();
        }
    }
}