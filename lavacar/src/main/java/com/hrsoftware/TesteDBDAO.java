package com.hrsoftware;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hrsoftware.dao.DaoAbstrato;

public class TesteDBDAO extends DaoAbstrato<TesteBD> {

	private static final String PELOS_NUMERO = "SELECT t FROM TesteBD t WHERE t.numero = :num1 OR t.numero = :num2";

	public TesteDBDAO() {
		super(TesteBD.class);
	}

	/**
	 * @param num1
	 * @param num2
	 * @return SELECIONA PARAMETRIZAVEL
	 * FACILITA A BUSCA SEM O CRITERIA.
	 * USO O CRITERIA NAS OCASIÕES ONDE A ABSTRAÇÃO FACILITA.
	 */
	public List<TesteBD> selecionaPeloNumero(int num1, int num2) {
		
		Map<String, Object> parametros = new LinkedHashMap<>();
		parametros.put("num1", num1);
		parametros.put("num2", num2);

		return seleciona(PELOS_NUMERO, parametros);
	}

}
