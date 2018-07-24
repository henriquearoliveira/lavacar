package com.hrsoftware.services;

public enum MethodHTTP {

	GET("GET"), POST("POST"), PUT("PUT"), DELETE("DELETE");

	private String descricao;

	private MethodHTTP(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
