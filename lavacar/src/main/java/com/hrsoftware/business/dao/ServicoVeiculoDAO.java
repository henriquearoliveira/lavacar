package com.hrsoftware.business.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hrsoftware.business.BusinessClient;
import com.hrsoftware.business.FluxoDeCaixa;
import com.hrsoftware.business.ServicoVeiculo;
import com.hrsoftware.business.StatusDoServico;
import com.hrsoftware.dao.DaoAbstrato;

public class ServicoVeiculoDAO extends DaoAbstrato<ServicoVeiculo> {

	public final String BY_BUSINESS_CLIENT = "SELECT sv FROM ServicoVeiculo sv WHERE sv.servico.businessClient.id = :idBusinessClient ORDER BY sv.dataHoraCriacao ASC";

	public final String BY_DATE_TODAY = "SELECT sv FROM ServicoVeiculo sv WHERE sv.servico.businessClient.id = :idBusinessClient"
			+ " AND sv.statusDoServico = :statusDoServico ORDER BY sv.dataHoraCriacao ASC"; // DATE(sv.dataHoraCriacao)
																							// = DATE(:dataHoraCriacao)
																							// AND

	public final String BY_FLUXO_DE_CAIXA = "SELECT sv FROM ServicoVeiculo sv WHERE sv.fluxoDeCaixa.id = :idFluxoDeCaixa"
			+ " AND sv.statusDoServico = :statusDoServico ORDER BY sv.dataHoraCriacao ASC";

	public ServicoVeiculoDAO() {
		super(ServicoVeiculo.class);
	}

	public List<ServicoVeiculo> seleciona(BusinessClient businessClient) {

		Map<String, Object> parametros = new LinkedHashMap<>();
		parametros.put("idBusinessClient", businessClient.getId());

		return super.seleciona(BY_BUSINESS_CLIENT, parametros);

	}

	public List<ServicoVeiculo> selecionaAbertos(BusinessClient businessClient) {

		Map<String, Object> parametros = new LinkedHashMap<>();
		parametros.put("idBusinessClient", businessClient.getId());
		parametros.put("statusDoServico", StatusDoServico.ABERTO);

		return super.seleciona(BY_DATE_TODAY, parametros);
	}

	public List<ServicoVeiculo> selecionaFechados(FluxoDeCaixa fluxoDeCaixa) {

		Map<String, Object> parametros = new LinkedHashMap<>();
		parametros.put("idFluxoDeCaixa", fluxoDeCaixa.getId());
		parametros.put("statusDoServico", StatusDoServico.TERMINADO);

		return super.seleciona(BY_FLUXO_DE_CAIXA, parametros);

	}

}
