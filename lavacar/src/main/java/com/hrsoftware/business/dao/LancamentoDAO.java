package com.hrsoftware.business.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hrsoftware.business.FluxoDeCaixa;
import com.hrsoftware.business.Lancamento;
import com.hrsoftware.dao.DaoAbstrato;

public class LancamentoDAO extends DaoAbstrato<Lancamento> {

	private final String BY_FLUXO_CAIXA = "SELECT l FROM Lancamento l WHERE l.fluxoDeCaixa.id = :idFluxoDeCaixa";

	public LancamentoDAO() {
		super(Lancamento.class);
	}

	public List<Lancamento> selecionaPelo(FluxoDeCaixa fluxoDeCaixa) {

		Map<String, Object> parametros = new LinkedHashMap<>();
		parametros.put("idFluxoDeCaixa", fluxoDeCaixa.getId());

		return super.seleciona(BY_FLUXO_CAIXA, parametros);

	}

}
