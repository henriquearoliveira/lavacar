package com.hrsoftware.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaUtil {

	private static JpaUtil instance = null;
	
	private EntityManagerFactory emf = null;
	
    private static final String DATA_BASE_NAME = "lavacar";

	private JpaUtil() {
	}

	/**
	 * @return
	 */
	public static JpaUtil getInstance() {

		if (instance == null)
			instance = new JpaUtil();

		return instance;
	}

	/**
	 * @return
	 */
	public EntityManager getEntityManager() {

		emf = Persistence.createEntityManagerFactory(DATA_BASE_NAME);

		return emf.createEntityManager();

	}
	
	public EntityManagerFactory getEntityManagerFactory() {
		return emf;
	}

}
