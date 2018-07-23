package com.hrsoftware.relatorios;

import java.util.LinkedList;
import java.util.List;

public enum RelatoriosCaminho {

	FECHAMENTO_CAIXA(new String[] { "FechamentoDeCaixa/FechamentoDeCaixa", "FechamentoDeCaixa/TabelaServicoVeiculo",
			"FechamentoDeCaixa/TabelaLancamento" }),
	FECHAMENTO_CAIXA_PERIODO(
					new String[] { "FechamentoDeCaixa_Periodo/FechamentoDeCaixa", "FechamentoDeCaixa_Periodo/TabelaServicoVeiculo",
							"FechamentoDeCaixa_Periodo/TabelaLancamento" });

	public String[] descricao;

	RelatoriosCaminho(String[] descricao) {
		this.descricao = descricao;
	}

	// DA PRA FAZER COM CASE
	public List<String> getRelatorios(RelatoriosCaminho relatorios) {

		if (relatorios == null)
			return null;

		List<String> rela = new LinkedList<>();

		for (RelatoriosCaminho caminho : RelatoriosCaminho.values()) {

			if (relatorios.equals(caminho)) {

				for (String descricao : relatorios.getDescricao()) {

					rela.add(descricao);

				}

			}

		}

		return rela;

	}

	public String[] getDescricao() {
		return descricao;
	}
}
