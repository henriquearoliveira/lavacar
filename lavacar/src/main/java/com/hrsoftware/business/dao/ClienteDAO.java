package com.hrsoftware.business.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hrsoftware.business.BusinessClient;
import com.hrsoftware.business.Cliente;
import com.hrsoftware.dao.DaoAbstrato;

public class ClienteDAO extends DaoAbstrato<Cliente> {
	
	public final String BY_BUSINESS_CLIENT =
			"SELECT c FROM Cliente c WHERE c.businessClient.id = :idBusinessClient ORDER BY c.nome ASC";

	public ClienteDAO() {
		super(Cliente.class);
	}
	
	public List<Cliente> seleciona(BusinessClient businessClient) {
		
		Map<String, Object> parametros = new LinkedHashMap<>();
		parametros.put("idBusinessClient", businessClient.getId());
		
		return super.seleciona(BY_BUSINESS_CLIENT, parametros);
		
	}

}
