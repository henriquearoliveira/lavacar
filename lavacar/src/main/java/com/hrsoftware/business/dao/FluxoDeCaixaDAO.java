package com.hrsoftware.business.dao;

import com.hrsoftware.business.BusinessClient;
import com.hrsoftware.business.FluxoDeCaixa;
import com.hrsoftware.dao.DaoAbstrato;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FluxoDeCaixaDAO extends DaoAbstrato<FluxoDeCaixa> {

    private final String BY_BUSINESS_CLIENT = "SELECT f FROM FluxoDeCaixa f WHERE f.businessClient.id = :idBusinessClient";

    private final String CAIXA_ABERTO = "SELECT f FROM FluxoDeCaixa f WHERE f.businessClient.id = :idBusinessClient"
            + " AND f.fechado IS FALSE AND f.valorTotal = NULL";

    private final String CAIXA_FECHADO = "SELECT f FROM FluxoDeCaixa f WHERE f.businessClient.id = :idBusinessClient"
            + " AND f.fechado IS TRUE AND f.valorTotal IS NOT NULL";

    public FluxoDeCaixaDAO() {
        super(FluxoDeCaixa.class);
    }

    public FluxoDeCaixa getCaixaAberto(BusinessClient businessClient) {

        Map<String, Object> parametros = new LinkedHashMap<>();
        parametros.put("idBusinessClient", businessClient.getId());

        return super.getBy(CAIXA_ABERTO, parametros);

    }

    public FluxoDeCaixa get(BusinessClient businessClient) {

        Map<String, Object> parametros = new LinkedHashMap<>();
        parametros.put("idBusinessClient", businessClient.getId());

        return super.getBy(BY_BUSINESS_CLIENT, parametros);

    }

    public List<FluxoDeCaixa> seleciona(BusinessClient bunBusinessClient) {

        Map<String, Object> parametros = new LinkedHashMap<>();
        parametros.put("idBusinessClient", bunBusinessClient.getId());

        return super.seleciona(CAIXA_FECHADO, parametros);
    }

}
