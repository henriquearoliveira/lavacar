package com.hrsoftware.relatorios;

import java.util.LinkedList;
import java.util.List;

public enum PathReports {

	FECHAMENTO_CAIXA(new String[] { "lavacar-relatorios/FechamentoDeCaixa/FechamentoDeCaixa.jasper",
			"lavacar-relatorios/FechamentoDeCaixa/TabelaServicoVeiculo.jasper",
			"lavacar-relatorios/FechamentoDeCaixa/TabelaLancamento.jasper" }),

	FECHAMENTO_CAIXA_PERIODO(new String[] { "lavacar-relatorios/FechamentoDeCaixa_Periodo/FechamentoDeCaixa.jasper",
			"lavacar-relatorios/FechamentoDeCaixa_Periodo/TabelaServicoVeiculo.jasper",
			"lavacar-relatorios/FechamentoDeCaixa_Periodo/TabelaLancamento.jasper" });

	private String[] description;

	PathReports(String[] description) {
		this.description = description;
	}

	// DA PRA FAZER COM CASE
	public List<String> getRelatorios(PathReports relatorios) {

		if (relatorios == null)
			return null;

		List<String> rela = new LinkedList<>();

		for (PathReports caminho : PathReports.values()) {

			if (relatorios.equals(caminho)) {

				for (String descricao : relatorios.getDescription()) {

					rela.add(descricao);

				}

			}

		}

		return rela;

	}

	public String[] getDescription() {
		return description;
	}

	public void setDescription(String[] description) {
		this.description = description;
	}

}
