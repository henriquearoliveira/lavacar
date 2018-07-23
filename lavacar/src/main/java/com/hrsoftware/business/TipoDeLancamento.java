package com.hrsoftware.business;

import java.util.Arrays;
import java.util.List;

public enum TipoDeLancamento {

	DESPESA("Despesa"), RECEITA("Receita");

	private String descricao;

	private TipoDeLancamento(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static List<String> lista() {
		return Arrays.asList(TipoDeLancamento.DESPESA.descricao, TipoDeLancamento.RECEITA.descricao);
	}

	public static TipoDeLancamento get(String descricao) {

		TipoDeLancamento tipoDeLancamento = null;
		
		for (TipoDeLancamento tipo : TipoDeLancamento.values()) {

			if (tipo.descricao.equals(descricao)) {
				tipoDeLancamento = tipo;
			}

		}

		return tipoDeLancamento;
	}

}
