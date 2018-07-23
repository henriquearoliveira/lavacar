package com.hrsoftware.components;

public enum TipoMoeda {
	
	Real("R$ "), Dollar("$ ");
	
	private String descricao;

	private TipoMoeda(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}

}
