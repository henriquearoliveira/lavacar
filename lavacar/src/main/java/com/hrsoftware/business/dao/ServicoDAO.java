package com.hrsoftware.business.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hrsoftware.business.BusinessClient;
import com.hrsoftware.business.Servico;
import com.hrsoftware.dao.DaoAbstrato;

public class ServicoDAO extends DaoAbstrato<Servico> {
	
	public final String BY_BUSINESS_CLIENT =
			"SELECT s FROM Servico s WHERE s.businessClient.id = :idBusinessClient";

	public ServicoDAO() {
		super(Servico.class);
	}

	public List<Servico> seleciona(BusinessClient businessClient) {

		Map<String, Object> parametros = new LinkedHashMap<>();
		parametros.put("idBusinessClient", businessClient.getId());

		return super.seleciona(BY_BUSINESS_CLIENT, parametros);

	}

}
