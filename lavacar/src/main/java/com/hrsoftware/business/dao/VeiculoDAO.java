package com.hrsoftware.business.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hrsoftware.business.BusinessClient;
import com.hrsoftware.business.Cliente;
import com.hrsoftware.business.Veiculo;
import com.hrsoftware.dao.DaoAbstrato;

public class VeiculoDAO extends DaoAbstrato<Veiculo> {

	private final String VEICULO_PELO_BUSINESS = "SELECT v FROM Veiculo v WHERE v.cliente.businessClient.id = :idClienteBusiness";

	private final String VEICULO_PELO_CLIENTE = "SELECT v FROM Veiculo v WHERE v.cliente.id = :idCliente";

	public VeiculoDAO() {
		super(Veiculo.class);
	}

	public List<Veiculo> seleciona(BusinessClient businessClient) {

		Map<String, Object> parametros = new LinkedHashMap<>();
		parametros.put("idClienteBusiness", businessClient.getId());

		return super.seleciona(VEICULO_PELO_BUSINESS, parametros);
	}

	public List<Veiculo> seleciona(Cliente cliente) {

		Map<String, Object> parametros = new LinkedHashMap<>();
		parametros.put("idCliente", cliente.getId());

		return super.seleciona(VEICULO_PELO_CLIENTE, parametros);
	}

}
