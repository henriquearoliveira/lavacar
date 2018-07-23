package com.hrsoftware.dao;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.hrsoftware.business.dao.StatusException;
import com.hrsoftware.utilitario.Ferramentas;

public class DAO<T> {

	private Class<T> objectClass;

	private StatusException statusException;

	public StatusException getStatusException() {
		return statusException;
	}

	protected DAO(Class<T> objectClass) {
		this.objectClass = objectClass;
	}

	protected T atualiza(T objeto) {

		EntityManager em = JpaUtil.getInstance().getEntityManager();

		try {

			em.getTransaction().begin();
			em.merge(objeto);
			em.getTransaction().commit();

		} catch (Exception ex) {

			System.err.println("DAO ATUALIZA: " + objeto.toString());
			ex.printStackTrace();
			closeOperations(em);

			objeto = null;

			return objeto;

		}

		closeOperations(em);

		return objeto;
	}

	protected T insereObjeto(T objeto) {

		EntityManager em = JpaUtil.getInstance().getEntityManager();

		try {

			em.getTransaction().begin();
			em.persist(objeto);
			em.getTransaction().commit();

		} catch (Exception ex) {

			System.err.println("DAO INSERE: " + objeto.toString());
			ex.printStackTrace();
			closeOperations(em);

			return null;

		}

		// CLOSE OPERATIONS
		closeOperations(em);

		return objeto;
	}

	protected T getByID(Long id) {

		EntityManager em = JpaUtil.getInstance().getEntityManager();

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(objectClass);

		Root<T> c = cq.from(objectClass);

		cq.select(c);
		cq.where(cb.equal(c.get("id"), id));

		TypedQuery<T> query = em.createQuery(cq);

		T objeto = null;

		try {
			objeto = query.getSingleResult();
		} catch (Exception e) {

			System.err.println("DAO GET BY ID ");
			e.printStackTrace();
			closeOperations(em);

			return null;
		}

		closeOperations(em);

		return objeto;

	}

	protected T getBy(String sqlConsulta, Map<String, Object> parametros) {

		EntityManager em = JpaUtil.getInstance().getEntityManager();

		TypedQuery<T> query = em.createQuery(sqlConsulta, objectClass);

		if (parametros != null) {

			parametros.entrySet().forEach(p -> {
				query.setParameter(p.getKey(), p.getValue());
			});

		}

		T objeto = null;

		try {

			objeto = query.getSingleResult();

		} catch (Exception e) {
			
			if (!e.getMessage().equals("No entity found for query"))
				e.printStackTrace();
			closeOperations(em);

			return null;
		}

		closeOperations(em);

		return objeto;
	}

	public LocalDateTime getDateTime() {

		EntityManager em = JpaUtil.getInstance().getEntityManager();

		Query query = em.createNativeQuery("SELECT NOW()");

		Date objeto = null;

		try {

			objeto = (Date) query.getSingleResult();

		} catch (Exception e) {

			System.err.println("DAO GET JPQL: ");
			e.printStackTrace();
			closeOperations(em);

			return null;
		}

		closeOperations(em);

		return new Ferramentas().dateToLocalDateTime(objeto);
	}

	/**
	 * @return ABSTRAI O FECHAMENTO DE CONEXÕES USADO PARA CONSULTAS PERSONALIZADAS
	 */
	protected List<T> seleciona(String sqlConsulta, Map<String, Object> parametros) {

		EntityManager em = JpaUtil.getInstance().getEntityManager();

		TypedQuery<T> query = em.createQuery(sqlConsulta, objectClass);

		parametros.entrySet().forEach(p -> {
			query.setParameter(p.getKey(), p.getValue());
		});

		List<T> lista = null;

		try {

			lista = query.getResultList();

		} catch (Exception e) {

			System.err.println("DAO SELECIONA JPQL: ");
			e.printStackTrace();
			closeOperations(em);

			return null;
		}

		closeOperations(em);

		return lista;
	}

	protected List<T> selecionaCompleto() {

		EntityManager em = JpaUtil.getInstance().getEntityManager();

		CriteriaQuery<T> cq = em.getCriteriaBuilder().createQuery(objectClass);
		Root<T> c = cq.from(objectClass);
		cq.select(c);
		TypedQuery<T> query = em.createQuery(cq);

		List<T> lista = null;

		try {

			lista = query.getResultList();

		} catch (Exception e) {

			System.err.println("DAO SELECIONA COMPLETO: ");
			e.printStackTrace();
			closeOperations(em);

			return null;
		}

		closeOperations(em);

		return lista;
	}

	protected List<T> selecionaPela(String descricao) {

		EntityManager em = JpaUtil.getInstance().getEntityManager();

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(objectClass);

		Root<T> c = cq.from(objectClass);
		cq.select(c);
		cq.where(cb.like(c.get("nome"), descricao));
		TypedQuery<T> query = em.createQuery(cq);

		List<T> lista = null;

		try {

			lista = query.getResultList();

		} catch (Exception e) {

			System.err.println("DAO SELECIONA PELA DESCRIÇÃO: ");
			e.printStackTrace();
			closeOperations(em);

			return null;
		}

		closeOperations(em);

		return lista;

	}

	protected int updateQuery(String sqlUpdate, Map<String, Object> parametros) {

		EntityManager em = JpaUtil.getInstance().getEntityManager();
		int quantidadeUpdates = 0;

		Query query = em.createQuery(sqlUpdate);

		parametros.entrySet().forEach(p -> {
			query.setParameter(p.getKey(), p.getValue());
		});

		try {

			quantidadeUpdates = query.executeUpdate();

		} catch (Exception e) {

			System.err.println("DAO ATUALIZA PELA QUERY");
			e.printStackTrace();
			closeOperations(em);
			return 0;
		}

		// CLOSE OPERATIONS
		closeOperations(em);

		return quantidadeUpdates;

	}

	protected boolean removeObjeto(Long id) {

		EntityManager em = JpaUtil.getInstance().getEntityManager();

		try {

			T object = em.find(objectClass, id);
			em.getTransaction().begin();
			em.remove(object);
			em.getTransaction().commit();

		} catch (PersistenceException ex) {

			System.err.println("DAO REMOVE: " + id);
			ex.printStackTrace();
			closeOperations(em);

			this.statusException = StatusException.DEPENDENCIA;

			return false;

		} catch (Exception ex) {

			System.err.println("DAO REMOVE: " + id);
			ex.printStackTrace();
			closeOperations(em);
			return false;

		}

		// CLOSE OPERATIONS
		closeOperations(em);

		return true;
	}

	private void closeOperations(EntityManager em) {
		em.close();
	}

}
